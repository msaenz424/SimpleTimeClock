package com.android.mig.simpletimeclock.source.model;

import android.content.Context;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.model.tasks.ReadEmployeesTask;

public class ActiveEmployeesInteractorImpl implements ActiveEmployeesInteractor {

    private static int ACTIVE_STATUS = 1;

    private Context mContext;

    public ActiveEmployeesInteractorImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void readActiveEmployees(OnFinishedTransactionListener onFinishedTransactionListener) {
        String[] returnColumns = {TimeClockContract.Employees.EMP_ID, TimeClockContract.Employees.EMP_NAME};
        String where = TimeClockContract.Employees.EMP_STATUS + " = " + ACTIVE_STATUS;

        ReadEmployeesTask readEmployeesTask = new ReadEmployeesTask(mContext, onFinishedTransactionListener);
        readEmployeesTask.execute(returnColumns, where);
    }

    @Override
    public void updateEmployeeStatus(Integer[] ids, boolean isActive, OnFinishedTransactionListener onFinishedTransactionListener) {

    }
}
