package com.android.mig.simpletimeclock.view;

import com.android.mig.simpletimeclock.source.model.ActiveEmployee;

import java.util.ArrayList;

public interface MainView {

    /**
     * Displays a list of employees who are currently working
     *
     * @param activeEmployeesArrayList  array list of active employees
     */
    void showActiveEmployees(ArrayList<ActiveEmployee> activeEmployeesArrayList);

}
