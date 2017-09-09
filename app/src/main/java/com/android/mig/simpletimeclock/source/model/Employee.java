package com.android.mig.simpletimeclock.source.model;

public class Employee {
    int mID;
    String mName;
    double mWage;

    public Employee(int id, String name){
        mID = id;
        mName = name;
    }

    public void setEmployeeID(int id){ mID = id; }
    public void setEmployeeName(String name){ mName = name; }
    public void setEmployeeWage(double wage){ mWage = wage; }

    public int getEmployeeID(){ return mID; }
    public String getEmployeeName(){ return mName; }
    public double getEmployeeWage(){ return mWage; }
}
