package com.android.mig.simpletimeclock.source.model;

public interface EmployeeDetailsInteractor {

    interface OnFinishedTransactionListener{
        void onReadSuccess(EmployeeDetails employeeDetails);

        void onUpdateSuccess();

        void onUpdateUnpaidTimeSuccess();
    }

    void readEmployeeDetails(int empId, OnFinishedTransactionListener onFinishedTransactionListener);

    void editEmployeeDetails(int empId, String name, double wage, String photoPath, OnFinishedTransactionListener onFinishedTransactionListener);

    void updateUnpaidTime(int empId, OnFinishedTransactionListener onFinishedTransactionListener);
}
