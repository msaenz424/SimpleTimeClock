package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class InsertTask extends AsyncTask<ContentValues, Void, Long> {

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public InsertTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Long doInBackground(ContentValues... contentValues) {
        TimeClockDbHelper timeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = timeClockDbHelper.getWritableDatabase();
        long id = db.insert(TimeClockContract.Employees.TABLE_EMPLOYEES, null, contentValues[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        if (aLong != -1){
            this.mOnFinishedTransactionListener.onInsertSuccess();
        }
    }
}
