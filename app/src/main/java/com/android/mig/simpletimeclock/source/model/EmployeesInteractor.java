package com.android.mig.simpletimeclock.source.model;

import android.database.Cursor;

public interface EmployeesInteractor {

    Cursor readActiveEmployees();

    Cursor readEmployees();

    int insertEmployee(String name, double wage);

}
