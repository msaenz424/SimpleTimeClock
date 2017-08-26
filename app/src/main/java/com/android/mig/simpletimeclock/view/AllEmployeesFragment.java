package com.android.mig.simpletimeclock.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.view.adapters.AllEmployeesAdapter;

public class AllEmployeesFragment extends Fragment implements AllEmployeesView{

    RecyclerView mAllEmployeesRecyclerView;
    View rootView;

    private AllEmployeesAdapter mAllEmployeeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_employees , container, false);

        mAllEmployeesRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_employees_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAllEmployeesRecyclerView.setLayoutManager(layoutManager);
        mAllEmployeesRecyclerView.hasFixedSize();
        mAllEmployeeAdapter = new AllEmployeesAdapter();
        mAllEmployeesRecyclerView.setAdapter(mAllEmployeeAdapter);

        return rootView;
    }

    @Override
    public void showAllEmployees(Cursor employeesCursor) {
        AllEmployeesAdapter allEmployeesAdapter = (AllEmployeesAdapter) mAllEmployeesRecyclerView.getAdapter();
        allEmployeesAdapter.setAllEmployeesData(employeesCursor);
    }
}
