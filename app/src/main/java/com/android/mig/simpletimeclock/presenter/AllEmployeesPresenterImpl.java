package com.android.mig.simpletimeclock.presenter;

import android.content.Context;
import android.database.Cursor;

import com.android.mig.simpletimeclock.source.model.EmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.EmployeesInteractorImpl;
import com.android.mig.simpletimeclock.view.AllEmployeesView;

public class AllEmployeesPresenterImpl implements AllEmployeesPresenter,
        EmployeesInteractor.OnFinishedTransactionListener{

    private AllEmployeesView mAllEmployeesView;
    private EmployeesInteractor mEmployeesInteractor;

    public AllEmployeesPresenterImpl(AllEmployeesView allEmployeesView, Context context) {
        this.mAllEmployeesView = allEmployeesView;
        this.mEmployeesInteractor = new EmployeesInteractorImpl(context);
    }

    @Override
    public void onResume() {
        mEmployeesInteractor.readEmployees(this);
    }

    @Override
    public void onActionAddClicked(String name, double wage) {
        mEmployeesInteractor.insertEmployee(name, wage, this);
    }

    @Override
    public void onActionDoneClicked(Integer[] ids, boolean isActive) {
        mEmployeesInteractor.updateEmployeeStatus(ids, isActive, this);
    }

    @Override
    public void onActionDeleteClicked(Integer[] ids) {
        mEmployeesInteractor.deleteEmployee(ids, this);
    }

    private void loadAllEmployees(Cursor employees){
        mAllEmployeesView.showAllEmployees(employees);
    }

    /** {@inheritDoc} */
    @Override
    public void onReadSuccess(Cursor readQuery) {
        loadAllEmployees(readQuery);
    }

    /** {@inheritDoc} */
    @Override
    public void onInsertSuccess() {
        onResume();
    }

    /** {@inheritDoc} */
    @Override
    public void onUpdateSuccess() {
        mAllEmployeesView.showStatusUpdateMessage();
        mAllEmployeesView.resetScreen();
    }

    /** {@inheritDoc} */
    @Override
    public void onDeleteSuccess() {
        mAllEmployeesView.showSuccessDeleteMessage();
        mAllEmployeesView.resetScreen();
    }

    /** {@inheritDoc} */
    @Override
    public void onPartialDeleteSuccess() {
        mAllEmployeesView.showPartialDeleteMessage();
        mAllEmployeesView.resetScreen();
    }

    /** {@inheritDoc} */
    @Override
    public void onDeleteFail() {
        mAllEmployeesView.showFailedDeleteMessage();
        mAllEmployeesView.resetScreen();
    }
}
