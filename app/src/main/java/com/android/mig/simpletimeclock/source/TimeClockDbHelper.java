package com.android.mig.simpletimeclock.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimeClockDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timeclock.db";
    private static final int DATABASE_VERSION = 2;

    // Employees table (introduced in version 1)
    private static final String SQL_CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TimeClockContract.Employees.TABLE_EMPLOYEES + " (" +
            TimeClockContract.Employees.EMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TimeClockContract.Employees.EMP_NAME + " TEXT NOT NULL, " +
            TimeClockContract.Employees.EMP_WAGE + " REAL, " +
            TimeClockContract.Employees.EMP_PHOTO_PATH + " TEXT DEFAULT NULL" +
            "); ";
/*
    // Deprecated Timeclock table (introduced version 1)
    private static final String SQL_CREATE_TIMECLOCK_TABLE = "CREATE TABLE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " (" +
            TimeClockContract.Timeclock.TIMECLOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " INTEGER, " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " INTEGER, " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " INTEGER, " +
            TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + " INTEGER DEFAULT 0, " +
            TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + " INTEGER DEFAULT 0, " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE + " REAL, " +
            TimeClockContract.Timeclock.TIMECLOCK_PAID + " INTEGER, " +
            "FOREIGN KEY(" +  TimeClockContract.Timeclock.TIMECLOCK_EMP_ID  + ") REFERENCES " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + "(" + TimeClockContract.Employees.EMP_ID + ")" +
            ");";
*/

    // Upgraded Timeclock table (introduced in version 2)
    private static final String SQL_CREATE_TIMECLOCK_TABLE = "CREATE TABLE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " (" +
            TimeClockContract.Timeclock.TIMECLOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + " INTEGER, " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " INTEGER, " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " INTEGER, " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE + " REAL, " +
            TimeClockContract.Timeclock.TIMECLOCK_PAID + " INTEGER, " +
            "FOREIGN KEY(" +  TimeClockContract.Timeclock.TIMECLOCK_EMP_ID  + ") REFERENCES " +
            TimeClockContract.Employees.TABLE_EMPLOYEES + "(" + TimeClockContract.Employees.EMP_ID + ")" +
            ");";

    // New table Break introduced in version 2
    private static final String SQL_CREATE_BREAK_TABLE = "CREATE TABLE " + TimeClockContract.Breaks.TABLE_BREAKS + " (" +
            TimeClockContract.Breaks.BREAK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + " INTEGER, " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_START + " INTEGER DEFAULT 0, " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " INTEGER DEFAULT 0, " +
            "FOREIGN KEY(" + TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + ") REFERENCES " +
            TimeClockContract.Timeclock.TABLE_TIMECLOCK + "(" + TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + ")" +
            ");";

    public TimeClockDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_EMPLOYEES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TIMECLOCK_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_BREAK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // introduced in database version 2
        final String PRAGMA_OFF = "PRAGMA foreign_keys=off;";
        final String PRAGMA_ON = "PRAGMA foreign_keys=on;";
        final String BEGIN_TRANSACTION = "BEGIN TRANSACTION;";
        final String COMMIT = "COMMIT;";
        final String ALTER_TIMECLOCK_1 = "ALTER TABLE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " RENAME TO _timeclock_old;";
        final String INSERT_INTO_TIMECLOCK_1 = "INSERT INTO " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " (" +
                TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_WAGE + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_PAID + ") SELECT " +
                TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_WAGE + ", " +
                TimeClockContract.Timeclock.TIMECLOCK_PAID + " FROM _timeclock_old;";

        switch (oldVersion){
            case 1:
                sqLiteDatabase.beginTransaction();
                try {
                    sqLiteDatabase.execSQL(PRAGMA_OFF);
                    sqLiteDatabase.execSQL(BEGIN_TRANSACTION);
                    sqLiteDatabase.execSQL(ALTER_TIMECLOCK_1);
                    sqLiteDatabase.execSQL(SQL_CREATE_TIMECLOCK_TABLE);
                    sqLiteDatabase.execSQL(INSERT_INTO_TIMECLOCK_1);
                    sqLiteDatabase.execSQL(COMMIT);
                    sqLiteDatabase.execSQL(PRAGMA_ON);
                    sqLiteDatabase.setTransactionSuccessful();
                    Log.d("onUpgrade", "transaction success");
                } finally {
                    sqLiteDatabase.endTransaction();
                }

                sqLiteDatabase.execSQL(SQL_CREATE_BREAK_TABLE);
        }

        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TimeClockContract.Timeclock.TABLE_TIMECLOCK);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TimeClockContract.Employees.TABLE_EMPLOYEES);
        //onCreate(sqLiteDatabase);
    }
}
