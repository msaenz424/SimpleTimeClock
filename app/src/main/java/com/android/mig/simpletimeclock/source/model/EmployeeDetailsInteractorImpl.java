package com.android.mig.simpletimeclock.source.model;

import android.content.Context;

import com.android.mig.simpletimeclock.source.model.tasks.ReadEmployeeDetailsTask;
import com.android.mig.simpletimeclock.source.model.tasks.ReadWorkLogRangeTask;
import com.android.mig.simpletimeclock.source.model.tasks.UpdateEmployeeTask;
import com.android.mig.simpletimeclock.source.model.tasks.UpdateUnpaidTimeTask;

public class EmployeeDetailsInteractorImpl implements EmployeeDetailsInteractor{

    private Context mContext;

    public EmployeeDetailsInteractorImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void readEmployeeDetails(int empId, OnFinishedTransactionListener onFinishedTransactionListener) {
        ReadEmployeeDetailsTask readEmployeeDetailsTask = new ReadEmployeeDetailsTask(mContext, onFinishedTransactionListener);
        readEmployeeDetailsTask.execute(empId);
    }

    @Override
    public void readWorkLogByDateRange(int empId, long dateStart, long dateEnd, OnFinishedTransactionListener onFinishedTransactionListener) {
        ReadWorkLogRangeTask readWorkLogRangeTask = new ReadWorkLogRangeTask(mContext, onFinishedTransactionListener);
        readWorkLogRangeTask.execute((long) empId, dateStart, dateEnd);
    }

    @Override
    public void editEmployeeDetails(int empId, String name, double wage, String photoPath, OnFinishedTransactionListener onFinishedTransactionListener) {
        UpdateEmployeeTask updateEmployeeTask = new UpdateEmployeeTask(mContext, onFinishedTransactionListener);
        updateEmployeeTask.execute(empId, name, wage, photoPath);
    }

    @Override
    public void updateUnpaidTime(int empId, OnFinishedTransactionListener onFinishedTransactionListener) {
        UpdateUnpaidTimeTask updateUnpaidTimeTask = new UpdateUnpaidTimeTask(mContext, onFinishedTransactionListener);
        updateUnpaidTimeTask.execute(empId);
    }
}
