package com.android.mig.simpletimeclock.view.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenter;
import com.android.mig.simpletimeclock.presenter.ActiveEmployeesPresenterImpl;
import com.android.mig.simpletimeclock.view.MainView;
import com.android.mig.simpletimeclock.view.adapters.EmployeeAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity
        implements MainView, EmployeeAdapter.OnClickHandler {

    private static final int CLOCK_OUT_CODE = 0;
    private static final int BREAK_START_CODE = 1;
    private static final int BREAK_END_CODE = 2;

    private RecyclerView mEmployeeRecyclerView;
    private TextView mListMessageTextView;
    private AdView mAdView;
    private ActiveEmployeesPresenter mActiveEmployeesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.main_activity_title));
        }

        mAdView = (AdView) findViewById(R.id.adView);
        // Requests an ad to the AdMob platform
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mListMessageTextView = (TextView) findViewById(R.id.active_list_text_view);
        mEmployeeRecyclerView = (RecyclerView) findViewById(R.id.rv_emp_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeRecyclerView.setLayoutManager(layoutManager);
        mEmployeeRecyclerView.hasFixedSize();
        EmployeeAdapter mEmployeeAdapter = new EmployeeAdapter(this);
        mEmployeeRecyclerView.setAdapter(mEmployeeAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                EmployeeAdapter employeeAdapter = (EmployeeAdapter) mEmployeeRecyclerView.getAdapter();
                int timeId = employeeAdapter.getTimeId(position);
                mActiveEmployeesPresenter.onItemSwiped(timeId, CLOCK_OUT_CODE);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View foregroundView = ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mForegroundLayout;
                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX > 0){
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mLeftClockOutIcon.setVisibility(View.VISIBLE);
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mLeftClockOutTextView.setVisibility(View.VISIBLE);
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mRightClockOutIcon.setVisibility(View.INVISIBLE);
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mRightClockOutTextView.setVisibility(View.INVISIBLE);
                    } else {
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mRightClockOutIcon.setVisibility(View.VISIBLE);
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mRightClockOutTextView.setVisibility(View.VISIBLE);
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mLeftClockOutIcon.setVisibility(View.INVISIBLE);
                        ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mLeftClockOutTextView.setVisibility(View.INVISIBLE);
                    }
                    final View foregroundView = ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mForegroundLayout;
                    getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
                }

            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mForegroundLayout;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((EmployeeAdapter.EmployeeViewHolder) viewHolder).mForegroundLayout;
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
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
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void showActiveEmployees(Cursor employees) {
        if (employees.getCount() == 0) {
            mListMessageTextView.setVisibility(View.VISIBLE);
        } else {
            mListMessageTextView.setVisibility(View.INVISIBLE);
        }
        EmployeeAdapter employeeAdapter = (EmployeeAdapter) mEmployeeRecyclerView.getAdapter();
        employeeAdapter.setEmployeesData(employees);
    }

    @Override
    public void onItemClick(int employeeId, View photoImageView) {
        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions
                    .makeSceneTransitionAnimation(this, photoImageView, photoImageView.getTransitionName())
                    .toBundle();
        }
        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_UID, employeeId);
        startActivity(intent, bundle);
    }

    @Override
    public boolean onItemTimerClick(int employeeId, int actionCode) {
        if (actionCode == BREAK_START_CODE || actionCode == BREAK_END_CODE){
            Log.d("break", String.valueOf(actionCode));
            mActiveEmployeesPresenter.onItemTimerClicked(employeeId, actionCode);
            return true;
        } else {
            return false;
        }
    }
}