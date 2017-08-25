package com.android.mig.simpletimeclock.presenter;


import android.content.Context;
import android.database.Cursor;

import com.android.mig.simpletimeclock.model.EmployeesInteractor;
import com.android.mig.simpletimeclock.model.EmployeesInteractorImpl;
import com.android.mig.simpletimeclock.view.MainView;

public class ActiveEmployeesPresenterImpl implements ActiveEmployeesPresenter{

    private MainView mMainView;
    private EmployeesInteractor mEmployeesInteractor;

    public ActiveEmployeesPresenterImpl(MainView mainView) {
        this.mMainView = mainView;
        this.mEmployeesInteractor = new EmployeesInteractorImpl((Context) mainView);
    }

    @Override
    public Cursor loadActiveEmployees() {
        //mMainView.showActiveEmployees(mEmployeesInteractor.readActiveEmployees());
        return mEmployeesInteractor.readActiveEmployees();
    }

    @Override
    public void setActiveEmployee(String[] employeeId) {

    }

    @Override
    public void resetActiveEmployee(String[] employeeId) {

    }
}
