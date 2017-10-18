package com.android.mig.simpletimeclock.presenter;

public interface ActiveEmployeesPresenter {

    void onResume();

    void onItemSwiped(int timeId);

    void onItemTimerClicked(int timeId, int breakId, boolean isOnBreak);

}
