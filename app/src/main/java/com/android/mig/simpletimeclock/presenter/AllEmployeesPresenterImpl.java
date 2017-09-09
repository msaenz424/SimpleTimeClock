package com.android.mig.simpletimeclock.presenter;

import android.content.Context;

import com.android.mig.simpletimeclock.model.EmployeesInteractor;
import com.android.mig.simpletimeclock.model.EmployeesInteractorImpl;
import com.android.mig.simpletimeclock.view.AllEmployeesView;

public class AllEmployeesPresenterImpl implements AllEmployeesPresenter{

    private AllEmployeesView mAllEmployeesView;
    private EmployeesInteractor mEmployeesInteractor;

    public AllEmployeesPresenterImpl(AllEmployeesView allEmployeesView) {
        this.mAllEmployeesView = allEmployeesView;
        this.mEmployeesInteractor = new EmployeesInteractorImpl((Context) allEmployeesView);
    }

    @Override
    public void loadAllEmployees() {
        mAllEmployeesView.showAllEmployees(mEmployeesInteractor.readEmployees());
    }

    @Override
    public void addEmployee(String name, double wage) {
        mEmployeesInteractor.insertEmployee(name, wage);
    }
}
