package com.android.mig.simpletimeclock.source.model;

public class EmployeeDetails {
    private int mID;
    private String mName;
    private double mWage;
    private long mUnpaidTimeWorked;
    private double mUnpaidEarnings;
    private long mTotalTimeWorked;
    private double mTotalEarnings;

    public EmployeeDetails(int id, String name, double wage){
        this.mID = id;
        this.mName = name;
        this.mWage = wage;
    }

    public EmployeeDetails(int id, String name, double wage, long unpaidTimeWorked, double unpaidEarnings, long totalTimeWorked, double totalEarnings) {
        this.mID = id;
        this.mName = name;
        this.mWage = wage;
        this.mUnpaidTimeWorked = unpaidTimeWorked;
        this.mUnpaidEarnings = unpaidEarnings;
        this.mTotalTimeWorked = totalTimeWorked;
        this.mTotalEarnings = totalEarnings;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getWage() {
        return mWage;
    }

    public void setWage(double wage) {
        this.mWage = wage;
    }

    public long getUnpaidTimeWorked() {
        return mUnpaidTimeWorked;
    }

    public void setUnpaidTimeWorked(long unpaidTimeWorked) {
        this.mUnpaidTimeWorked = unpaidTimeWorked;
    }

    public double getUnpaidEarnings() {
        return mUnpaidEarnings;
    }

    public void setUnpaidEarnings(double unpaidEarnings) {
        this.mUnpaidEarnings = unpaidEarnings;
    }

    public long getTotalTimeWorked() {
        return mTotalTimeWorked;
    }

    public void setTotalTimeWorked(long totalTimeWorked) {
        this.mTotalTimeWorked = totalTimeWorked;
    }

    public double getTotalEarnings() {
        return mTotalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.mTotalEarnings = totalEarnings;
    }
}
