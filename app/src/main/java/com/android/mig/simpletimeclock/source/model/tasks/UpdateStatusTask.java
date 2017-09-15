package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;

public class UpdateStatusTask extends AsyncTask<Object, Void, Boolean> {

    private Context mContext;
    private EmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public UpdateStatusTask(Context context, EmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener){
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        boolean isSuccess = false;
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sqlUpdateQuery = "UPDATE " + TimeClockContract.Employees.TABLE_EMPLOYEES + " SET " + TimeClockContract.Employees.EMP_STATUS + " =? WHERE " + TimeClockContract.Employees.EMP_ID + " =?";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            Integer[] ids = (Integer[]) params[0];
            int status = (int) params[1];

            for (int i = 0; i < ids.length; i++){
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, status);
                sqLiteStatement.bindLong(2, ids[i]);
                sqLiteStatement.execute();
            }
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
        if (isSuccess){
            this.mOnFinishedTransactionListener.onUpdateSuccess();
        }
    }
}
