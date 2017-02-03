package com.android.mig.simpletimeclock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<String>>{

    private static final int LOADER_ID = 99;
    private SQLiteDatabase mDb;
    private RecyclerView mEmployeeRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private TimeclockDbHelper mTimeclockDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimeclockDbHelper = new TimeclockDbHelper(this);
        mEmployeeRecyclerView = (RecyclerView) findViewById(R.id.rv_emp_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeRecyclerView.setLayoutManager(layoutManager);
        mEmployeeRecyclerView.hasFixedSize();
        mEmployeeAdapter = new EmployeeAdapter();
        mEmployeeRecyclerView.setAdapter(mEmployeeAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void addEmployee(View view){
        ContentValues contentValues = new ContentValues();
        String name = "new emp";    // test data
        contentValues.put(TimeclockContract.Employees.EMP_NAME, name);
        boolean insert = DbUtils.insertEmployee(mTimeclockDbHelper, contentValues);
        if (!insert)
            Toast.makeText(this, R.string.error_text_insert, Toast.LENGTH_SHORT).show();
        else
            mEmployeeAdapter.addNewEmployeeToArrayList(name);
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<String>>(this) {

            ArrayList<String> mEmployeesArrayList;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<String> loadInBackground() {
                ArrayList<String> employeesArrayList = new ArrayList<>();
                Cursor cursor = DbUtils.readEmployees(mTimeclockDbHelper);
                if (cursor != null){
                    cursor.moveToFirst();
                    do{
                        employeesArrayList.add(cursor.getString(1));
                    }while (cursor.moveToNext());
                }

                return employeesArrayList;
            }

            @Override
            public void deliverResult(ArrayList<String> data) {
                super.deliverResult(data);
                mEmployeesArrayList = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
        mEmployeeAdapter.setEmployeesData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }
}