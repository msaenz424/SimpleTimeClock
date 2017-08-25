package com.android.mig.simpletimeclock.presenter;

import android.database.Cursor;

public interface ActiveEmployeesPresenter {

    Cursor loadActiveEmployees();

    void setActiveEmployee(String[] employeeId);

    void resetActiveEmployee(String[] employeeId);
}
