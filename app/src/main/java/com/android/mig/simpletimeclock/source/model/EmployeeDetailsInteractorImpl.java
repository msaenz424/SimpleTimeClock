package com.android.mig.simpletimeclock.source.model;

import android.content.Context;

import com.android.mig.simpletimeclock.source.model.tasks.ReadEmployeeDetailsTask;
import com.android.mig.simpletimeclock.source.model.tasks.UpdateUnpaidTimeTask;

public class EmployeeDetailsInteractorImpl implements EmployeeDetailsInteractor{

    Context mContext;

    public EmployeeDetailsInteractorImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void readEmployeeDetails(int empId, OnFinishedTransactionListener onFinishedTransactionListener) {
        ReadEmployeeDetailsTask readEmployeeDetailsTask = new ReadEmployeeDetailsTask(mContext, onFinishedTransactionListener);
        readEmployeeDetailsTask.execute(empId);
    }

    @Override
    public void updateUnpaidTime(int empId, OnFinishedTransactionListener onFinishedTransactionListener) {
        UpdateUnpaidTimeTask updateUnpaidTimeTask = new UpdateUnpaidTimeTask(mContext, onFinishedTransactionListener);
        updateUnpaidTimeTask.execute(empId);
    }
}
