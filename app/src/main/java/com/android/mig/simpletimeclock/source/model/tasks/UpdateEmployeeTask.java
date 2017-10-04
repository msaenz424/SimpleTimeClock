package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractor;

public class UpdateEmployeeTask extends AsyncTask<Object, Void, Boolean> {

    private Context mContext;
    private EmployeeDetailsInteractor.OnFinishedTransactionListener mOnFinishedTransactionListener;

    public UpdateEmployeeTask(Context context, EmployeeDetailsInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        Boolean isSuccess = false;
        int empId = (int) params[0];
        String empName = (String) params[1];
        double empWage = (double) params[2];
        String empPhotoPath = (String) params[3];

        try {
            db.beginTransaction();
            String sqlUpdateQuery = "UPDATE " + TimeClockContract.Employees.TABLE_EMPLOYEES + " SET " +
                    TimeClockContract.Employees.EMP_NAME + " =?, " +
                    TimeClockContract.Employees.EMP_WAGE + " =?, " +
                    TimeClockContract.Employees.EMP_PHOTO_PATH + " =? WHERE " +
                    TimeClockContract.Employees.EMP_ID + " =?";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            sqLiteStatement.clearBindings();
            sqLiteStatement.bindString(1, empName);
            sqLiteStatement.bindDouble(2, empWage);
            sqLiteStatement.bindString(3, empPhotoPath);
            sqLiteStatement.bindLong(4, empId);

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
        if (isSuccess){
            this.mOnFinishedTransactionListener.onUpdateSuccess();
        }
    }
}
