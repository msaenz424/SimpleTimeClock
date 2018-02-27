package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractor;
import com.android.mig.simpletimeclock.utils.TimeCalculations;

public class ReadEmployeeDetailsTask extends AsyncTask<Integer, Void, EmployeeDetails> {

    private final int PAID_STATUS = 1;
    private final int UNPAID_STATUS = 0;

    private final int ACTIVE_TIME_ID_INDEX = 0;
    private final int ACTIVE_CLOCKIN_INDEX = 1;
    private final int ACTIVE_WAGE_INDEX = 2;

    private final int BY_PAID_TIME_ID_INDEX = 0;
    private final int BY_PAID_CLOCKIN_INDEX = 1;
    private final int BY_PAID_CLOCKOUT_INDEX = 2;
    private final int BY_PAID_WAGE_INDEX = 3;

    private final int EMPLOYEES_NAME_INDEX = 0;
    private final int EMPLOYEES_WAGE_INDEX = 1;
    private final int EMPLOYEES_PHOTO_INDEX = 2;

    private final int BREAKS_START_INDEX = 0;
    private final int BREAKS_END_INDEX = 1;

    private final String ACTIVE_TIME_QUERY = "SELECT " +
            TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE + " FROM " +
            TimeClockContract.Timeclock.TABLE_TIMECLOCK + " WHERE " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NULL";

    private final String BY_PAID_TIME_QUERY = "SELECT " +
            TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE + " FROM " +
            TimeClockContract.Timeclock.TABLE_TIMECLOCK + " WHERE " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + "=? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_PAID + "=? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NOT NULL ORDER BY " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " DESC";

    private final String EMPLOYEE_QUERY = "SELECT " +
            TimeClockContract.Employees.EMP_NAME + ", " +
            TimeClockContract.Employees.EMP_WAGE + ", " +
            TimeClockContract.Employees.EMP_PHOTO_PATH + " FROM " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + " WHERE " +
            TimeClockContract.Employees.EMP_ID + " =?";

    private final String BREAKS_QUERY = "SELECT " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_START + ", " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " FROM " +
            TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
            TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + "=?";

    private Context mContext;
    private EmployeeDetailsInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public ReadEmployeeDetailsTask(Context context, EmployeeDetailsInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected EmployeeDetails doInBackground(Integer... params) {
        EmployeeDetails employeeDetails = null;
        boolean isWorking = false;
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();

        String empId = String.valueOf(params[0]);

        try {
            db.beginTransaction();

            // calculate the earnings of an active time if there is any
            Cursor currentCursor = db.rawQuery(ACTIVE_TIME_QUERY, new String[]{empId});
            if (currentCursor.moveToFirst()) {
                isWorking = true;
                int timeId = currentCursor.getInt(ACTIVE_TIME_ID_INDEX);
                long clockIn = currentCursor.getLong(ACTIVE_CLOCKIN_INDEX);
                double wage = currentCursor.getDouble(ACTIVE_WAGE_INDEX);
                long timeNow = (System.currentTimeMillis() / 1000);

                Cursor breaksCursor = db.rawQuery(BREAKS_QUERY, new String[] {String.valueOf(timeId)});

                TimeCalculations.Factory.createTimeClockItemWithTotals(breaksCursor, BREAKS_START_INDEX, BREAKS_END_INDEX, timeId, clockIn, timeNow, wage);
            }
            currentCursor.close();

            // calculate the earnings of unpaid times (active time not included)
            Cursor unpaidCursor = db.rawQuery(BY_PAID_TIME_QUERY, new String[]{empId, String.valueOf(UNPAID_STATUS)});
            if (unpaidCursor.moveToFirst()) {
                do {
                    int timeId = unpaidCursor.getInt(BY_PAID_TIME_ID_INDEX);
                    long clockInTime = unpaidCursor.getLong(BY_PAID_CLOCKIN_INDEX);
                    long clockOutTime = unpaidCursor.getLong(BY_PAID_CLOCKOUT_INDEX);
                    double wage = unpaidCursor.getDouble(BY_PAID_WAGE_INDEX);

                    Cursor breaksCursor = db.rawQuery(BREAKS_QUERY, new String[] {String.valueOf(timeId)});

                    TimeCalculations.Factory.createTimeClockItemWithTotals(breaksCursor, BREAKS_START_INDEX, BREAKS_END_INDEX, timeId, clockInTime, clockOutTime, wage);

                } while (unpaidCursor.moveToNext());
            }
            unpaidCursor.close();

            // gets employee's personal info
            Cursor employeeCursor = db.rawQuery(EMPLOYEE_QUERY, new String[]{empId});
            String empName = null;
            double empWage = 0;
            String empPhotoUri = null;
            if (employeeCursor.getCount() > 0) {
                employeeCursor.moveToFirst();
                empName = employeeCursor.getString(EMPLOYEES_NAME_INDEX);
                empWage = employeeCursor.getDouble(EMPLOYEES_WAGE_INDEX);
                empPhotoUri = employeeCursor.getString(EMPLOYEES_PHOTO_INDEX);
            }
            employeeCursor.close();

            int totalTimeWorkedInMinutes = TimeCalculations.Factory.getMTotalTimeWorkedInMinutes();
            double totalEarnings = TimeCalculations.Factory.getMTotalEarnings();

            employeeDetails = new EmployeeDetails(Integer.valueOf(empId), empName, empWage, empPhotoUri, totalTimeWorkedInMinutes, totalEarnings, isWorking);

            TimeCalculations.Factory.setMTotalTimeWorkedInMinutes(0);
            TimeCalculations.Factory.setMTotalEarnings(0);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return employeeDetails;
    }

    @Override
    protected void onPostExecute(EmployeeDetails employeeDetails) {
        if (employeeDetails != null) {
            this.mOnFinishedTransactionListener.onReadSuccess(employeeDetails);
        }
    }

}
