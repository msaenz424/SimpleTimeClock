package com.android.mig.simpletimeclock;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.android.mig.simpletimeclock.TimeclockContract.*;

public class TimeclockDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timeclock.db";
    private static final int DATABASE_VERSION = 1;

    public TimeclockDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + Employees.TABLE_EMPLOYEES + " (" +
                Employees.EMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Employees.EMP_NAME + " TEXT NOT NULL, " +
                Employees.EMP_STATUS + " INTEGER, " +
                Employees.EMP_WAGE + " REAL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_EMPLOYEES_TABLE);

        final String SQL_CREATE_TIMECLOCK_TABLE = "CREATE TABLE " + Timeclock.TABLE_TIMECLOCK + " (" +
                Timeclock.TIMECLOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Timeclock.TIMECLOCK_EMP_ID + " INTEGER, " +
                Timeclock.TIMECLOCK_CLOCK_IN + " INTEGER, " +
                Timeclock.TIMECLOCK_CLOCK_OUT + " INTEGER, " +
                Timeclock.TIMECLOCK_BREAK_IN + " INTEGER, " +
                Timeclock.TIMECLOCK_BREAK_OUT + " INTEGER, " +
                "FOREIGN KEY(" +  Timeclock.TIMECLOCK_EMP_ID  + ") REFERENCES " + Employees.TABLE_EMPLOYEES + "(" + Employees.EMP_ID + ")" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_TIMECLOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Timeclock.TABLE_TIMECLOCK);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Employees.TABLE_EMPLOYEES);
        onCreate(sqLiteDatabase);
    }
}
