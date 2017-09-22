package com.android.mig.simpletimeclock.view.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenter;
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenterImpl;
import com.android.mig.simpletimeclock.view.MainView;
import com.android.mig.simpletimeclock.view.adapters.EmployeeAdapter;

public class MainActivity extends AppCompatActivity
        implements MainView, EmployeeAdapter.OnClickHandler {

    private RecyclerView mEmployeeRecyclerView;
    private ActiveEmployeesPresenter mActiveEmployeesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmployeeRecyclerView = (RecyclerView) findViewById(R.id.rv_emp_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeRecyclerView.setLayoutManager(layoutManager);
        mEmployeeRecyclerView.hasFixedSize();
        EmployeeAdapter mEmployeeAdapter = new EmployeeAdapter(this);
        mEmployeeRecyclerView.setAdapter(mEmployeeAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Integer[] ids;
                int position = viewHolder.getAdapterPosition();
                EmployeeAdapter employeeAdapter = (EmployeeAdapter) mEmployeeRecyclerView.getAdapter();
                ids = employeeAdapter.getItemIds(position);
                mActiveEmployeesPresenter.onItemSwiped(ids);
            }
        }).attachToRecyclerView(mEmployeeRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActiveEmployeesPresenter = new ActiveEmployeesPresenterImpl(this, this);
        mActiveEmployeesPresenter.onResume();
    }

    public void addEmployee(View view){
        Intent intent = new Intent(this, AllEmployeesActivity.class);
        startActivity(intent);
    }

    @Override
    public void showActiveEmployees(Cursor employees) {
        EmployeeAdapter employeeAdapter = (EmployeeAdapter) mEmployeeRecyclerView.getAdapter();
        employeeAdapter.setEmployeesData(employees);
    }

    @Override
    public void onItemClick(int employeeId) {
        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_UID, employeeId);
        startActivity(intent);
    }
}