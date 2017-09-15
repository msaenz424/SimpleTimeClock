package com.android.mig.simpletimeclock.presenter;

public interface AllEmployeesPresenter {

    void onResume();

    void onActionAddClicked(String name, double wage);

    void onActionDoneClicked(Integer[] ids, boolean isActive);

    void onActionDeleteClicked(Integer[] ids);

}
