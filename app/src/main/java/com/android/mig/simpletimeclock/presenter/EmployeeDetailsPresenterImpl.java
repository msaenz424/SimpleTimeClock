package com.android.mig.simpletimeclock.presenter;

import android.content.Context;

import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractor;
import com.android.mig.simpletimeclock.source.model.EmployeeDetailsInteractorImpl;
import com.android.mig.simpletimeclock.view.EmployeeDetailsView;

public class EmployeeDetailsPresenterImpl implements EmployeeDetailsPresenter,
        EmployeeDetailsInteractor.OnFinishedTransactionListener{

    private EmployeeDetailsView mEmployeeDetailsView;
    private EmployeeDetailsInteractor mEmployeeDetailsInteractor;

    public EmployeeDetailsPresenterImpl(EmployeeDetailsView employeeDetailsView, Context context) {
        this.mEmployeeDetailsView = employeeDetailsView;
        this.mEmployeeDetailsInteractor = new EmployeeDetailsInteractorImpl(context);
    }

    @Override
    public void onResume(int empId) {
        mEmployeeDetailsInteractor.readEmployeeDetails(empId, this);
    }

    @Override
    public void onActionEditClicked(int empId, String name, double wage, String photoPath) {
        mEmployeeDetailsInteractor.editEmployeeDetails(empId, name, wage, photoPath, this);
    }

    @Override
    public void onPayButtonClicked(int empId) {
        mEmployeeDetailsInteractor.updateUnpaidTime(empId, this);
    }

    @Override
    public void onReadSuccess(EmployeeDetails employeeDetails) {
        mEmployeeDetailsView.showEmployeeInfo(employeeDetails);
    }


    @Override
    public void onUpdateSuccess() {
        mEmployeeDetailsView.refreshEmployeeInfo();
    }

    @Override
    public void onUpdateUnpaidTimeSuccess() {
        mEmployeeDetailsView.showPaidMessage();
        mEmployeeDetailsView.refreshEmployeeInfo();
    }
}
