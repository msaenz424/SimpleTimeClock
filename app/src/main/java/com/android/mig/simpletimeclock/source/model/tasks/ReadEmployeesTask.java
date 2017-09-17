package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class ReadEmployeesTask extends AsyncTask<Object, Void, ReadEmployeesTask.ResponseWrapper>{

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;
    private ActiveEmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionActiveListener;

    public ReadEmployeesTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    public ReadEmployeesTask(Context context, ActiveEmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionActiveListener = onFinishedTransactionListener;
    }

    @Override
    protected ResponseWrapper doInBackground(Object... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();

        ResponseWrapper responseWrapper = new ResponseWrapper();
        if (params[0] != null && params[1] != null){
            responseWrapper.isActive = true;
            String[] returnColumns = (String[]) params[0];
            String where = String.valueOf(params[1]);
            responseWrapper.mCursor = db.query(TimeClockContract.Employees.TABLE_EMPLOYEES, returnColumns, where, null, null, null, null);
        } else {
            responseWrapper.isActive = false;
            responseWrapper.mCursor = db.query(TimeClockContract.Employees.TABLE_EMPLOYEES, null, null, null, null, null, null);
        }
        return responseWrapper;
    }

    @Override
    protected void onPostExecute(ResponseWrapper response) {
        if (response.mCursor != null){
            if (response.isActive){
                this.mOnFinishedTransactionActiveListener.onReadSuccess(response.mCursor);
            } else {
                this.mOnFinishedTransactionListener.onReadSuccess(response.mCursor);
            }
        }
    }

    class ResponseWrapper{
        private boolean isActive;
        private Cursor mCursor;
    }
}
