package com.android.mig.simpletimeclock.view;

import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.source.model.Timeclock;

import java.util.ArrayList;

public interface EmployeeDetailsView {

    void showEmployeeInfo(EmployeeDetails employeeDetails);

    void saveWorkLogInfo(ArrayList<Timeclock> timeclockArrayList);

    void showPaidMessage();

    void refreshEmployeeInfo();

}
