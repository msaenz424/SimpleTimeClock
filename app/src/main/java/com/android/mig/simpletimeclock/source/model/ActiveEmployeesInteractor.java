package com.android.mig.simpletimeclock.source.model;

import android.database.Cursor;

public interface ActiveEmployeesInteractor {

    interface OnFinishedTransactionListener{

        void onReadSuccess(Cursor readQuery);

        void onUpdateSuccess();
    }

    void readActiveEmployees(OnFinishedTransactionListener onFinishedTransactionListener);

    void updateTimeStatus(Integer[] ids, OnFinishedTransactionListener onFinishedTransactionListener);


}
