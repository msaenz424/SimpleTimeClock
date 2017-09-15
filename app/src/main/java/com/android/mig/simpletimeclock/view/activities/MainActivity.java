package com.android.mig.simpletimeclock.view.activities;

import android.content.Intent;
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

import com.android.mig.simpletimeclock.source.model.EmployeesInteractorImpl;
import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenter;
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenterImpl;
import com.android.mig.simpletimeclock.view.MainView;
import com.android.mig.simpletimeclock.view.adapters.EmployeeAdapter;

public class MainActivity extends AppCompatActivity
        implements MainView,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 99;
    private RecyclerView mEmployeeRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private TimeClockDbHelper mTimeClockDbHelper;
    private ActiveEmployeesPresenter mActiveEmployeesPresenter;

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
                /*
                int empID = (int) viewHolder.itemView.getTag();
                if (direction == ItemTouchHelper.LEFT){
                    EmployeesInteractorImpl.deleteEmployee(mTimeClockDbHelper, empID);
                    mEmployeeAdapter.deleteEmployee(viewHolder.getAdapterPosition());
                }
                */
            }
        }).attachToRecyclerView(mEmployeeRecyclerView);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void addEmployee(View view){
        Intent intent = new Intent(this, AllEmployeesActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mEmployeesCursor;

            @Override
            protected void onStartLoading() {
                if (mEmployeesCursor == null)
                    forceLoad();
                else
                    deliverResult(mEmployeesCursor);
            }

            @Override
            public Cursor loadInBackground() {
                mActiveEmployeesPresenter = new ActiveEmployeesPresenterImpl(MainActivity.this);
                Cursor cursor = mActiveEmployeesPresenter.getActiveEmployees();

                return cursor;
            }

            @Override
            public void deliverResult(Cursor data) {
                super.deliverResult(data);
                mEmployeesCursor = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null)
            mActiveEmployeesPresenter.showActiveEmployees(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void showActiveEmployees(Cursor employees) {
        mEmployeeAdapter.setEmployeesData(employees);
    }
}