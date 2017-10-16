package com.android.mig.simpletimeclock.source.model;

import java.util.ArrayList;

public class ActiveEmployee {
    private int mTimeID;
    private int mEmployeeID;
    private String mEmployeeName;
    private String mPhotoPath;
    private long mClockIn;
    private boolean mIsOnBreak;

    private ArrayList<Break> mBreaksArrayList;

    public ActiveEmployee(int timeID, int employeeID, String employeeName, String photoPath, long clockIn) {
        this.mTimeID = timeID;
        this.mEmployeeID = employeeID;
        this.mEmployeeName = employeeName;
        this.mPhotoPath = photoPath;
        this.mClockIn = clockIn;
        this.mIsOnBreak = false;
    }

    public void setBreaksArrayList(ArrayList<Break> mBreaksArrayList) {
        this.mBreaksArrayList = mBreaksArrayList;
    }

    public int getTimeID() {
        return mTimeID;
    }

    public int getEmployeeID() {
        return mEmployeeID;
    }

    public String getEmployeeName() {
        return mEmployeeName;
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public long getClockIn() {
        return mClockIn;
    }

    public ArrayList<Break> getBreaksArrayList() {
        return mBreaksArrayList;
    }

    public boolean getIsOnBreak() {
        return mIsOnBreak;
    }

    public void setIsOnBreak(boolean isOnBreak) {
        this.mIsOnBreak = isOnBreak;
    }
}