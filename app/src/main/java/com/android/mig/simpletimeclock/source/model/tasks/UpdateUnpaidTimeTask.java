package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractor;

public class UpdateUnpaidTimeTask extends AsyncTask<Integer, Void, Boolean>{

    private final int PAID_STATUS = 1;
    private final int UNPAID_STATUS = 0;

    private Context mContext;
    private EmployeeDetailsInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public UpdateUnpaidTimeTask(Context context, EmployeeDetailsInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        Boolean isSuccess = false;
        int empdId = params[0];
        try {
            db.beginTransaction();
            String sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                    TimeClockContract.Timeclock.TIMECLOCK_PAID + " =? WHERE " +
                    TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " =? AND " +
                    TimeClockContract.Timeclock.TIMECLOCK_PAID + " =?";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            sqLiteStatement.clearBindings();
            sqLiteStatement.bindLong(1, PAID_STATUS);
            sqLiteStatement.bindLong(2, empdId);
            sqLiteStatement.bindLong(3, UNPAID_STATUS);
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
            this.mOnFinishedTransactionListener.onUpdateUnpaidTimeSuccess();
        }
    }
}
