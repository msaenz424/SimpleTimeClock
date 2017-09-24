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
    public void onActionAddClicked(String name, double wage, String photoUri) {
        mEmployeesInteractor.insertEmployee(name, wage, photoUri, this);
    }

    @Override
    public void onActionDoneClicked(Integer[] ids) {
        mEmployeesInteractor.insertTime(ids, this);
    }

    @Override
    public void onActionDeleteClicked(Integer[] ids) {
        mEmployeesInteractor.deleteEmployee(ids, this);
    }

    /** {@inheritDoc} */
    @Override
    public void onReadSuccess(Cursor readQuery) {
        mAllEmployeesView.showAllEmployees(readQuery);
    }

    /** {@inheritDoc} */
    @Override
    public void onInsertSuccess() {
        onResume();
    }

    /** {@inheritDoc} */
    @Override
    public void onInsertTimeSuccess() {
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
