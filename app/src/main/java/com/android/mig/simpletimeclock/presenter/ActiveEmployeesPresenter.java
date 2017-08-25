package com.android.mig.simpletimeclock.presenter;

import android.database.Cursor;

public interface ActiveEmployeesPresenter {

    Cursor getActiveEmployees();

    void showActiveEmployees(Cursor activeEmployeesCursor);

    void setActiveEmployee(String[] employeeId);

    void resetActiveEmployee(String[] employeeId);
}
