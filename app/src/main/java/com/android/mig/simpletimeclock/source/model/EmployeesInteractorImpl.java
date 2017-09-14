package com.android.mig.simpletimeclock.source.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract.Employees;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;

public class EmployeesInteractorImpl implements EmployeesInteractor{

    private static int ACTIVE_STATUS = 1;
    private static int INACTIVE_STATUS = 0;

    private Context mContext;

    public EmployeesInteractorImpl(Context context) {
        this.mContext = context;
    }

    /** {@inheritDoc} */
    @Override
    public void insertEmployee(String name, double wage, OnFinishedTransactionListener onFinishedTransactionListener) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Employees.EMP_NAME, name);
        contentValues.put(Employees.EMP_STATUS, INACTIVE_STATUS);
        contentValues.put(Employees.EMP_WAGE, wage);

        TimeClockDbHelper timeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = timeClockDbHelper.getWritableDatabase();
        long id = db.insert(Employees.TABLE_EMPLOYEES, null, contentValues);
        if (id != -1){
            onFinishedTransactionListener.onInsertSuccess();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateEmployeeStatus(Integer[] ids, boolean isActive, OnFinishedTransactionListener onFinishedTransactionListeners) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();

        int mStatus = INACTIVE_STATUS;
        if (isActive){
            mStatus = ACTIVE_STATUS;
        }
        try {
            db.beginTransaction();
            String sqlUpdateQuery = "UPDATE " + Employees.TABLE_EMPLOYEES + " SET " + Employees.EMP_STATUS + " =? WHERE " + Employees.EMP_ID + " =?";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);

            for (int i = 0; i < ids.length; i++){
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, mStatus);
                sqLiteStatement.bindLong(2, ids[i]);
                sqLiteStatement.execute();
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
            onFinishedTransactionListeners.onUpdateSuccess();
        }
    }

    @Override
    public Cursor readActiveEmployees() {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();

        String returnColumns[] = {Employees.EMP_ID, Employees.EMP_NAME};
        String where = Employees.EMP_STATUS + " = " + ACTIVE_STATUS;

        Cursor cursor = db.query(Employees.TABLE_EMPLOYEES, returnColumns, where, null, null, null, null);
        if (cursor.getCount() > 0)
            return cursor;
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void readEmployees(OnFinishedTransactionListener onFinishedTransactionListeners) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();
        Cursor cursor = db.query(Employees.TABLE_EMPLOYEES, null, null, null, null, null, null);
        if (cursor != null){
            onFinishedTransactionListeners.onReadSuccess(cursor);
        }
    }

    public static boolean deleteEmployee(TimeClockDbHelper timeClockDbHelper, int id){
        final SQLiteDatabase db = timeClockDbHelper.getWritableDatabase();
        int deleted = db.delete(Employees.TABLE_EMPLOYEES, Employees.EMP_ID + "=" + id, null);
        if (deleted > 0)
            return true;
        return false;
    }
}
