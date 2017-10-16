package com.android.mig.simpletimeclock.source.model;

import android.content.Context;
import com.android.mig.simpletimeclock.source.model.tasks.ReadActiveTimeTask;
import com.android.mig.simpletimeclock.source.model.tasks.UpdateTimeStatusTask;

public class ActiveEmployeesInteractorImpl implements ActiveEmployeesInteractor {

    private Context mContext;

    public ActiveEmployeesInteractorImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void readActiveEmployees(OnFinishedTransactionListener onFinishedTransactionListener) {
        ReadActiveTimeTask readActiveTimeTask = new ReadActiveTimeTask(mContext, onFinishedTransactionListener);
        readActiveTimeTask.execute();
    }

    @Override
    public void updateTimeStatus(int timeId, int breakId, boolean mIsOnBreak, boolean isClockOut, OnFinishedTransactionListener onFinishedTransactionListener) {
        UpdateTimeStatusTask updateTimeStatusTask = new UpdateTimeStatusTask(mContext, onFinishedTransactionListener);
        updateTimeStatusTask.execute(timeId, breakId, mIsOnBreak, isClockOut);
    }
}
