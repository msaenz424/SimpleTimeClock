package com.android.mig.simpletimeclock.presenter;

import android.content.Context;

import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractorImpl;
import com.android.mig.simpletimeclock.view.AllEmployeesView;

public class AllEmployeesPresenterImpl implements AllEmployeesPresenter{

    private AllEmployeesView mAllEmployeesView;
    private EmployeesInteractor mEmployeesInteractor;

    public AllEmployeesPresenterImpl(AllEmployeesView allEmployeesView, Context context) {
        this.mAllEmployeesView = allEmployeesView;
        this.mEmployeesInteractor = new EmployeesInteractorImpl(context);
    }

    @Override
    public void loadAllEmployees() {
        mAllEmployeesView.showAllEmployees(mEmployeesInteractor.readEmployees());
    }

    @Override
    public void addEmployee(String name, double wage) {
        mEmployeesInteractor.insertEmployee(name, wage);
        loadAllEmployees();
    }
}
