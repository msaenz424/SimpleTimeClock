package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;
import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class UpdateStatusTask extends AsyncTask<Object, Void, UpdateStatusTask.ResponseWrapper> {

    private static int ACTIVE_STATUS = 1;

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;
    private ActiveEmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionActiveListener;

    public UpdateStatusTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    public UpdateStatusTask(Context context, ActiveEmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionActiveListener = onFinishedTransactionListener;
    }

    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try {
            db.beginTransaction();
            String sqlUpdateQuery = "UPDATE " + TimeClockContract.Employees.TABLE_EMPLOYEES + " SET " +
                    TimeClockContract.Employees.EMP_STATUS + " =? WHERE " + TimeClockContract.Employees.EMP_ID + " =?";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            Integer[] ids = (Integer[]) params[0];
            int status = (int) params[1];

            responseWrapper.changedToActiveStatus = false;
            if (status == ACTIVE_STATUS) {
                responseWrapper.changedToActiveStatus = true;
            }

            for (int i = 0; i < ids.length; i++){
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, status);
                sqLiteStatement.bindLong(2, ids[i]);
                sqLiteStatement.execute();
            }
            db.setTransactionSuccessful();
            responseWrapper.isSuccess = true;

        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return responseWrapper;
    }

    @Override
    protected void onPostExecute(ResponseWrapper response) {
        if (response.isSuccess){
            if (response.changedToActiveStatus){
                this.mOnFinishedTransactionListener.onUpdateSuccess();
            } else {
                this.mOnFinishedTransactionActiveListener.onUpdateSuccess();
            }

        }
    }

    class ResponseWrapper{
        private boolean isSuccess;
        private boolean changedToActiveStatus;
    }
}
