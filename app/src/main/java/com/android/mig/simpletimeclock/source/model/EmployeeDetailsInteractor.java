package com.android.mig.simpletimeclock.source.model;

public interface EmployeeDetailsInteractor {

    interface OnFinishedTransactionListener{
        void onReadSuccess(EmployeeDetails employeeDetails);

        void onUpdateUnpaidTimeSuccess();
    }

    void readEmployeeDetails(int empId, OnFinishedTransactionListener onFinishedTransactionListener);

    void updateUnpaidTime(int empId, OnFinishedTransactionListener onFinishedTransactionListener);
}
