package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;

public class UpdateTimeStatusTask extends AsyncTask<Integer, Void, Boolean> {

    private static final int CLOCK_OUT_CODE = 0;
    private static final int BREAK_START_CODE = 1;
    private static final int BREAK_END_CODE = 2;

    private Context mContext;
    private ActiveEmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionActiveListener;

    public UpdateTimeStatusTask(Context context, ActiveEmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionActiveListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        Boolean isSuccess = false;
        int timeId = params[0];
        int actionCode = params[1];

        Log.d("TASK", String.valueOf(actionCode));

        try {
            db.beginTransaction();
            String sqlUpdateQuery = null;
            switch (actionCode) {
                case CLOCK_OUT_CODE:
                    sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " =? WHERE " +
                            TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                    break;
                case BREAK_START_CODE:
                    sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                            TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + " =? WHERE " +
                            TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                    break;
                case BREAK_END_CODE:
                    sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                            TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + " =? WHERE " +
                            TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                    break;
            }

            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindLong(1, System.currentTimeMillis() / 1000);
            sqLiteStatement.bindLong(2, timeId);
            sqLiteStatement.execute();
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
