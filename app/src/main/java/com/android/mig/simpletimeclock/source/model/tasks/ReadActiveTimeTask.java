package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;

public class ReadActiveTimeTask extends AsyncTask<Void, Void, Cursor>{

    private final String ACTIVE_TIME_QUERY = "SELECT " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
            "e." + TimeClockContract.Employees.EMP_ID + ", " +
            "e." + TimeClockContract.Employees.EMP_NAME + ", " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + ", " +
            "t." + TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + " FROM " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + " e INNER JOIN " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " t" +
            " ON e." + TimeClockContract.Employees.EMP_ID + " = t." + TimeClockContract.Timeclock.TIMECLOCK_EMP_ID +
            " WHERE t." + TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " IS NULL";

    private Context mContext;
    private ActiveEmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionActiveListener;

    public ReadActiveTimeTask(Context context, ActiveEmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionActiveListener = onFinishedTransactionListener;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        Cursor responseCursor = null;
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();
        try {
            db.beginTransaction();
            responseCursor = db.rawQuery(ACTIVE_TIME_QUERY, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return responseCursor;
    }

    @Override
    protected void onPostExecute(Cursor responseCursor) {
        if (responseCursor != null){
            mOnFinishedTransactionActiveListener.onReadSuccess(responseCursor);
        }
    }
}
