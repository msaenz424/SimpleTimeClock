package com.android.mig.simpletimeclock.view.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.AllEmployeesPresenter;
import com.android.mig.simpletimeclock.presenter.AllEmployeesPresenterImpl;
import com.android.mig.simpletimeclock.view.AllEmployeesView;
import com.android.mig.simpletimeclock.view.adapters.AllEmployeesAdapter;

public class AllEmployeesFragment extends Fragment
        implements AllEmployeesView, AllEmployeesAdapter.OnListTapHandler{

    boolean actionMode = false;
    TextView mCounterTextView;
    AllEmployeesPresenter mAllEmployeesPresenter;
    FloatingActionButton mFabSetActiveEmployee;
    RecyclerView mAllEmployeesRecyclerView;
    View rootView;
    ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = true;
            mActionMode = mode;
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_all_employees_action_mode, menu);
            showFabDoneButton();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.menu_item_delete){
                AllEmployeesAdapter allEmployeesAdapter = (AllEmployeesAdapter) mAllEmployeesRecyclerView.getAdapter();
                mAllEmployeesPresenter.onActionDeleteClicked(allEmployeesAdapter.getEmployeesIds());
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            resetScreen();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_employees , container, false);

        mCounterTextView = rootView.findViewById(R.id.counter_text_view);
        mCounterTextView.setVisibility(View.GONE);

        mFabSetActiveEmployee = rootView.findViewById(R.id.fab_set_active_employee);
        mFabSetActiveEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllEmployeesAdapter allEmployeesAdapter = (AllEmployeesAdapter) mAllEmployeesRecyclerView.getAdapter();
                mAllEmployeesPresenter.onActionDoneClicked(allEmployeesAdapter.getEmployeesIds());
            }
        });

        mAllEmployeesRecyclerView = rootView.findViewById(R.id.all_employees_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAllEmployeesRecyclerView.setLayoutManager(layoutManager);
        mAllEmployeesRecyclerView.hasFixedSize();
        AllEmployeesAdapter mAllEmployeeAdapter = new AllEmployeesAdapter(this);
        mAllEmployeesRecyclerView.setAdapter(mAllEmployeeAdapter);

        mAllEmployeesPresenter = new AllEmployeesPresenterImpl(this, getActivity());
        mAllEmployeesPresenter.onResume();

        return rootView;
    }

    /** {@inheritDoc} */
    @Override
    public void showAllEmployees(Cursor employeesCursor) {
        AllEmployeesAdapter allEmployeesAdapter = (AllEmployeesAdapter) mAllEmployeesRecyclerView.getAdapter();
        allEmployeesAdapter.setAllEmployeesData(employeesCursor);
    }

    /** {@inheritDoc} */
    @Override
    public void showStatusUpdateMessage() {
        Snackbar snackbar = Snackbar.make(
                rootView,
                R.string.status_update_message,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /** {@inheritDoc} */
    @Override
    public void showSuccessDeleteMessage() {
        Snackbar snackbar = Snackbar.make(
                rootView,
                R.string.success_delete_message,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /** {@inheritDoc} */
    @Override
    public void showPartialDeleteMessage() {
        Snackbar snackbar = Snackbar.make(
                rootView,
                R.string.partial_delete_message,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /** {@inheritDoc} */
    @Override
    public void showFailedDeleteMessage() {
        Snackbar snackbar = Snackbar.make(
                rootView,
                R.string.failed_delete_message,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /** {@inheritDoc} */
    @Override
    public void showFabDoneButton() {
        mFabSetActiveEmployee.setVisibility(View.VISIBLE);
    }

    /** {@inheritDoc} */
    @Override
    public void hideFabDoneButton() {
        mFabSetActiveEmployee.setVisibility(View.GONE);
    }

    /** {@inheritDoc} */
    @Override
    public void resetScreen() {
        if (mActionMode != null){
            mActionMode.finish();
        }
        actionMode = false;
        mActionMode = null;
        AllEmployeesAdapter allEmployeesAdapter = (AllEmployeesAdapter) mAllEmployeesRecyclerView.getAdapter();
        allEmployeesAdapter.clearSelection();
        hideFabDoneButton();
        mAllEmployeesPresenter.onResume();
    }

    /**
     * Method to be called in parent activity to pass the values of
     * the new employee that was added.
     *
     * @param name name of employee
     * @param wage wage of employee
     */
    public void setNewEmployeeData(String name, double wage){
        mAllEmployeesPresenter.onActionAddClicked(name, wage);
    }

    /** {@inheritDoc} */
    @Override
    public void onItemLongTap() {
        if (!actionMode){
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.startSupportActionMode(mActionModeCallbacks);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onLastSelectionItemRemoved() {
        resetScreen();
    }
}
