package com.android.mig.simpletimeclock.presenter;

import android.database.Cursor;

public interface ActiveEmployeesPresenter {

    void onResume();

    void onItemSwiped();

    void onAddButtonClicked();

    void showActiveEmployees(Cursor activeEmployeesCursor);

    void setActiveEmployee(String[] employeeId);

    void resetActiveEmployee(String[] employeeId);
}
