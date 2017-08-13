package com.android.mig.simpletimeclock;


import android.provider.BaseColumns;

public class TimeClockContract {

    public static final class Employees implements BaseColumns{
        public static final String TABLE_EMPLOYEES = "employees";
        public static final String EMP_ID = "emp_id";
        public static final String EMP_NAME = "emp_name";
        public static final String EMP_STATUS = "emp_status";
        public static final String EMP_WAGE = "emp_wage";
    }

    public static final class Timeclock implements BaseColumns{
        public static final String TABLE_TIMECLOCK = "timeclock";
        public static final String TIMECLOCK_ID = "timeclock_id";
        public static final String TIMECLOCK_EMP_ID = "emp_id";
        public static final String TIMECLOCK_CLOCK_IN = "clock_in";
        public static final String TIMECLOCK_CLOCK_OUT = "clock_out";
        public static final String TIMECLOCK_BREAK_IN = "break_in";
        public static final String TIMECLOCK_BREAK_OUT = "break_out";
    }
}
