package com.android.mig.simpletimeclock.presenter;

import android.content.Context;

import com.android.mig.simpletimeclock.source.model.ActiveEmployee;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractorImpl;
import com.android.mig.simpletimeclock.view.MainView;

import java.util.ArrayList;

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
    public void onItemSwiped(int timeId) {
        this.mActiveEmployeesInteractor.updateTimeStatus(timeId, 0, false, true, this);
    }

    @Override
    public void onItemTimerClicked(int timeId, int breakId, boolean isOnBreak) {
        this.mActiveEmployeesInteractor.updateTimeStatus(timeId, breakId, isOnBreak, false, this);
    }

    @Override
    public void onReadSuccess(ArrayList<ActiveEmployee> activeEmployeesArrayList) {
        mMainView.showActiveEmployees(activeEmployeesArrayList);
    }

    @Override
    public void onUpdateSuccess() {
        onResume();
    }
}
