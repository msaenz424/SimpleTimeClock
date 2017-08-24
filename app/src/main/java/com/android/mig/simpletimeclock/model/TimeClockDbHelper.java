package com.android.mig.simpletimeclock.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeClockDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timeclock.db";
    private static final int DATABASE_VERSION = 1;

    public TimeClockDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TimeClockContract.Employees.TABLE_EMPLOYEES + " (" +
                TimeClockContract.Employees.EMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TimeClockContract.Employees.EMP_NAME + " TEXT NOT NULL, " +
                TimeClockContract.Employees.EMP_STATUS + " INTEGER, " +
                TimeClockContract.Employees.EMP_WAGE + " REAL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_EMPLOYEES_TABLE);

        final String SQL_CREATE_TIMECLOCK_TABLE = "CREATE TABLE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " (" +
                TimeClockContract.Timeclock.TIMECLOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " INTEGER, " +
                TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " INTEGER, " +
                TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " INTEGER, " +
                TimeClockContract.Timeclock.TIMECLOCK_BREAK_IN + " INTEGER, " +
                TimeClockContract.Timeclock.TIMECLOCK_BREAK_OUT + " INTEGER, " +
                "FOREIGN KEY(" +  TimeClockContract.Timeclock.TIMECLOCK_EMP_ID  + ") REFERENCES " + TimeClockContract.Employees.TABLE_EMPLOYEES + "(" + TimeClockContract.Employees.EMP_ID + ")" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_TIMECLOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TimeClockContract.Timeclock.TABLE_TIMECLOCK);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TimeClockContract.Employees.TABLE_EMPLOYEES);
        onCreate(sqLiteDatabase);
    }
}
