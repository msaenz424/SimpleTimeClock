package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class DeleteEmployeeTask extends AsyncTask<Integer[], Void, DeleteEmployeeTask.ResponseWrapper>{

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public DeleteEmployeeTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected ResponseWrapper doInBackground(Integer[]... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();

        Integer[] ids = params[0];

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.isDeleted = true;
        for (int i = 0; i < ids.length; i++){
            int deleted = db.delete(TimeClockContract.Employees.TABLE_EMPLOYEES, TimeClockContract.Employees.EMP_ID + "=" + ids[i], null);
            if (deleted == 0){
                responseWrapper.isDeleted = false;
            } else {
                responseWrapper.mCounter++;
            }
        }
        return responseWrapper;
    }

    @Override
    protected void onPostExecute(ResponseWrapper responseWrapper) {
        // if all intended rows are deleted
        if (responseWrapper.isDeleted) {
            this.mOnFinishedTransactionListener.onDeleteSuccess();
        } else {
            // if at least one row is deleted
            if (responseWrapper.mCounter >= 1){
                this.mOnFinishedTransactionListener.onPartialDeleteSuccess();
            } else {
                this.mOnFinishedTransactionListener.onDeleteFail();
            }
        }
    }

    class ResponseWrapper{
        boolean isDeleted;   // counts num of rows deleted
        int mCounter;        // tracks if a row couldn't be deleted
    }
}
