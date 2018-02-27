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
     * Displays a message when payment was made
     */
    void showPaidMessage();

    /**
     * Displays updated employee's info
     */
    void refreshEmployeeInfo();

}
