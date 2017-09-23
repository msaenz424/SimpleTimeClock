package com.android.mig.simpletimeclock.view;

import com.android.mig.simpletimeclock.source.model.EmployeeDetails;

public interface EmployeeDetailsView {

    void showEmployeeInfo(EmployeeDetails employeeDetails);

    void showPaidMessage();

    void refreshEmployeeInfo();

}
