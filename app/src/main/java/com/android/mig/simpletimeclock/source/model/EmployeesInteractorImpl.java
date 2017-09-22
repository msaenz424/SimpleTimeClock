package com.android.mig.simpletimeclock.source.model;

import android.content.ContentValues;
import android.content.Context;

import com.android.mig.simpletimeclock.source.TimeClockContract.Employees;
import com.android.mig.simpletimeclock.source.model.tasks.DeleteEmployeeTask;
import com.android.mig.simpletimeclock.source.model.tasks.InsertEmployeeTask;
import com.android.mig.simpletimeclock.source.model.tasks.InsertTimeTask;
import com.android.mig.simpletimeclock.source.model.tasks.ReadEmployeesTask;

public class EmployeesInteractorImpl implements EmployeesInteractor{

    private Context mContext;

    public EmployeesInteractorImpl(Context context) {
        this.mContext = context;
    }

    /** {@inheritDoc} */
    @Override
    public void insertEmployee(String name, double wage, OnFinishedTransactionListener onFinishedTransactionListener) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Employees.EMP_NAME, name);
        contentValues.put(Employees.EMP_WAGE, wage);

        InsertEmployeeTask insertTask = new InsertEmployeeTask(mContext, onFinishedTransactionListener);
        insertTask.execute(contentValues);
    }

    /** {@inheritDoc} */
    @Override
    public void insertTime(Integer[] ids, OnFinishedTransactionListener onFinishedTransactionListeners) {
        InsertTimeTask insertTimeTask = new InsertTimeTask(mContext, onFinishedTransactionListeners);
        insertTimeTask.execute(ids);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteEmployee(Integer[] ids, OnFinishedTransactionListener onFinishedTransactionListener) {
        DeleteEmployeeTask deleteTask = new DeleteEmployeeTask(mContext, onFinishedTransactionListener);
        deleteTask.execute(ids);
    }

    /** {@inheritDoc} */
    @Override
    public void readEmployees(OnFinishedTransactionListener onFinishedTransactionListeners) {
        ReadEmployeesTask readEmployeesTask = new ReadEmployeesTask(mContext, onFinishedTransactionListeners);
        readEmployeesTask.execute();
    }
}
