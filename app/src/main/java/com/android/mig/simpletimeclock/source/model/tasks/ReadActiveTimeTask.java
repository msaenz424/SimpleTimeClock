package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.ActiveEmployee;
import com.android.mig.simpletimeclock.source.model.Break;

import java.util.ArrayList;

public class ReadActiveTimeTask extends AsyncTask<Void, Void, ArrayList<ActiveEmployee>>{

    private static final int TIMECLOCK_COL_ID_INDEX = 0;
    private static final int TIMECLOCK_COL_EMP_ID_INDEX = 1;
    private static final int TIMECLOCK_COL_NAME_INDEX = 2;
    private static final int TIMECLOCK_COL_PHOTO_PATH = 3;
    private static final int TIMECLOCK_COL_CLOCK_IN_INDEX = 4;

    private static final int BREAK_COL_ID_INDEX = 0;
    private static final int BREAK_COL_BREAK_START_INDEX = 2;
    private static final int BREAK_COL_BREAK_END_INDEX = 3;


    private final String ACTIVE_TIME_QUERY = "SELECT " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
            "e." + TimeClockContract.Employees.EMP_ID + ", " +
            "e." + TimeClockContract.Employees.EMP_NAME + ", " +
            "e." + TimeClockContract.Employees.EMP_PHOTO_PATH + ", " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " FROM " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + " e INNER JOIN " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " t" +
            " ON e." + TimeClockContract.Employees.EMP_ID + " = t." + TimeClockContract.Timeclock.TIMECLOCK_EMP_ID +
            " WHERE t." + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NULL";

    private final String BREAKS_ACTIVE_TIME_QUERY = "SELECT * FROM " +
            TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
            TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + "=?";

    private Context mContext;
    private ActiveEmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionActiveListener;

    public ReadActiveTimeTask(Context context, ActiveEmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionActiveListener = onFinishedTransactionListener;
    }

    @Override
    protected ArrayList<ActiveEmployee> doInBackground(Void... voids) {
        ArrayList<ActiveEmployee> activeEmployeesArrayList = new ArrayList<>();
        Cursor activeTimeCursor;
        Cursor breaksCursor;

        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();
        try {
            db.beginTransaction();
            activeTimeCursor = db.rawQuery(ACTIVE_TIME_QUERY, null);

            if (activeTimeCursor.moveToFirst()){
                do {
                    ActiveEmployee activeEmployee = new ActiveEmployee(
                            activeTimeCursor.getInt(TIMECLOCK_COL_ID_INDEX),
                            activeTimeCursor.getInt(TIMECLOCK_COL_EMP_ID_INDEX),
                            activeTimeCursor.getString(TIMECLOCK_COL_NAME_INDEX),
                            activeTimeCursor.getString(TIMECLOCK_COL_PHOTO_PATH),
                            activeTimeCursor.getLong(TIMECLOCK_COL_CLOCK_IN_INDEX));

                    Log.d("REACACTIVETIMETASK", activeTimeCursor.getString(TIMECLOCK_COL_NAME_INDEX));
                    Log.d("REACACTIVETIMETASK", String.valueOf(activeTimeCursor.getLong(TIMECLOCK_COL_CLOCK_IN_INDEX)));

                    breaksCursor = db.rawQuery(BREAKS_ACTIVE_TIME_QUERY, new String[]{String.valueOf(activeEmployee.getTimeID())});
                    if (breaksCursor.getCount() > 0) {
                        Log.d("READACTIVETIME", "cursor exists");
                        breaksCursor.moveToFirst();
                        ArrayList<Break> breakArrayList = new ArrayList<>();
                        do {
                            Log.d("BREAK LOOP", "break item");
                            Break breakObject = new Break(
                                    breaksCursor.getInt(BREAK_COL_ID_INDEX),
                                    breaksCursor.getLong(BREAK_COL_BREAK_START_INDEX),
                                    breaksCursor.getLong(BREAK_COL_BREAK_END_INDEX));
                            breakArrayList.add(breakObject);
                        } while (breaksCursor.moveToNext());
                        breaksCursor.close();
                        activeEmployee.setIsOnBreak(true);
                        activeEmployee.setBreaksArrayList(breakArrayList);
                    }
                    activeEmployeesArrayList.add(activeEmployee);
                } while (activeTimeCursor.moveToNext());
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return activeEmployeesArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<ActiveEmployee> responseCursor) {
        if (responseCursor != null){
            Log.d("READACTIVETIMETASK", "read success");
            mOnFinishedTransactionActiveListener.onReadSuccess(responseCursor);
        }
    }
}