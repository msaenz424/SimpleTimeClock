package com.android.mig.simpletimeclock.source.model;

import android.database.Cursor;

public interface EmployeesInteractor {

    Cursor readActiveEmployees();

    /**
     * Retrieves all the rows from Employees table in database
     *
     * @return a cursor with all employees
     */
    Cursor readEmployees();

    int insertEmployee(String name, double wage);

    void updateEmployeeStatus(Integer[] ids, boolean isActive);

}
