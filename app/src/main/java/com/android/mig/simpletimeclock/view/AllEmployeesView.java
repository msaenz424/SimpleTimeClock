package com.android.mig.simpletimeclock.view;

import android.database.Cursor;

public interface AllEmployeesView {

    /**
     * Displays a list of all employees
     *
     * @param employeesCursor cursor from database
     */
    void showAllEmployees(Cursor employeesCursor);

    /**
     * Displays a successful update message
     */
    void showStatusUpdateMessage();

}
