package com.android.mig.simpletimeclock.source.model;

public interface EmployeeDetailsInteractor {

    interface OnFinishedTransactionListener{
        void onReadSuccess(EmployeeDetails employeeDetails);
    }

    void readEmployeeDetails(int empId, OnFinishedTransactionListener onFinishedTransactionListener);
}
