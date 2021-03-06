package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class InsertTimeTask extends AsyncTask<Integer[], Void, Boolean> {

    private final String EMPLOYEE_QUERY = "SELECT " +
            TimeClockContract.Employees.EMP_WAGE + " FROM " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + " WHERE " +
            TimeClockContract.Employees.EMP_ID + " =?";

    private final String IS_ACTIVE_QUERY = "SELECT *  FROM " +
            TimeClockContract.Timeclock.TABLE_TIMECLOCK + " WHERE " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NULL";

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public InsertTimeTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Integer[]... params) {
        boolean isSuccess = false;
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sqlUpdateQuery = "INSERT INTO " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " (" +
                    TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + ", " +
                    TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
                    TimeClockContract.Timeclock.TIMECLOCK_WAGE + ", " +
                    TimeClockContract.Timeclock.TIMECLOCK_PAID +
                    ") VALUES (?,?,?,0);";

            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            Integer[] ids = params[0];

            for (int i = 0; i < ids.length; i++) {
                // checks if employee was already added to active list
                Cursor isActiveCursor = db.rawQuery(IS_ACTIVE_QUERY, new String[]{String.valueOf(ids[i])});
                if (isActiveCursor == null || isActiveCursor.getCount() == 0) {
                    // gets the current wage for the corresponding employee
                    Cursor employeeCursor = db.rawQuery(EMPLOYEE_QUERY, new String[]{String.valueOf(ids[i])});
                    employeeCursor.moveToPosition(0);
                    double empWage = employeeCursor.getDouble(0);
                    employeeCursor.close();

                    // performs the insert (clocks in)
                    sqLiteStatement.clearBindings();
                    sqLiteStatement.bindLong(1, ids[i]);
                    sqLiteStatement.bindLong(2, System.currentTimeMillis() / 1000);
                    sqLiteStatement.bindDouble(3, empWage);
                    sqLiteStatement.executeInsert();
                }
                if (isActiveCursor != null) {
                    isActiveCursor.close();
                }
            }
            isSuccess = true;
            db.setTransactionSuccessful();
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
            this.mOnFinishedTransactionListener.onInsertTimeSuccess();
        }
    }
}
