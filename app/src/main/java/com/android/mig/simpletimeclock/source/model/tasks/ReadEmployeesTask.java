package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class ReadEmployeesTask extends AsyncTask<Object[], Void, Cursor>{

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public ReadEmployeesTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Cursor doInBackground(Object[]... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();

        Cursor cursor;
        if (params[0] != null && params[1] != null){
            String[] returnColumns = (String[]) params[0];
            String where = String.valueOf(params[1]);
            cursor = db.query(TimeClockContract.Employees.TABLE_EMPLOYEES, returnColumns, where, null, null, null, null);
        } else {
            cursor = db.query(TimeClockContract.Employees.TABLE_EMPLOYEES, null, null, null, null, null, null);
        }
        return cursor;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        if (cursor != null){
            this.mOnFinishedTransactionListener.onReadSuccess(cursor);
        }
    }
}
