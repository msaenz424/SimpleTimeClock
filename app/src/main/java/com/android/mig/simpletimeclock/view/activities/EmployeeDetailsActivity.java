package com.android.mig.simpletimeclock.view.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.view.fragments.EmployeeDetailsFragment;

public class EmployeeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        EmployeeDetailsFragment employeeDetailsFragment = new EmployeeDetailsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.employee_detail_container, employeeDetailsFragment)
                .commit();
    }
}
