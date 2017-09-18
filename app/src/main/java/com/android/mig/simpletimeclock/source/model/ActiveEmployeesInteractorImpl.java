package com.android.mig.simpletimeclock.source.model;

import android.content.Context;
import com.android.mig.simpletimeclock.source.model.tasks.ReadActiveTimeTask;

public class ActiveEmployeesInteractorImpl implements ActiveEmployeesInteractor {

    private Context mContext;

    public ActiveEmployeesInteractorImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void readActiveEmployees(OnFinishedTransactionListener onFinishedTransactionListener) {
        ReadActiveTimeTask readActiveTimeTask = new ReadActiveTimeTask(mContext, onFinishedTransactionListener);
        readActiveTimeTask.execute();
    }

    @Override
    public void updateEmployeeStatus(Integer[] ids, boolean isActive, OnFinishedTransactionListener onFinishedTransactionListener) {

    }
}
