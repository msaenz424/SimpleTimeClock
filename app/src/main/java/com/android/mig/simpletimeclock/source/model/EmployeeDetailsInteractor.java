package com.android.mig.simpletimeclock.source.model;

import java.util.ArrayList;

public interface EmployeeDetailsInteractor {

    interface OnFinishedTransactionListener{
        /**
         * Triggers a response after a successful query
         *
         * @param employeeDetails
         * @param timeclockArrayList
         */
        void onReadSuccess(EmployeeDetails employeeDetails, ArrayList<Timeclock> timeclockArrayList);

        /**
         * Triggers a response after a successful query by date range
         *
         * @param timeclockArrayList    a work log
         */
        void onReadWorkLogByDateRangeSuccess(ArrayList<Timeclock> timeclockArrayList);

        /**
         * Triggers a response after a successful update against the database
         */
        void onUpdateSuccess();

        /**
         * Triggers a response after a successful update against the database
         */
        void onUpdateUnpaidTimeSuccess();
    }

    /**
     * Obtains details of an employee
     *
     * @param empId                             employee id
     * @param onFinishedTransactionListener     callback listener
     */
    void readEmployeeDetails(int empId, OnFinishedTransactionListener onFinishedTransactionListener);

    /**
     * Retrieves a work log by date range
     *
     * @param empId                             employee id
     * @param dateStart                         starting date
     * @param dateEnd                           ending date
     * @param onFinishedTransactionListener     callback listener
     */
    void readWorkLogByDateRange(int empId, long dateStart, long dateEnd, OnFinishedTransactionListener onFinishedTransactionListener);

    /**
     * Updates an employee's info
     *
     * @param empId                             employee id
     * @param name                              employee name
     * @param wage                              employee wage
     * @param photoPath                         a string path of employee's photo
     * @param onFinishedTransactionListener     callback listener
     */
    void editEmployeeDetails(int empId, String name, double wage, String photoPath, OnFinishedTransactionListener onFinishedTransactionListener);

    /**
     * Marks the data set of worked hours as paid by an update against the database
     *
     * @param empId                             employee id
     * @param onFinishedTransactionListener     callback listener
     */
    void updateUnpaidTime(int empId, OnFinishedTransactionListener onFinishedTransactionListener);
}
