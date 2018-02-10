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
import com.android.mig.simpletimeclock.source.model.Timeclock;

import java.math.BigDecimal;
import java.util.ArrayList;


public class ReadEmployeeDetailsTask extends AsyncTask<Integer, Void, ReadEmployeeDetailsTask.ResultWrapper> {

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
    private int mTotalTimeWorkedInMinutes = 0;
    private double mTotalEarnings = 0;

    public ReadEmployeeDetailsTask(Context context, EmployeeDetailsInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected ResultWrapper doInBackground(Integer... params) {
        ArrayList<Timeclock> timeclockArrayList = new ArrayList<>();
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

                Timeclock timeclock = createTimeClockItem(db, timeId, clockIn, timeNow, wage);
                timeclockArrayList.add(timeclock);
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

                    Timeclock timeclock = createTimeClockItem(db, timeId, clockInTime, clockOutTime, wage);
                    timeclockArrayList.add(timeclock);

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

            employeeDetails = new EmployeeDetails(Integer.valueOf(empId), empName, empWage, empPhotoUri, mTotalTimeWorkedInMinutes, mTotalEarnings, isWorking);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        ResultWrapper resultWrapper = new ResultWrapper(employeeDetails, timeclockArrayList);
        return resultWrapper;
    }

    @Override
    protected void onPostExecute(ResultWrapper resultWrapper) {
        if (resultWrapper.getEmployeeDetails() != null) {
            this.mOnFinishedTransactionListener.onReadSuccess(resultWrapper.getEmployeeDetails(), resultWrapper.getTimeclockArrayList());
        }
    }

    class ResultWrapper{

        private EmployeeDetails mEmployeeDetails;
        private ArrayList<Timeclock> mTimeclockArrayList;

        public ResultWrapper(EmployeeDetails employeeDetails, ArrayList<Timeclock> timeclockArrayList) {
            this.mEmployeeDetails = employeeDetails;
            this.mTimeclockArrayList = timeclockArrayList;
        }

        public EmployeeDetails getEmployeeDetails() {
            return mEmployeeDetails;
        }

        public ArrayList<Timeclock> getTimeclockArrayList() {
            return mTimeclockArrayList;
        }
    }

    /**
     * Calculates the breaks in minutes
     *
     * @param breaksCursor  cursor containing breaks from db
     * @param timeNow       system time used as starting point
     * @return              total break rounded in minutes
     */
    private int calculateBreakInMinutes(Cursor breaksCursor, long timeNow){
        double breakSum = 0;
        if (breaksCursor.getCount() > 0){
            breaksCursor.moveToFirst();
            do {
                long breakStart = breaksCursor.getLong(BREAKS_START_INDEX);
                long breakEnd = breaksCursor.getLong(BREAKS_END_INDEX);
                if (breakEnd == 0) {
                    breakEnd = timeNow;
                }
                breakSum += (int) (breakEnd - breakStart);
            } while (breaksCursor.moveToNext());
        }
        breaksCursor.close();
        int breakInMinutes = (int) Math.round(breakSum / 60);
        return breakInMinutes;
    }

    /**
     * Calculates the earning in decimals
     *
     * @param minutesWorked actual minutes worked
     * @param wage          employee's wage
     * @return              decimal expression of earnings
     */
    private double calculateEarningsInDecimals(int minutesWorked, double wage){
        double earned;
        double decimalHours = (double) minutesWorked / 60;
        earned = (decimalHours * wage);
        BigDecimal earningsBigDecimal = new BigDecimal(String.valueOf(earned));                 // string on BigDecimal helps to preserve precision
        earningsBigDecimal = earningsBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        earned = earningsBigDecimal.doubleValue();
        return earned;
    }

    /**
     * Calculates time span from two values
     *
     * @param clockOutTime  end time value
     * @param clockInTime   start time value
     * @return              time span in minutes
     */
    private int calculateTimeSpanInMinutes(long clockOutTime, long clockInTime) {
        double doubleClockOutTime = (double) clockOutTime;
        double doubleClockIntTime = (double) clockInTime;
        int workedTimeInMinutes = (int) Math.round((doubleClockOutTime - doubleClockIntTime) / 60);
        return workedTimeInMinutes;
    }

    /**
     * Adds up the totals of time worked and earnings
     *
     * @param timeWorked    time worked in minutes
     * @param earned        earning
     */
    private void sumTotals(int timeWorked, double earned){
        mTotalTimeWorkedInMinutes += timeWorked;
        mTotalEarnings += earned;
    }

    /**
     * Creates a new Timeclock object
     *
     * @param db            database
     * @param timeId        timeclock id
     * @param clockStart    clock in time in seconds
     * @param clockEnd      clock out time in seconds
     * @param wage          employee's wage
     * @return              a new Timeclock object
     */
    private Timeclock createTimeClockItem(SQLiteDatabase db, int timeId, long clockStart, long clockEnd, double wage){
        Cursor breaksCursor = db.rawQuery(BREAKS_QUERY, new String[] {String.valueOf(timeId)});
        int currentBreakInMinutes = calculateBreakInMinutes(breaksCursor, clockEnd);
        int currentTimeSpanInMinutes = calculateTimeSpanInMinutes(clockEnd, clockStart);

        int currentTimeWorkedInMinutes =  currentTimeSpanInMinutes - currentBreakInMinutes;
        double currentEarningsInDecimals = calculateEarningsInDecimals(currentTimeWorkedInMinutes, wage);

        sumTotals(currentTimeWorkedInMinutes, currentEarningsInDecimals);

        Timeclock timeclock = new Timeclock(timeId, clockStart, clockEnd, currentTimeWorkedInMinutes, currentBreakInMinutes, currentEarningsInDecimals);
        return timeclock;
    }

}
