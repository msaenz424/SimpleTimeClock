package com.android.mig.simpletimeclock.view.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.mig.simpletimeclock.R
import com.android.mig.simpletimeclock.source.model.Timeclock
import com.android.mig.simpletimeclock.view.WorkLogDetailsView
import kotlinx.android.synthetic.main.activity_work_log_details.*
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class WorkLogDetailsActivity : AppCompatActivity(), WorkLogDetailsView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_log_details)
        //actionBar.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.getBundleExtra(Intent.EXTRA_INTENT)
        val timeclock = bundle.getParcelable(Intent.EXTRA_TEXT) as Timeclock

        Log.d("worklogdetails", timeclock.timeId.toString())

        displayWorkLogDetails(timeclock)

    }

    override fun displayWorkLogDetails(timeclock: Timeclock) {
        worklog_detail_clocked_in.text = formatTime(timeclock.clockIn * 1000L)
        worklog_detail_clocked_out.text = formatTime(timeclock.clockOut * 1000L)
    }

    override fun displayCorrectionSuccessMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayCorrectionFailMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun formatTime(time: Long): String{
        val date = Date(time)
        val formatter = SimpleDateFormat("EEE, MMM dd, h:mm a", Locale.US)
        return formatter.format(date).toString()
    }

}