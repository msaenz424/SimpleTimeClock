package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.Timeclock;
import com.android.mig.simpletimeclock.source.model.WorkLogInteractor;
import com.android.mig.simpletimeclock.utils.TimeCalculations;
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
    WorkLogInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public ReadWorkLogRangeTask(Context context, WorkLogInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected ArrayList<Timeclock> doInBackground(Long... params) {
        ArrayList<Timeclock> timeclockArrayList = new ArrayList<>();
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();

        String empIdString = String.valueOf(params[0]);
        String dateStart = String.valueOf(params[1]);
        String dateEnd = String.valueOf(params[2]);
        Log.d("clockin input", dateStart);
        Log.d("clockout input", dateEnd);

        try {
            db.beginTransaction();
            Cursor timeclockCursor = db.rawQuery(DATE_RANGE_WORKLOG_QUERY, new String[]{empIdString, dateStart, dateEnd});
            if (timeclockCursor.moveToFirst()) {
                do {
                    int timeId = timeclockCursor.getInt(ACTIVE_TIME_ID_INDEX);
                    long clockIn = timeclockCursor.getLong(ACTIVE_CLOCKIN_INDEX);
                    long clockOut = timeclockCursor.getLong(ACTIVE_CLOCKOUT_INDEX);
                    double wage = timeclockCursor.getDouble(ACTIVE_WAGE_INDEX);

                    Cursor breaksCursor = db.rawQuery(BREAKS_QUERY, new String[]{String.valueOf(timeId)});

                    Timeclock timeclock = TimeCalculations.Factory.createTimeClockItem(breaksCursor, BREAKS_START_INDEX, BREAKS_END_INDEX, timeId, clockIn, clockOut, wage);
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
    protected void onPostExecute(ArrayList<Timeclock> timeClockArrayList) {
        if (timeClockArrayList != null) {
            mOnFinishedTransactionListener.onReadWorkLogByDateRangeSuccess(timeClockArrayList);
        }
    }

}
