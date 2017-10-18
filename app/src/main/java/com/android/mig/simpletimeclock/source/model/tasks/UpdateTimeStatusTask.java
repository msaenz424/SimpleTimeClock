package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;

public class UpdateTimeStatusTask extends AsyncTask<Object, Void, Boolean> {

    private static final int BREAK_ID_INDEX = 0;
    private static final int BREAK_END_INDEX = 1;

    private final String BREAKS_QUERY = "SELECT " +
            TimeClockContract.Breaks.BREAK_ID + ", " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " FROM " +
            TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
            TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + "=?";

    private Context mContext;
    private ActiveEmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionActiveListener;

    public UpdateTimeStatusTask(Context context, ActiveEmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionActiveListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        Boolean isSuccess = false;
        int timeId = (int) params[0];
        int breakId = (int) params[1];
        boolean isOnBreak = (boolean) params[2];
        boolean isClockOut = (boolean) params[3];

        try {
            db.beginTransaction();

            long currentTime = System.currentTimeMillis() / 1000;
            if (isClockOut) {
                String sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                        TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " =? WHERE " +
                        TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, currentTime);
                sqLiteStatement.bindLong(2, timeId);
                sqLiteStatement.execute();
                sqLiteStatement.close();
                Log.d("UPDATETIMETASK", "updated clock out");

                Cursor breaksCursor = db.rawQuery(BREAKS_QUERY, new String[]{String.valueOf(timeId)});
                if (breaksCursor.moveToLast()) {
                    if (breaksCursor.getLong(BREAK_END_INDEX) == 0) {
                        breakId = breaksCursor.getInt(BREAK_ID_INDEX);
                        isOnBreak = true;
                    }
                }
            }

            if (isOnBreak) {
                String sqlUpdateQuery = "UPDATE " + TimeClockContract.Breaks.TABLE_BREAKS + " SET " +
                        TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " =? WHERE " +
                        TimeClockContract.Breaks.BREAK_ID + " =?";
                SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, currentTime);
                sqLiteStatement.bindLong(2, breakId);
                sqLiteStatement.execute();
                sqLiteStatement.close();
                Log.d("UPDATETIMETASK", "updated break end");
            }

            if (!isOnBreak && !isClockOut){
                String sqlUpdateQuery = "INSERT INTO " + TimeClockContract.Breaks.TABLE_BREAKS + " (" +
                        TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + ", " +
                        TimeClockContract.Breaks.TIMECLOCK_BREAK_START + ") VALUES (?,?);";
                SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, timeId);
                sqLiteStatement.bindLong(2, currentTime);
                sqLiteStatement.executeInsert();
                sqLiteStatement.close();
                Log.d("UPDATETIMETASK", "updated break start");
            }

            db.setTransactionSuccessful();
            isSuccess = true;
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        if (isSuccess) {
            this.mOnFinishedTransactionActiveListener.onUpdateSuccess();
        }
    }
}
