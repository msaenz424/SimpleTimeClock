package com.android.mig.simpletimeclock.view.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.android.mig.simpletimeclock.R
import com.android.mig.simpletimeclock.presenter.WorkLogDetailsPresenter
import com.android.mig.simpletimeclock.presenter.WorkLogDetailsPresenterImpl
import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.Timeclock
import com.android.mig.simpletimeclock.view.WorkLogDetailsView
import com.android.mig.simpletimeclock.view.adapters.BreaksAdapter
import kotlinx.android.synthetic.main.activity_work_log_details.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkLogDetailsActivity : AppCompatActivity(), WorkLogDetailsView {

    private lateinit var mWorkLogDetailsPresenter: WorkLogDetailsPresenter
    private lateinit var mTimeClock: Timeclock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_log_details)
        val toolbar = findViewById<Toolbar>(R.id.worklog_deatils_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.worklog_details_activity_title)
        toolbar.setNavigationOnClickListener {onBackPressed() }

        val bundle = intent.getBundleExtra(Intent.EXTRA_INTENT)
        mTimeClock = bundle.getParcelable(Intent.EXTRA_TEXT) as Timeclock

        mWorkLogDetailsPresenter = WorkLogDetailsPresenterImpl(this, this)
        mWorkLogDetailsPresenter.onCreate(mTimeClock.timeId)
    }

    override fun displayWorkLogDetails(timeclock: Timeclock) {
        worklog_detail_clocked_in.text = formatTime(timeclock.clockIn * 1000L)
        worklog_detail_clocked_out.text = formatTime(timeclock.clockOut * 1000L)
    }

    override fun displayWorkLogBreaks(breaksArrayList: ArrayList<Break>) {
        val layoutManager = LinearLayoutManager(this)
        val breaksAdapter = BreaksAdapter(breaksArrayList)
        breaks_recycler_view.layoutManager = layoutManager
        breaks_recycler_view.adapter = breaksAdapter
        displayWorkLogDetails(mTimeClock)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_worklog_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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