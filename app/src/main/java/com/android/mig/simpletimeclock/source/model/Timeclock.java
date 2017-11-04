package com.android.mig.simpletimeclock.source.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Timeclock implements Parcelable {

    private int mTimeId;
    private long mClockIn, mClockOut;
    private int mHoursWorkedInSeconds, mBreaksInSeconds;
    private double mEarned;

    public Timeclock(int timeId, long clockIn, long clockOut, int hoursWorkedInSeconds, int breaksInSeconds, double earned) {

        this.mTimeId = timeId;
        this.mClockIn = clockIn;
        this.mClockOut = clockOut;
        this.mHoursWorkedInSeconds = hoursWorkedInSeconds;
        this.mBreaksInSeconds = breaksInSeconds;
        this.mEarned = earned;
    }

    protected Timeclock(Parcel in) {
        mTimeId = in.readInt();
        mClockIn = in.readLong();
        mClockOut = in.readLong();
        mHoursWorkedInSeconds = in.readInt();
        mBreaksInSeconds = in.readInt();
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

    public int getHoursInSecondsWorked() {
        return mHoursWorkedInSeconds;
    }

    public void setHoursWorked(int hoursWorked) {
        this.mHoursWorkedInSeconds = hoursWorked;
    }

    public int getBreaksInSeconds() {
        return mBreaksInSeconds;
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
        parcel.writeInt(mHoursWorkedInSeconds);
        parcel.writeInt(mBreaksInSeconds);
        parcel.writeDouble(mEarned);
    }
}
