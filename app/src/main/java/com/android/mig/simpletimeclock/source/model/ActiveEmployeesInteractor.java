package com.android.mig.simpletimeclock.source.model;

import java.util.ArrayList;

public interface ActiveEmployeesInteractor {

    interface OnFinishedTransactionListener{

        void onReadSuccess(ArrayList<ActiveEmployee> activeEmployeesArrayList);

        void onUpdateSuccess();
    }

    void readActiveEmployees(OnFinishedTransactionListener onFinishedTransactionListener);

    void updateTimeStatus(int timeId, int breakId, boolean isOnBreak, boolean isClockOut, OnFinishedTransactionListener onFinishedTransactionListener);

}
