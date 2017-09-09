package com.android.mig.simpletimeclock.model;

import android.database.Cursor;

public interface EmployeesInteractor {

    Cursor readActiveEmployees();

    Cursor readEmployees();

    int insertEmployee(String name, double wage);

}
