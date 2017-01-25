package com.android.mig.simpletimeclock;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeclockDbHelper timeclockDbHelper = new TimeclockDbHelper(this);
        mDb = timeclockDbHelper.getWritableDatabase();
    }
}
