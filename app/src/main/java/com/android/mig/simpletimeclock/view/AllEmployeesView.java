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

    /**
     * Displays Done FAB
     */
    void showFabDoneButton();

    /**
     * Hides Done FAB
     */
    void hideFabDoneButton();

    /**
     * Exits action mode, clears selection list, reset values,
     * and performs a new query on database for updated data
     */
    void resetScreen();

}
