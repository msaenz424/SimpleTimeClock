package com.android.mig.simpletimeclock.presenter;


public interface AllEmployeesPresenter {

    void loadAllEmployees();

    void addEmployee(String name, double wage);

    void setActiveEmployees(Integer[] ids, boolean isActive);

}
