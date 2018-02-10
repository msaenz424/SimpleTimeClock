package com.android.mig.simpletimeclock.source.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Timeclock implements Parcelable {

    private int mTimeId;
    private long mClockIn, mClockOut;
    private int mHoursWorkedInMinutes, mBreaksInMinutes;
    private double mEarned;

    public Timeclock(int timeId, long clockIn, long clockOut, int hoursWorkedInMinutes, int breaksInMinutes, double earned) {

        this.mTimeId = timeId;
        this.mClockIn = clockIn;
        this.mClockOut = clockOut;
        this.mHoursWorkedInMinutes = hoursWorkedInMinutes;
        this.mBreaksInMinutes = breaksInMinutes;
        this.mEarned = earned;
    }

    public Timeclock(int timeId, long clockIn, long clockOut){
        this.mTimeId = timeId;
        this.mClockIn = clockIn;
        this.mClockOut = clockOut;
    }

    protected Timeclock(Parcel in) {
        mTimeId = in.readInt();
        mClockIn = in.readLong();
        mClockOut = in.readLong();
        mHoursWorkedInMinutes = in.readInt();
        mBreaksInMinutes = in.readInt();
        mEarned = in.readDouble();
    }

    public static final Creator<Timeclock> CREATOR = new Creator<Timeclock>() {
        @Override
        public Timeclock createFromParcel(Parcel in) {
            return new Timeclock(in);
        }

        @Override
        public Timeclock[] newArray(int size) {
            return new Timeclock[size];
        }
    };

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

    public int getHoursWorkedInMinutes() {
        return mHoursWorkedInMinutes;
    }

    public void setHoursWorkedInMinutes(int hoursWorkedInMinutes) {
        this.mHoursWorkedInMinutes = hoursWorkedInMinutes;
    }

    public int getBreaksInMinutes() {
        return mBreaksInMinutes;
    }

    public double getEarned() {
        return mEarned;
    }

    public void setEarned(double earned) {
        this.mEarned = mEarned;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mTimeId);
        parcel.writeLong(mClockIn);
        parcel.writeLong(mClockOut);
        parcel.writeInt(mHoursWorkedInMinutes);
        parcel.writeInt(mBreaksInMinutes);
        parcel.writeDouble(mEarned);
    }
}
