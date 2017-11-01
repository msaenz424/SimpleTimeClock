package com.android.mig.simpletimeclock.source.model;

public class Timeclock {

    private int mTimeId;
    private long mClockIn, mClockOut;
    private int mHoursWorkedInSeconds;
    private double mEarned;

    public Timeclock(int timeId, long clockIn, long clockOut, int hoursWorkedInSeconds, double earned) {

        this.mTimeId = timeId;
        this.mClockIn = clockIn;
        this.mClockOut = clockOut;
        this.mHoursWorkedInSeconds = hoursWorkedInSeconds;
        this.mEarned = earned;
    }

    public int getTimeId() {
        return mTimeId;
    }

    public void setTimeId(int timeId) {
        this.mTimeId = mTimeId;
    }

    public long getClockIn() {
        return mClockIn;
    }

    public void setClockIn(long clockIn) {
        this.mClockIn = clockIn;
    }

    public long getClockOut() {
        return mClockOut;
    }

    public void setClockOut(long clockOut) {
        this.mClockOut = clockOut;
    }

    public int getHoursWorked() {
        return mHoursWorkedInSeconds;
    }

    public void setHoursWorked(int hoursWorked) {
        this.mHoursWorkedInSeconds = hoursWorked;
    }

    public double getEarned() {
        return mEarned;
    }

    public void setEarned(double earned) {
        this.mEarned = mEarned;
    }

}
