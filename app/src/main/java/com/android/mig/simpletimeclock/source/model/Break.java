package com.android.mig.simpletimeclock.source.model;

public class Break {

    private int mBreakID;
    private long mBreakStart;
    private long mBreakEnd;

    public Break(int breakID, long breakStart, long breakEnd) {
        this.mBreakID = breakID;
        this.mBreakStart = breakStart;
        this.mBreakEnd = breakEnd;
    }

    public int getBreakID() {
        return mBreakID;
    }

    public long getBreakStart() {
        return mBreakStart;
    }

    public long getBreakEnd() {
        return mBreakEnd;
    }

    public void setBreakStart(long breakStart) {
        this.mBreakStart = breakStart;
    }

    public void setBreakEnd(long breakEnd) {
        this.mBreakEnd = breakEnd;
    }
}
