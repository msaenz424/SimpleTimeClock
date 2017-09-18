package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class ReadEmployeesTask extends AsyncTask<Void, Void, Cursor> {

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public ReadEmployeesTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        Cursor cursor = db.query(TimeClockContract.Employees.TABLE_EMPLOYEES, null, null, null, null, null, null);

        return cursor;
    }

    @Override
    protected void onPostExecute(Cursor responseCursor) {
        if (responseCursor != null) {
            this.mOnFinishedTransactionListener.onReadSuccess(responseCursor);
        }
    }
}
