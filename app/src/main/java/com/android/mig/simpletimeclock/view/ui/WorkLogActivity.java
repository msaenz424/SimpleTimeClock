package com.android.mig.simpletimeclock.view.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.WorkLogPresenter;
import com.android.mig.simpletimeclock.presenter.WorkLogPresenterImpl;
import com.android.mig.simpletimeclock.source.model.Timeclock;
import com.android.mig.simpletimeclock.view.WorkLogView;
import com.android.mig.simpletimeclock.view.fragments.WorkLogFragment;

import java.util.ArrayList;

public class WorkLogActivity extends AppCompatActivity implements WorkLogView {

    private static final int VIEW_UNPAID_WORKLOG = 1;
    private static final int VIEW_DATE_RANGE_WORKLOG = 2;
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    private WorkLogFragment mWorkLogFragment;
    FragmentManager mFragmentManager;
    private WorkLogPresenter mWorkLogPresenter;
    private int mCurrentWorkLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_log);

        mWorkLogPresenter = new WorkLogPresenterImpl(this, this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mWorkLogFragment = (WorkLogFragment) fragmentManager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (mWorkLogFragment == null) {
            mCurrentWorkLogView = getIntent().getIntExtra(Intent.EXTRA_UID, 1);
            ArrayList<Timeclock> timeclockArrayList = getIntent().getParcelableArrayListExtra(Intent.EXTRA_TEXT);
            mWorkLogFragment = new WorkLogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Intent.EXTRA_TEXT, timeclockArrayList);
            mWorkLogFragment.setArguments(bundle);

            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .add(R.id.work_log_container, mWorkLogFragment, TAG_RETAINED_FRAGMENT)
                    .commit();
            mWorkLogFragment.setData(timeclockArrayList);
        }
    }

    @Override
    protected void onResume() {
        /** TODO below empId is placeholder until actual empId is passed to this activity */
        int empId = 1;
        switch (mCurrentWorkLogView){
            case VIEW_UNPAID_WORKLOG :
                mWorkLogPresenter.onResume(empId);
                break;
            case VIEW_DATE_RANGE_WORKLOG :
                /** TODO this case needs to be implemented */
                break;
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(mWorkLogFragment).commit();
        }
    }

    @Override
    public void displayWorkLog(ArrayList<Timeclock> timeClockArrayList) {
        mFragmentManager.beginTransaction()
                .remove(mWorkLogFragment)
                .commit();
        mWorkLogFragment = new WorkLogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Intent.EXTRA_TEXT, timeClockArrayList);
        mWorkLogFragment.setArguments(bundle);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.work_log_container, mWorkLogFragment, TAG_RETAINED_FRAGMENT)
                .commit();
        mWorkLogFragment.setData(timeClockArrayList);
    }
}
