package com.android.mig.simpletimeclock.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mig.simpletimeclock.model.TimeClockContract.Employees;

public class EmployeesInteractorImpl implements EmployeesInteractor{

    private static int ACTIVE_STATUS = 1;
    private static int INACTIVE_STATUS = 0;

    private Context mContext;

    public EmployeesInteractorImpl(Context context) {
        this.mContext = context;
    }

    public int insertEmployee(String name, double wage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Employees.EMP_NAME, name);
        contentValues.put(Employees.EMP_STATUS, INACTIVE_STATUS);
        contentValues.put(Employees.EMP_WAGE, wage);

        TimeClockDbHelper timeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = timeClockDbHelper.getWritableDatabase();
        long id = db.insert(Employees.TABLE_EMPLOYEES, null, contentValues);
        return (int)id;
    }

    public Cursor readEmployees(){
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();
        Cursor cursor = db.query(Employees.TABLE_EMPLOYEES, null, null, null, null, null, null);
        if (cursor.getCount() > 0)
            return cursor;
        return null;
    }

    public Cursor readActiveEmployees(){
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getReadableDatabase();

        String returnColumns[] = {Employees.EMP_ID, Employees.EMP_NAME};
        String where = Employees.EMP_WAGE + " = " + ACTIVE_STATUS;

        Cursor cursor = db.query(Employees.TABLE_EMPLOYEES, returnColumns, where, null, null, null, null);
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
