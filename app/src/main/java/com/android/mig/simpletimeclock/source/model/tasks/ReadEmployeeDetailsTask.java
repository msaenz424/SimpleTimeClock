package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractor;

public class ReadEmployeeDetailsTask extends AsyncTask<Integer, Void, Cursor> {

    private final int INACTIVE_TIME_STATUS = 0;
    private final String PERIODIC_QUERY = "SELECT sum(cast(" +
            "(strftime('%s'," +  TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + ") - strftime('%s'," + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + "))" +
            " as real)/60/60 as elapsed)" +
            " FROM " + TimeClockContract.Timeclock.TABLE_TIMECLOCK +
            " WHERE " + TimeClockContract.Timeclock.TIMECLOCK_STATUS + " =? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =?";

    private final String EMPLOYEE_DETAILS_QUERY = "SELECT " +
            "e." + TimeClockContract.Employees.EMP_NAME + ", " +
            "e." + TimeClockContract.Employees.EMP_WAGE + ", " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + ", " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + " FROM " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + " e INNER JOIN " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " t" +
            " ON e." + TimeClockContract.Employees.EMP_ID + " = t." + TimeClockContract.Timeclock.TIMECLOCK_EMP_ID +
            " WHERE t." + TimeClockContract.Timeclock.TIMECLOCK_STATUS + " =?";

    Context mContext;
    EmployeeDetailsInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public ReadEmployeeDetailsTask(Context context, EmployeeDetailsInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Cursor doInBackground(Integer... id) {
        Cursor responseCursor = null;
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();
        try {
            db.beginTransaction();
            responseCursor = db.rawQuery(PERIODIC_QUERY, new String[]{String.valueOf(INACTIVE_TIME_STATUS), String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return responseCursor;
    }
}
