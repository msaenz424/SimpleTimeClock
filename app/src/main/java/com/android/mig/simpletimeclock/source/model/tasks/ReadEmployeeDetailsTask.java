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

public class ReadEmployeeDetailsTask extends AsyncTask<Integer, Void, EmployeeDetails> {

    private final int PAID_STATUS = 1;
    private final int UNPAID_STATUS = 0;

    private final String ACTIVE_TIME_QUERY = "SELECT " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE + " FROM " +
            TimeClockContract.Timeclock.TABLE_TIMECLOCK + " WHERE " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NULL";

    private final String PERIODIC_QUERY = "SELECT sum((" +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " - " + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN +
            ") - (" + TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + " - " + TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + ")), " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE +
            " FROM " + TimeClockContract.Timeclock.TABLE_TIMECLOCK +
            " WHERE " + TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_PAID + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NOT NULL GROUP BY " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE;

    private final String TOTALS_QUERY = "SELECT sum((" +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " - " + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN +
            ") - (" + TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + " - " + TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + ")), " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE +
            " FROM " + TimeClockContract.Timeclock.TABLE_TIMECLOCK +
            " WHERE " + TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_PAID + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NOT NULL GROUP BY " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE;

    private final String EMPLOYEE_QUERY = "SELECT " +
            TimeClockContract.Employees.EMP_NAME + ", " +
            TimeClockContract.Employees.EMP_WAGE + ", " +
            TimeClockContract.Employees.EMP_PHOTO_PATH + " FROM " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + " WHERE " +
            TimeClockContract.Employees.EMP_ID + " =?";

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

            Cursor currentCursor = db.rawQuery(ACTIVE_TIME_QUERY, new String[]{empId});
            long currentTime = 0;
            double currentEarnings = 0;

            if (currentCursor.getCount() > 0) {
                isWorking = true;
                currentCursor.moveToPosition(0);
                currentTime = (((System.currentTimeMillis() / 1000) - currentCursor.getLong(0)) - (currentCursor.getLong(1) - currentCursor.getLong(2)));
                currentEarnings = currentCursor.getDouble(3) * currentTime / 3600;
            }
            currentCursor.close();

            Cursor unpaidCursor = db.rawQuery(PERIODIC_QUERY, new String[]{empId, String.valueOf(UNPAID_STATUS)});
            long unpaidPreviousTime = 0;
            double unpaidPreviousEarnings = 0;
            if (unpaidCursor.getCount() > 0) {
                unpaidCursor.moveToPosition(0);
                do {
                    unpaidPreviousTime += unpaidCursor.getLong(0);
                    unpaidPreviousEarnings += unpaidCursor.getDouble(1) * unpaidCursor.getLong(0) / 3600;
                } while (unpaidCursor.moveToNext());
            }
            unpaidCursor.close();

            long totalUnpaidTime = currentTime + unpaidPreviousTime;
            double totalUnpaidEarnings = currentEarnings + unpaidPreviousEarnings;
            Cursor paidCursor = db.rawQuery(TOTALS_QUERY, new String[]{empId, String.valueOf(PAID_STATUS)});
            long paidTime = 0;
            double paidEarnings = 0;
            if (paidCursor.getCount() > 0) {
                paidCursor.moveToPosition(0);
                do {
                    paidTime += paidCursor.getLong(0);
                    paidEarnings += paidCursor.getDouble(1) * paidCursor.getLong(0) / 3600;
                } while (paidCursor.moveToNext());
            }
            paidCursor.close();

            long totalTime = (totalUnpaidTime + paidTime);
            double totalEarnings = totalUnpaidEarnings + paidEarnings;
            Cursor employeeCursor = db.rawQuery(EMPLOYEE_QUERY, new String[]{empId});
            String empName = null;
            double empWage = 0;
            String empPhotoUri = null;
            if (employeeCursor.getCount() > 0) {
                employeeCursor.moveToPosition(0);
                empName = employeeCursor.getString(0);
                empWage = employeeCursor.getDouble(1);
                empPhotoUri = employeeCursor.getString(2);
            }
            employeeCursor.close();

            employeeDetails = new EmployeeDetails(Integer.valueOf(empId), empName, empWage, empPhotoUri, totalUnpaidTime, totalUnpaidEarnings, totalTime, totalEarnings, isWorking);

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
