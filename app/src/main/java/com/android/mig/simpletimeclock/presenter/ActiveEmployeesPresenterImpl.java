package com.android.mig.simpletimeclock.presenter;


import android.content.Context;
import android.database.Cursor;

import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractorImpl;
import com.android.mig.simpletimeclock.view.MainView;

public class ActiveEmployeesPresenterImpl implements ActiveEmployeesPresenter{

    private MainView mMainView;
    private EmployeesInteractor mEmployeesInteractor;

    public ActiveEmployeesPresenterImpl(MainView mainView) {
        this.mMainView = mainView;
        this.mEmployeesInteractor = new EmployeesInteractorImpl((Context) mainView);
    }

    @Override
    public Cursor getActiveEmployees() {
        return mEmployeesInteractor.readActiveEmployees();
    }

    @Override
    public void showActiveEmployees(Cursor activeEmployeesCursor) {
        mMainView.showActiveEmployees(activeEmployeesCursor);
    }

    @Override
    public void setActiveEmployee(String[] employeeId) {

    }

    @Override
    public void resetActiveEmployee(String[] employeeId) {

    }
}
