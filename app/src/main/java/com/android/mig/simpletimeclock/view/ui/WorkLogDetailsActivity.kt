package com.android.mig.simpletimeclock.view.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.mig.simpletimeclock.R

class WorkLogDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_log_details)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}
