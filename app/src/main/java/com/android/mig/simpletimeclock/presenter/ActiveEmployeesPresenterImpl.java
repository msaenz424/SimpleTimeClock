package com.android.mig.simpletimeclock.presenter;

import android.content.Context;
import android.database.Cursor;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractorImpl;
import com.android.mig.simpletimeclock.view.MainView;

public class ActiveEmployeesPresenterImpl implements ActiveEmployeesPresenter,
        ActiveEmployeesInteractor.OnFinishedTransactionListener{

    private MainView mMainView;
    private ActiveEmployeesInteractor mActiveEmployeesInteractor;

    public ActiveEmployeesPresenterImpl(MainView mainView, Context context) {
        this.mMainView = mainView;
        this.mActiveEmployeesInteractor = new ActiveEmployeesInteractorImpl(context);
    }

    @Override
    public void onResume() {
        this.mActiveEmployeesInteractor.readActiveEmployees(this);
    }

    @Override
    public void onItemSwiped() {

    }

    @Override
    public void onAddButtonClicked() {

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

    @Override
    public void onReadSuccess(Cursor readQuery) {
        mMainView.showActiveEmployees(readQuery);
    }

    @Override
    public void onUpdateSuccess() {

    }
}
