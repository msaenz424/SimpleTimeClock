package com.android.mig.simpletimeclock.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mig.simpletimeclock.model.TimeClockContract.Employees;
import com.android.mig.simpletimeclock.model.TimeClockDbHelper;

public class DbUtils {

    public static int insertEmployee(TimeClockDbHelper timeClockDbHelpter, ContentValues contentValues){
        final SQLiteDatabase db = timeClockDbHelpter.getWritableDatabase();
        long id = db.insert(Employees.TABLE_EMPLOYEES, null, contentValues);
        return (int)id;
    }

    public static Cursor readEmployees(TimeClockDbHelper timeClockDbHelper){
        final SQLiteDatabase db = timeClockDbHelper.getReadableDatabase();
        Cursor cursor = db.query(Employees.TABLE_EMPLOYEES, null, null, null, null, null, null);
        if (cursor.getCount() > 0)
            return cursor;
        return null;
    }

    public static boolean deleteEmployee(TimeClockDbHelper timeClockDbHelper, int id){
        final SQLiteDatabase db = timeClockDbHelper.getWritableDatabase();
        int deleted = db.delete(Employees.TABLE_EMPLOYEES, Employees.EMP_ID + "=" + id, null);
        if (deleted > 0)
            return true;
        return false;
    }
}
