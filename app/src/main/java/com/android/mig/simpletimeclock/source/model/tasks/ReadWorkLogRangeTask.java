package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractor;
import com.android.mig.simpletimeclock.source.model.Timeclock;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ReadWorkLogRangeTask extends AsyncTask<Long, Void, ArrayList<Timeclock>> {

    private final int ACTIVE_TIME_ID_INDEX = 0;
    private final int ACTIVE_CLOCKIN_INDEX = 1;
    private final int ACTIVE_CLOCKOUT_INDEX = 2;
    private final int ACTIVE_WAGE_INDEX = 3;

    private final int BREAKS_START_INDEX = 0;
    private final int BREAKS_END_INDEX = 1;

    private final String DATE_RANGE_WORKLOG_QUERY = "SELECT " +
            TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE + " FROM " +
            TimeClockContract.Timeclock.TABLE_TIMECLOCK + " WHERE " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + "=? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " >=? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " <=? ORDER BY " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " DESC";

    private final String BREAKS_QUERY = "SELECT " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_START + ", " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " FROM " +
            TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
            TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + "=?";

    private Context mContext;
    EmployeeDetailsInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public ReadWorkLogRangeTask(Context context, EmployeeDetailsInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected ArrayList<Timeclock> doInBackground(Long... params) {
        ArrayList<Timeclock> timeclockArrayList = new ArrayList<>();
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();

        String empIdString = String.valueOf(params[0]);
        String clockInString = String.valueOf(params[1]);
        String clockOutString = String.valueOf(params[2]);
        Log.d("clockin input", clockInString);
        Log.d("clockout input", clockOutString);

        try {
            db.beginTransaction();
            Cursor timeclockCursor = db.rawQuery(DATE_RANGE_WORKLOG_QUERY, new String[]{empIdString, clockInString, clockOutString});
            if (timeclockCursor.moveToFirst()) {
                long timeNow = (System.currentTimeMillis() / 1000);
                do {
                    int timeId = timeclockCursor.getInt(ACTIVE_TIME_ID_INDEX);
                    long clockIn = timeclockCursor.getLong(ACTIVE_CLOCKIN_INDEX);
                    long clockOut = timeclockCursor.getLong(ACTIVE_CLOCKOUT_INDEX);
                    Log.d("clockIn", String.valueOf(clockIn));
                    Log.d("clockOut", String.valueOf(clockOut));

                    double wage = timeclockCursor.getDouble(ACTIVE_WAGE_INDEX);
                    int currentTime = 0;
                    double currentEarnings = 0;

                    Cursor breaksCursor = db.rawQuery(BREAKS_QUERY, new String[]{String.valueOf(timeId)});
                    int currentBreakTime = calculateBreaks(breaksCursor, timeNow);
                    if (clockOut == 0) {
                        currentTime = (int) (timeNow - clockIn - currentBreakTime);
                    } else {
                        currentTime = (int) (clockOut - clockIn - currentBreakTime);
                    }

                    currentEarnings = calculateDecimalEarnings(currentTime, wage);

                    Timeclock timeclock = new Timeclock(timeId, clockIn, clockOut, currentTime, currentBreakTime, currentEarnings);
                    timeclockArrayList.add(timeclock);
                } while (timeclockCursor.moveToNext());
            }
            timeclockCursor.close();
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return timeclockArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Timeclock> timeclockArrayList) {
        if (timeclockArrayList != null) {
            mOnFinishedTransactionListener.onReadWorkLogByDateRangeSuccess(timeclockArrayList);
        }
    }

    private int calculateBreaks(Cursor breaksCursor, long timeNow){
        int breakSum = 0;
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
        return breakSum;
    }

    private double calculateDecimalEarnings(int secondsWorked, double wage){
        double earned;
        int hours = secondsWorked / 3600;
        int minutes = (secondsWorked % 3600) / 60;
        double decimalMinutes = (double) minutes / 60;
        earned = (hours * wage) + (decimalMinutes * wage);
        BigDecimal earningsBigDecimal = new BigDecimal(String.valueOf(earned));                 // string on BigDecimal helps to preserve precision
        earningsBigDecimal = earningsBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        earned = earningsBigDecimal.doubleValue();
        return earned;
    }

}
