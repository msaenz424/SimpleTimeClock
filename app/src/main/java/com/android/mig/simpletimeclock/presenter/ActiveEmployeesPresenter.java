package com.android.mig.simpletimeclock.presenter;

public interface ActiveEmployeesPresenter {

    /**
     * Called when Activity starts
     */
    void onResume();

    /**
     * Called when an item from the active list is swipe left or right
     *
     * @param timeId        time id
     */
    void onItemSwiped(int timeId);

    /**
     * Called when user taps on timer for break
     *
     * @param timeId        time id
     * @param breakId       break id
     * @param isOnBreak     boolean that tell if current employee is on break
     */
    void onItemTimerClicked(int timeId, int breakId, boolean isOnBreak);

}
