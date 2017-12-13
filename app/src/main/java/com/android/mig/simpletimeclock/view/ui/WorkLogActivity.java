package com.android.mig.simpletimeclock.view.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.source.model.Timeclock;
import com.android.mig.simpletimeclock.view.fragments.WorkLogFragment;

import java.util.ArrayList;

public class WorkLogActivity extends AppCompatActivity {

    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    private WorkLogFragment mWorkLogFragment;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_log);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mWorkLogFragment = (WorkLogFragment) fragmentManager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (mWorkLogFragment == null) {
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
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(mWorkLogFragment).commit();
        }
    }
}
