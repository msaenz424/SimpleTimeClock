package com.android.mig.simpletimeclock.source.model;

import android.database.Cursor;

public interface EmployeesInteractor {

    interface OnFinishedTransactionListener{

        /**
         * Triggers a response after a successful query in database
         *
         * @param readQuery cursor result of the query
         */
        void onReadSuccess(Cursor readQuery);

        /**
         * Triggers a response after a successful insert in database
         */
        void onInsertSuccess();

        /**
         * Triggers a response after a successful insert of a new time in database
         */
        void onInsertTimeSuccess();

        /**
         * Triggers a response for a successful deletion
         */
        void onDeleteSuccess();

        /**
         * Triggers a response for partial deletions
         */
        void onPartialDeleteSuccess();

        /**
         * Triggers a response if no deletion was performed
         */
        void onDeleteFail();
    }

    /**
     * Performs a query to Employees table in database
     *
     * @param onFinishedTransactionListener callback listener
     */
    void readEmployees(OnFinishedTransactionListener onFinishedTransactionListener);

    /**
     * Performs an insert into Employees table in database
     *
     * @param name                              employees name
     * @param wage                              employees wage
     * @param onFinishedTransactionListener     callback listener
     */
    void insertEmployee(String name, double wage, OnFinishedTransactionListener onFinishedTransactionListener);

    /**
     * Performs a status update in Employees table in database
     *
     * @param ids                               employees id
     * @param onFinishedTransactionListener     callback listener
     */
    void insertTime(Integer[] ids, OnFinishedTransactionListener onFinishedTransactionListener);

    /**
     * Delete rows from Employees table in database
     *
     * @param ids                               employees id
     * @param onFinishedTransactionListener     callback listener
     */
    void deleteEmployee(Integer[] ids, OnFinishedTransactionListener onFinishedTransactionListener);

}
