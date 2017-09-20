package com.android.mig.simpletimeclock.source.model;

import android.database.Cursor;

public interface EmployeeDetailsInteractor {

    interface OnFinishedTransactionListener{
        void onReadSuccess(Cursor cursor);
    }

    void readEmployeeDetails(int empId, OnFinishedTransactionListener onFinishedTransactionListener);
}
