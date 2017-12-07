package com.android.mig.simpletimeclock.presenter;

public interface AllEmployeesPresenter {

    /**
     * Called when Activity starts
     */
    void onResume();

    /**
     * Called when the Add button is clicked
     *
     * @param name          employee name
     * @param wage          employee wage
     * @param photoUri      path of employee's photo
     */
    void onActionAddClicked(String name, double wage, String photoUri);

    /**
     * Called when the Done button is clicked
     *
     * @param ids           array of employees' ids
     */
    void onActionDoneClicked(Integer[] ids);

    /**
     * Called when the Delete button is clicked
     *
     * @param ids           array of employee's ids
     */
    void onActionDeleteClicked(Integer[] ids);

}
