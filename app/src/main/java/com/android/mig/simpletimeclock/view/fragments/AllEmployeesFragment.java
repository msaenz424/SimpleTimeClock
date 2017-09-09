package com.android.mig.simpletimeclock.view.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.view.AllEmployeesView;
import com.android.mig.simpletimeclock.view.adapters.AllEmployeesAdapter;

public class AllEmployeesFragment extends Fragment
        implements AllEmployeesView{

    FloatingActionButton mAddEmployeeFab;
    RecyclerView mAllEmployeesRecyclerView;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_employees , container, false);

        mAddEmployeeFab = (FloatingActionButton) rootView.findViewById(R.id.fab_add_employee);
        mAddEmployeeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                DialogFragment dialogFragment = new AddEmployeeDialogFragment();
                dialogFragment.show(fragmentManager, "title");
            }
        });

        mAllEmployeesRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_employees_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAllEmployeesRecyclerView.setLayoutManager(layoutManager);
        mAllEmployeesRecyclerView.hasFixedSize();
        AllEmployeesAdapter mAllEmployeeAdapter = new AllEmployeesAdapter();
        mAllEmployeesRecyclerView.setAdapter(mAllEmployeeAdapter);

        return rootView;
    }

    @Override
    public void showAllEmployees(Cursor employeesCursor) {
        AllEmployeesAdapter allEmployeesAdapter = (AllEmployeesAdapter) mAllEmployeesRecyclerView.getAdapter();
        allEmployeesAdapter.setAllEmployeesData(employeesCursor);
    }

    /**
     * Method to be called in parent activity to pass the values of
     * the new employee that was added.
     *
     * @param name name of employee
     * @param wage wage of employee
     */
    public void setNewEmployeeData(String name, double wage){
        Log.d("passed" , name + " " + wage);
    }
}
