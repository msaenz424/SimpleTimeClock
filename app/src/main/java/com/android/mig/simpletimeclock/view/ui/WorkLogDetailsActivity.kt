package com.android.mig.simpletimeclock.view.ui

import android.app.ActionBar
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
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
import com.android.mig.simpletimeclock.utils.TimeConverter
import com.android.mig.simpletimeclock.view.WorkLogDetailsView
import com.android.mig.simpletimeclock.view.adapters.BreaksAdapter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_work_log_details.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
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
    private var mClockedInMonth: Int = 0
    private var mClockedOutMonth: Int = 0
    private var mClockedInYear: Int = 0
    private var mClockedOutYear: Int = 0

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

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
            mWorkLogDetailsPresenter.onAddBreakClicked(mTimeClock.timeId, mTimeClock.clockIn , mTimeClock.clockIn)  // use clockIn time as starting point for breaks
        }

        worklog_delete_button.setOnClickListener {
            alert(resources.getString(R.string.worklog_detail_delete_dialog_text)) {
                positiveButton(resources.getString(R.string.worklog_detail_delete_dialog_yes)) { mWorkLogDetailsPresenter.onDeleteTimeClicked(mTimeClock.timeId) }
                negativeButton(resources.getString(R.string.worklog_detail_delete_dialog_no)) {}
            }.show()
        }

    }

    override fun displayWorkLogDetails(timeclock: Timeclock) {
        val clockInTimeInMillis = timeclock.clockIn * 1000
        val clockOutTimeInMillis = timeclock.clockOut * 1000
        mClockedInDay = TimeConverter.Factory.getDayOfMonth(clockInTimeInMillis)
        mClockedOutDay = TimeConverter.Factory.getDayOfMonth(clockOutTimeInMillis)
        mClockedInMonth = TimeConverter.Factory.getMonth(clockInTimeInMillis)
        mClockedOutMonth = TimeConverter.Factory.getMonth(clockOutTimeInMillis)
        mClockedInYear = TimeConverter.Factory.getYear(clockInTimeInMillis)
        mClockedOutYear = TimeConverter.Factory.getYear(clockOutTimeInMillis)
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
        mYear = year
        mMonth = monthOfYear
        mDay = dayOfMonth

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
                openTimePicker(mBreaksAdapter.getBreakStart(mAdapterPosition) * 1000, BREAK_START_TIME_PICKER_TAG)
            }
            BREAK_END_DATE_PICKER_TAG -> {
                mBreakEndDate = convertDateToString(year, monthOfYear + 1, dayOfMonth)
                openTimePicker(mBreaksAdapter.getBreakEnd(mAdapterPosition) * 1000, BREAK_END_TIME_PICKER_TAG)
            }
        }
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        when (mPickerTag) {
            CLOCK_IN_TIME_PICKER_TAG -> {
                mTimeClock.clockIn = TimeConverter.Factory.getSeconds(mYear, mMonth, mDay, hourOfDay, minute)
                worklog_detail_clocked_in.text = formatTime(mTimeClock.clockIn * 1000L)
            }
            CLOCK_OUT_TIME_PICKER_TAG -> {
                mTimeClock.clockOut = TimeConverter.Factory.getSeconds(mYear, mMonth, mDay, hourOfDay, minute)
                worklog_detail_clocked_out.text = formatTime(mTimeClock.clockOut * 1000L)
            }
            BREAK_START_TIME_PICKER_TAG -> {
                mBreaksAdapter.updateBreakStart(TimeConverter.Factory.getSeconds(mYear, mMonth, mDay, hourOfDay, minute), mAdapterPosition)
            }
            BREAK_END_TIME_PICKER_TAG -> {
                mBreaksAdapter.updateBreakEnd(TimeConverter.Factory.getSeconds(mYear, mMonth, mDay, hourOfDay, minute), mAdapterPosition)
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

                if (validateDates(timeClock, breaksArrayList)) {
                    mWorkLogDetailsPresenter.onActionSaveClick(timeClock, breaksArrayList)
                } else {
                    displayIncoherentDatesMessage()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayCorrectionSuccessMessage() {
        snackbar(worklog_details_linear_layout, resources.getString(R.string.update_success_message))
                .view.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))

    }

    override fun displayCorrectionFailMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayDeleteSuccessMessage() {
        snackbar(worklog_details_linear_layout, resources.getString(R.string.deleted_break_message))
                .view.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
    }

    override fun finishActivity() {
        toast(resources.getString(R.string.deleted_time_message))
        finish()
    }

    override fun onStartBreakClicked(position: Int) {
        mAdapterPosition = position
        if (!isSameDay()) {
            openDatePicker(mBreaksAdapter.getBreakStart(position) * 1000, BREAK_START_DATE_PICKER_TAG)
        } else {
            mYear = mClockedInYear
            mMonth = mClockedInMonth
            mDay = mClockedInDay
            openTimePicker(mBreaksAdapter.getBreakStart(position) * 1000, BREAK_START_TIME_PICKER_TAG)
        }
    }

    override fun onEndBreakClicked(position: Int) {
        mAdapterPosition = position
        if (!isSameDay()) {
            openDatePicker(mBreaksAdapter.getBreakEnd(position) * 1000, BREAK_END_DATE_PICKER_TAG)
        } else {
            mYear = mClockedInYear
            mMonth = mClockedInMonth
            mDay = mClockedInDay
            openTimePicker(mBreaksAdapter.getBreakEnd(position) * 1000, BREAK_END_TIME_PICKER_TAG)
        }
    }

    override fun onDeleteBreakClicked(breakId: Int) {
        mWorkLogDetailsPresenter.onDeleteBreakClicked(breakId)
        mBreaksAdapter.updateBreakItem(mAdapterPosition)
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
                false)
        tpd.show(fragmentManager, tag)
    }

    private fun convertDateToString(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        return year.toString() + "-" + monthOfYear + "-" + dayOfMonth + " "
    }

    private fun isSameDay(): Boolean {
        return mClockedInDay == mClockedOutDay && mClockedInMonth == mClockedOutMonth && mClockedInYear == mClockedOutYear
    }

    /**
     * Validates if dates on fields are coherent
     *
     * @return  boolean value, true if coherent or false if it's not
     */
    private fun validateDates(timeClock: Timeclock, breaksArrayList: ArrayList<Break>): Boolean {
        for (breakItem in breaksArrayList) {
            if (breakItem.breakStart > breakItem.breakEnd) {
                return false
            } else if (breakItem.breakStart < timeClock.clockIn || breakItem.breakStart > timeClock.clockOut) {
                return false
            } else if (breakItem.breakEnd < timeClock.clockIn || breakItem.breakEnd > timeClock.clockOut) {
                return false
            }
        }

        if (timeClock.clockIn > timeClock.clockOut) {
            return false
        }

        return true
    }

    private fun displayIncoherentDatesMessage() {
        val snackBar = Snackbar.make(worklog_details_linear_layout, resources.getString(R.string.snackbar_incoherent_dates_message), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(resources.getString(R.string.snackbar_ok_button), { snackBar.dismiss() })
        snackBar.setActionTextColor(ContextCompat.getColor(applicationContext, android.R.color.white))
        snackBar.view.setBackgroundColor(ContextCompat.getColor(applicationContext, android.R.color.holo_red_light))
        snackBar.show()
    }
}