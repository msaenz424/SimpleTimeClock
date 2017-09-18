package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class InsertTimeTask extends AsyncTask<Integer[], Void, Boolean>{

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public InsertTimeTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Integer[]... params) {
        int insertCounter = 0;
        boolean isSuccess = false;
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sqlUpdateQuery = "INSERT INTO " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " (" +
                    TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + ", " +
                    TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
                    TimeClockContract.Timeclock.TIMECLOCK_STATUS + ", " +
                    TimeClockContract.Timeclock.TIMECLOCK_PAID +
                    ") VALUES (?,?,1,0);";

            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            Integer[] ids = params[0];

            for (int i = 0; i < ids.length; i++){
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, ids[i]);
                sqLiteStatement.bindLong(2, System.currentTimeMillis() / 1000 );
                Long rowInserted = sqLiteStatement.executeInsert();
                if (rowInserted != -1) {
                    insertCounter++;
                }
            }
            if (insertCounter == ids.length){
                  isSuccess = true;
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        if (isSuccess){
            this.mOnFinishedTransactionListener.onUpdateSuccess();
        }
    }
}
