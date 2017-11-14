package com.android.mig.simpletimeclock.presenter;

public interface EmployeeDetailsPresenter {

    void onResume(int empId);

    void onActionEditClicked(int empId, String name, double wage, String photoPath);

    void onPayButtonClicked(int empId);

    void onCustomWorkLogButtonClicked(int empId, long dateStart, long dateEnd);

}
