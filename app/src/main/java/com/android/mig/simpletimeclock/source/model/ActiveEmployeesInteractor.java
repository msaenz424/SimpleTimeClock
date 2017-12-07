package com.android.mig.simpletimeclock.source.model;

import java.util.ArrayList;

public interface ActiveEmployeesInteractor {

    interface OnFinishedTransactionListener{

        /**
         * Triggers a response after a successful query against the database
         *
         * @param activeEmployeesArrayList  an array list of employees who are working
         */
        void onReadSuccess(ArrayList<ActiveEmployee> activeEmployeesArrayList);

        /**
         * Triggers a response after a successful update against the database
         */
        void onUpdateSuccess();
    }

    /**
     * Queries employees who are currently working
     *
     * @param onFinishedTransactionListener callback listener
     */
    void readActiveEmployees(OnFinishedTransactionListener onFinishedTransactionListener);

    /**
     * Updates the current working status of an employee. It can be
     * either start break or a clock out
     *
     * @param timeId                        current time id
     * @param breakId                       current break id
     * @param isOnBreak                     boolean that tells if employee is on break or not
     * @param isClockOut                    boolean that tells if employee is clocking out
     * @param onFinishedTransactionListener callback listener
     */
    void updateTimeStatus(int timeId, int breakId, boolean isOnBreak, boolean isClockOut, OnFinishedTransactionListener onFinishedTransactionListener);

}
