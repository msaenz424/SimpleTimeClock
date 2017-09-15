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
     * Displays a successful delete message
     */
    void showSuccessDeleteMessage();

    /**
     * Warns the user that some items couldn't be deleted
     */
    void showPartialDeleteMessage();

    /**
     * Warns the user that the deletion failed
     */
    void showFailedDeleteMessage();

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
