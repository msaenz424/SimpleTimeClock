package com.android.mig.simpletimeclock.view.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.android.mig.simpletimeclock.R
import com.android.mig.simpletimeclock.presenter.WorkLogDetailsPresenter
import com.android.mig.simpletimeclock.presenter.WorkLogDetailsPresenterImpl
import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.Timeclock
import com.android.mig.simpletimeclock.view.WorkLogDetailsView
import com.android.mig.simpletimeclock.view.adapters.BreaksAdapter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_work_log_details.*
import org.jetbrains.anko.design.snackbar
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkLogDetailsActivity : AppCompatActivity(),
        WorkLogDetailsView,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        BreaksAdapter.OnClickHandler {

    private val CLOCK_IN_DATE_PICKER_TAG = "ClockInDatePicker"
    private val CLOCK_OUT_DATE_PICKER_TAG = "ClockOutDatePicker"
    private val CLOCK_IN_TIME_PICKER_TAG = "ClockInTimePicker"
    private val CLOCK_OUT_TIME_PICKER_TAG = "ClockOutTimePicker"
    private val BREAK_START_DATE_PICKER_TAG = "BreakStartDatePicker"
    private val BREAK_END_DATE_PICKER_TAG = "BreakEndDatePicker"
    private val BREAK_START_TIME_PICKER_TAG = "BreakStartTimePicker"
    private val BREAK_END_TIME_PICKER_TAG = "BreakEndTimePicker"

    private lateinit var mWorkLogDetailsPresenter: WorkLogDetailsPresenter
    private lateinit var mTimeClock: Timeclock
    private lateinit var mBreaksAdapter: BreaksAdapter
    private lateinit var mPickerTag: String
    private lateinit var mClockedInDate: String
    private lateinit var mClockedOutDate: String
    private lateinit var mBreakStartDate: String
    private lateinit var mBreakEndDate: String
    private var mAdapterPosition: Int = 0
    private var mClockedInDay: Int = 0
    private var mClockedOutDay: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_log_details)
        val toolbar = findViewById<Toolbar>(R.id.worklog_deatils_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.worklog_details_activity_title)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val bundle = intent.getBundleExtra(Intent.EXTRA_INTENT)
        mTimeClock = bundle.getParcelable(Intent.EXTRA_TEXT) as Timeclock

        mWorkLogDetailsPresenter = WorkLogDetailsPresenterImpl(this, this)
        mWorkLogDetailsPresenter.onCreate(mTimeClock.timeId)

        edit_start_date_button.setOnClickListener {
            openDatePicker(mTimeClock.clockIn * 1000, CLOCK_IN_DATE_PICKER_TAG)
        }

        edit_end_date_button.setOnClickListener {
            openDatePicker(mTimeClock.clockOut * 1000, CLOCK_OUT_DATE_PICKER_TAG)
        }

        add_break_button.setOnClickListener {
            mWorkLogDetailsPresenter.onAddBreakClicked(mTimeClock.timeId, System.currentTimeMillis() / 1000, System.currentTimeMillis() / 1000)
        }
    }

    override fun displayWorkLogDetails(timeclock: Timeclock) {
        mClockedInDay = getDayOfWeek(timeclock.clockIn * 1000)
        mClockedOutDay = getDayOfWeek(timeclock.clockOut * 1000)
        worklog_detail_clocked_in.text = formatTime(timeclock.clockIn * 1000L)
        worklog_detail_clocked_out.text = formatTime(timeclock.clockOut * 1000L)
    }

    override fun displayWorkLogBreaks(breaksArrayList: ArrayList<Break>) {
        val layoutManager = LinearLayoutManager(this)
        mBreaksAdapter = BreaksAdapter(breaksArrayList, this)
        breaks_recycler_view.layoutManager = layoutManager
        breaks_recycler_view.adapter = mBreaksAdapter
        displayWorkLogDetails(mTimeClock)
    }

    override fun displayAddedBreakItem(breakObject: Break) {
        mBreaksAdapter.addBreakItem(breakObject)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        when (mPickerTag) {
            CLOCK_IN_DATE_PICKER_TAG -> {
                mClockedInDate = convertDateToString(year, monthOfYear + 1, dayOfMonth)
                mClockedInDay = dayOfMonth
                openTimePicker(mTimeClock.clockIn * 1000, CLOCK_IN_TIME_PICKER_TAG)
            }
            CLOCK_OUT_DATE_PICKER_TAG -> {
                mClockedOutDate = convertDateToString(year, monthOfYear + 1, dayOfMonth)
                mClockedOutDay = dayOfMonth
                openTimePicker(mTimeClock.clockOut * 1000, CLOCK_OUT_TIME_PICKER_TAG)
            }
            BREAK_START_DATE_PICKER_TAG -> {
                mBreakStartDate = convertDateToString(year, monthOfYear + 1, dayOfMonth)
                openTimePicker(mBreaksAdapter.getBreakStart(mAdapterPosition), BREAK_START_TIME_PICKER_TAG)
            }
            BREAK_END_DATE_PICKER_TAG -> {
                mBreakEndDate = convertDateToString(year, monthOfYear + 1, dayOfMonth)
                openTimePicker(mBreaksAdapter.getBreakEnd(mAdapterPosition), BREAK_END_TIME_PICKER_TAG)
            }
        }
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        when (mPickerTag) {
            CLOCK_IN_TIME_PICKER_TAG -> {
                mTimeClock.clockIn = convertDateToSeconds(mClockedInDate, hourOfDay, minute)
                worklog_detail_clocked_in.text = formatTime(mTimeClock.clockIn * 1000L)
            }
            CLOCK_OUT_TIME_PICKER_TAG -> {
                mTimeClock.clockOut = convertDateToSeconds(mClockedOutDate, hourOfDay, minute)
                worklog_detail_clocked_out.text = formatTime(mTimeClock.clockOut * 1000L)
            }
            BREAK_START_TIME_PICKER_TAG -> {
                mBreaksAdapter.updateBreakStart(convertDateToSeconds(hourOfDay = hourOfDay, minute = minute), mAdapterPosition)
            }
            BREAK_END_TIME_PICKER_TAG -> {
                mBreaksAdapter.updateBreakEnd(convertDateToSeconds(hourOfDay = hourOfDay, minute = minute), mAdapterPosition)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_worklog_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
            }
            R.id.menu_item_update_worklog -> {
                val timeClock = retrieveTimeClockData()
                val breaksArrayList = retrieveBreaks()
                mWorkLogDetailsPresenter.onActionSaveClick(timeClock, breaksArrayList)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayCorrectionSuccessMessage() {
        snackbar(worklog_details_linear_layout, resources.getString(R.string.update_success_message))
    }

    override fun displayCorrectionFailMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayDeleteSuccessMessage() {
        snackbar(worklog_details_linear_layout, resources.getString(R.string.deleted_break_message))
    }

    override fun onStartBreakClicked(position: Int) {
        mAdapterPosition = position
        if (!isSameDay()) {
            openDatePicker(mBreaksAdapter.getBreakStart(position) * 1000, BREAK_START_DATE_PICKER_TAG)
        } else {
            openTimePicker(mBreaksAdapter.getBreakStart(position), BREAK_START_TIME_PICKER_TAG)
        }
    }

    override fun onEndBreakClicked(position: Int) {
        mAdapterPosition = position
        if (!isSameDay()) {
            openDatePicker(mBreaksAdapter.getBreakEnd(position) * 1000, BREAK_END_DATE_PICKER_TAG)
        } else {
            openTimePicker(mBreaksAdapter.getBreakEnd(position), BREAK_END_TIME_PICKER_TAG)
        }
    }

    override fun onDeleteBreakClicked(breakId: Int) {
        mWorkLogDetailsPresenter.onDeleteBreakClicked(breakId)
        mBreaksAdapter.updateBreakItem(mAdapterPosition)
    }

    private fun getDayOfWeek(timeInMillis: Long): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun formatTime(time: Long): String {
        val date = Date(time)
        val formatter = SimpleDateFormat("EEE, MMM dd, h:mm a", Locale.US)
        return formatter.format(date).toString()
    }

    private fun retrieveTimeClockData(): Timeclock {
        return Timeclock(mTimeClock.timeId, mTimeClock.clockIn, mTimeClock.clockOut)
    }

    private fun retrieveBreaks(): ArrayList<Break> {
        return mBreaksAdapter.getBreaksArrayList()
    }

    private fun openDatePicker(timeInMillis: Long, tag: String) {
        mPickerTag = tag
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(fragmentManager, tag)
    }

    private fun openTimePicker(timeInMillis: Long, tag: String) {
        mPickerTag = tag
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        Log.d("time", timeInMillis.toString())
        val tpd = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true)
        tpd.show(fragmentManager, tag)
    }

    private fun convertDateToString(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        return year.toString() + "-" + monthOfYear + "-" + dayOfMonth + " "
    }

    private fun convertDateToSeconds(stringDate: String = "", hourOfDay: Int, minute: Int): Long {
        val finalStringDate: String
        val dateFormat: SimpleDateFormat
        if (stringDate != "") {
            finalStringDate = stringDate + hourOfDay.toString() + ":" + minute.toString() + ":00"
            dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
        } else {
            finalStringDate = hourOfDay.toString() + ":" + minute.toString() + ":00"
            dateFormat = SimpleDateFormat("hh:mm:ss", Locale.US)
        }
        val date = dateFormat.parse(finalStringDate)

        Log.d("convert", date.time.toString())
        return date.time / 1000
    }

    private fun isSameDay(): Boolean {
        return mClockedInDay == mClockedOutDay
    }

}