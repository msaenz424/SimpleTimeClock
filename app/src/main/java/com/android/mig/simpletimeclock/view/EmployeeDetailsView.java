package com.android.mig.simpletimeclock.view;

import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.source.model.Timeclock;

import java.util.ArrayList;

public interface EmployeeDetailsView {

    /**
     * Displays an employee's info
     *
     * @param employeeDetails   employee object
     */
    void showEmployeeInfo(EmployeeDetails employeeDetails);

    /**
     * Holds the unpaid work log in memory
     *
     * @param timeclockArrayList    work log
     */
    void saveWorkLogInfo(ArrayList<Timeclock> timeclockArrayList);

    /**
     * Displays the work log by date range
     *
     * @param timeclockArrayList    work log
     */
    void showWorkLogByDateRange(ArrayList<Timeclock> timeclockArrayList);

    /**
     * Displays a message when payment was made
     */
    void showPaidMessage();

    /**
     * Displays updated employee's info
     */
    void refreshEmployeeInfo();

}
