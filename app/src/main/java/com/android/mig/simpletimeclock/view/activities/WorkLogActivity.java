package com.android.mig.simpletimeclock.view.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.source.model.Timeclock;
import com.android.mig.simpletimeclock.view.fragments.EmployeeDetailsFragment;
import com.android.mig.simpletimeclock.view.fragments.WorkLogFragment;

import java.util.ArrayList;

public class WorkLogActivity extends AppCompatActivity {

    WorkLogFragment mWorkLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_log);

        ArrayList<Timeclock> timeclockArrayList = getIntent().getParcelableArrayListExtra(Intent.EXTRA_TEXT);
        mWorkLogFragment = new WorkLogFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Intent.EXTRA_TEXT, timeclockArrayList);
        mWorkLogFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.work_log_container, mWorkLogFragment)
                .commit();

    }
}
