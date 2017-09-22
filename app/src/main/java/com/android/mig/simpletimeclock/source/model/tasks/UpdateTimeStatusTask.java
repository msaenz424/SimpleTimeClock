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

public class UpdateTimeStatusTask extends AsyncTask<Integer, Void, Boolean> {

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
        int empId = params[1];
        try {
            db.beginTransaction();
            String sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                    TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " =? WHERE " +
                    TimeClockContract.Timeclock.TIMECLOCK_ID + " =? AND " +
                    TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =?";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            sqLiteStatement.clearBindings();
            sqLiteStatement.bindLong(1, System.currentTimeMillis() / 1000);
            sqLiteStatement.bindLong(2, timeId);
            sqLiteStatement.bindLong(3, empId);
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
