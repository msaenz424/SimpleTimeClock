package com.android.mig.simpletimeclock.presenter;

import android.content.Context;
import android.database.Cursor;

import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractor;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractorImpl;
import com.android.mig.simpletimeclock.view.EmployeeDetailsView;

public class EmployeeDetailsPresenterImpl implements EmployeeDetailsPresenter,
        EmployeeDetailsInteractor.OnFinishedTransactionListener{

    EmployeeDetailsView mEmployeeDetailsView;
    EmployeeDetailsInteractor mEmployeeDetailsInteractor;

    public EmployeeDetailsPresenterImpl(EmployeeDetailsView employeeDetailsView, Context context) {
        this.mEmployeeDetailsView = employeeDetailsView;
        this.mEmployeeDetailsInteractor = new EmployeeDetailsInteractorImpl(context);
    }

    @Override
    public void onResume(int empId) {
        mEmployeeDetailsInteractor.readEmployeeDetails(empId, this);
    }

    @Override
    public void onActionEditClicked() {

    }

    @Override
    public void onReadSuccess(Cursor cursor) {

    }
}
