package com.android.mig.simpletimeclock.source.model;

import android.content.Context;
import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.model.tasks.ReadEmployeesTask;
import com.android.mig.simpletimeclock.source.model.tasks.UpdateStatusTask;

public class ActiveEmployeesInteractorImpl implements ActiveEmployeesInteractor {

    private static int ACTIVE_STATUS = 1;
    private static int INACTIVE_STATUS = 0;

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
        int mStatus = INACTIVE_STATUS;
        if (isActive){
            mStatus = ACTIVE_STATUS;
        }
        UpdateStatusTask updateStatusTask = new UpdateStatusTask(mContext, onFinishedTransactionListener);
        updateStatusTask.execute(ids, mStatus);
    }
}
