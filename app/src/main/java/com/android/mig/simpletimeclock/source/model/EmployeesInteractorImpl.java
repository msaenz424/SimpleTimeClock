package com.android.mig.simpletimeclock.source.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.android.mig.simpletimeclock.source.TimeClockContract.Employees;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.tasks.DeleteTask;
import com.android.mig.simpletimeclock.source.model.tasks.InsertTask;
import com.android.mig.simpletimeclock.source.model.tasks.ReadEmployeesTask;
import com.android.mig.simpletimeclock.source.model.tasks.UpdateStatusTask;

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

        InsertTask insertTask = new InsertTask(mContext, onFinishedTransactionListener);
        insertTask.execute(contentValues);
    }

    /** {@inheritDoc} */
    @Override
    public void updateEmployeeStatus(Integer[] ids, boolean isActive, OnFinishedTransactionListener onFinishedTransactionListeners) {
        int mStatus = INACTIVE_STATUS;
        if (isActive){
            mStatus = ACTIVE_STATUS;
        }
        UpdateStatusTask updateStatusTask = new UpdateStatusTask(mContext, onFinishedTransactionListeners);
        updateStatusTask.execute(ids, mStatus);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteEmployee(Integer[] ids, OnFinishedTransactionListener onFinishedTransactionListener) {
        DeleteTask deleteTask = new DeleteTask(mContext, onFinishedTransactionListener);
        deleteTask.execute(ids);
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
        ReadEmployeesTask readEmployeesTask = new ReadEmployeesTask(mContext, onFinishedTransactionListeners);
        readEmployeesTask.execute(null, null);
    }
}
