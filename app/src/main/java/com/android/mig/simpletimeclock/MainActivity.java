package com.android.mig.simpletimeclock;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    private RecyclerView mEmployeeRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeclockDbHelper timeclockDbHelper = new TimeclockDbHelper(this);
        mDb = timeclockDbHelper.getWritableDatabase();
        mEmployeeRecyclerView = (RecyclerView) findViewById(R.id.rv_emp_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeRecyclerView.setLayoutManager(layoutManager);
        mEmployeeRecyclerView.hasFixedSize();
        mEmployeeAdapter = new EmployeeAdapter();
        mEmployeeRecyclerView.setAdapter(mEmployeeAdapter);

    }
}