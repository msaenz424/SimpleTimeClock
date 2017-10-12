package com.android.mig.simpletimeclock.presenter;

public interface ActiveEmployeesPresenter {

    void onResume();

    void onItemSwiped(Integer[] ids);

    void onItemTimerClicked(int timeId, int actionCode);

}
