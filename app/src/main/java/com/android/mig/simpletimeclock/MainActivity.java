package com.android.mig.simpletimeclock;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Employee>>{
    private int c =0;
    private static final int LOADER_ID = 99;
    private RecyclerView mEmployeeRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private TimeClockDbHelper mTimeClockDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimeClockDbHelper = new TimeClockDbHelper(this);
        mEmployeeRecyclerView = (RecyclerView) findViewById(R.id.rv_emp_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeRecyclerView.setLayoutManager(layoutManager);
        mEmployeeRecyclerView.hasFixedSize();
        mEmployeeAdapter = new EmployeeAdapter();
        mEmployeeRecyclerView.setAdapter(mEmployeeAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int empID = (int) viewHolder.itemView.getTag();
                if (direction == ItemTouchHelper.LEFT){
                    DbUtils.deleteEmployee(mTimeClockDbHelper, empID);
                    mEmployeeAdapter.deleteEmployee(viewHolder.getAdapterPosition());
                }

            }
        }).attachToRecyclerView(mEmployeeRecyclerView);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void addEmployee(View view){
        ContentValues contentValues = new ContentValues();
        String name = "new emp " + String.valueOf(c++);    // test data
        contentValues.put(TimeClockContract.Employees.EMP_NAME, name);
        int newID = DbUtils.insertEmployee(mTimeClockDbHelper, contentValues);
        if (newID > 0)
            mEmployeeAdapter.addNewEmployeeToArrayList(newID, name);
        else
            Toast.makeText(this, R.string.error_text_insert, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<ArrayList<Employee>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Employee>>(this) {

            ArrayList<Employee> mEmployeesArrayList;

            @Override
            protected void onStartLoading() {
                if (mEmployeesArrayList == null)
                    forceLoad();
                else
                    deliverResult(mEmployeesArrayList);
            }

            @Override
            public ArrayList<Employee> loadInBackground() {
                ArrayList<Employee> employeesArrayList = new ArrayList<>();

                Cursor cursor = DbUtils.readEmployees(mTimeClockDbHelper);
                if (cursor != null){
                    cursor.moveToFirst();
                    do{
                        employeesArrayList.add(new Employee(cursor.getInt(0),cursor.getString(1)));
                    }while (cursor.moveToNext());
                }

                return employeesArrayList;
            }

            @Override
            public void deliverResult(ArrayList<Employee> data) {
                super.deliverResult(data);
                mEmployeesArrayList = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Employee>> loader, ArrayList<Employee> data) {
        if (data != null)
            mEmployeeAdapter.setEmployeesData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Employee>> loader) {

    }
}