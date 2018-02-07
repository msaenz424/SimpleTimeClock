package com.android.mig.simpletimeclock.presenter;

public interface EmployeeDetailsPresenter {

    /**
     * Called when Activity starts
     *
     * @param empId         employee id
     */
    void onResume(int empId);

    /**
     * Called when the Edit button is clicked
     *
     * @param empId         employee id
     * @param name          employee name
     * @param wage          employee wage
     * @param photoPath     path of employee's photo
     */
    void onActionEditClicked(int empId, String name, double wage, String photoPath);

    /**
     * Called when the Pay button is clicked
     *
     * @param empId         employee id
     */
    void onPayButtonClicked(int empId);

}
