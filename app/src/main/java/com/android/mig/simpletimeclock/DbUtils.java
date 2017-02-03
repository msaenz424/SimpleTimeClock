package com.android.mig.simpletimeclock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mig.simpletimeclock.TimeclockContract.Employees;

public class DbUtils {

    public static boolean insertEmployee(TimeclockDbHelper timeClockDbHelpter, ContentValues contentValues){
        final SQLiteDatabase db = timeClockDbHelpter.getWritableDatabase();
        long id = db.insert(Employees.TABLE_EMPLOYEES, null, contentValues);
        if (id == -1)
            return false;
        return true;

    }

    public static Cursor readEmployees(TimeclockDbHelper timeclockDbHelper){
        final SQLiteDatabase db = timeclockDbHelper.getReadableDatabase();
        Cursor cursor = db.query(Employees.TABLE_EMPLOYEES, null, null, null, null, null, null);
        if (cursor.getCount() > 0)
            return cursor;
        return null;
    }
}
