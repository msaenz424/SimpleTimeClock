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

    private static final String INTENT_DATE_START = "date_start";
    private static final String INTENT_DATE_END = "date_end";

    private static final int VIEW_UNPAID_WORKLOG = 1;
    private static final int VIEW_DATE_RANGE_WORKLOG = 2;
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    private WorkLogFragment mWorkLogFragment;
    FragmentManager mFragmentManager;
    private WorkLogPresenter mWorkLogPresenter;
    private int mWorkLogTypeIdentifier;
    private int mEmployeeId;
    private long mDateStart, mDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_log);

        mWorkLogPresenter = new WorkLogPresenterImpl(this, this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mWorkLogFragment = (WorkLogFragment) fragmentManager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (mWorkLogFragment == null) {
            mEmployeeId = getIntent().getIntExtra(Intent.EXTRA_UID, 0);
            mWorkLogTypeIdentifier = getIntent().getIntExtra(Intent.EXTRA_TEXT, 0);
            mDateStart = getIntent().getLongExtra(INTENT_DATE_START, 0);
            mDateEnd = getIntent().getLongExtra(INTENT_DATE_END, 0);
        }
    }

    @Override
    protected void onResume() {
        switch (mWorkLogTypeIdentifier) {
            case VIEW_UNPAID_WORKLOG:
                /** TODO find out why default parameters cannot be used here */
                mWorkLogPresenter.onResume(mEmployeeId, mDateStart, mDateEnd);
                break;
            case VIEW_DATE_RANGE_WORKLOG:
                mWorkLogPresenter.onResume(mEmployeeId, mDateStart, mDateEnd);
                break;
            default:
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
        mWorkLogFragment = new WorkLogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Intent.EXTRA_TEXT, timeClockArrayList);
        mWorkLogFragment.setArguments(bundle);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.work_log_container, mWorkLogFragment, TAG_RETAINED_FRAGMENT)
                .commit();
        mWorkLogFragment.setData(timeClockArrayList);
    }
}
