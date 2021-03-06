package com.android.mig.simpletimeclock.source.model.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.mig.simpletimeclock.source.TimeClockContract
import com.android.mig.simpletimeclock.source.TimeClockDbHelper
import com.android.mig.simpletimeclock.source.model.Timeclock
import com.android.mig.simpletimeclock.source.model.WorkLogInteractor
import com.android.mig.simpletimeclock.utils.TimeCalculations

class ReadWorkLogByEmployeeTask constructor(context: Context, onFinishedTransactionListener: WorkLogInteractor.OnFinishedTransactionListener) : AsyncTask<Int, Void, ArrayList<Timeclock>>() {

    private val UNPAID_STATUS = 0

    private val UNPAID_TIME_ID_INDEX = 0
    private val UNPAID_TIME_CLOCK_IN_INDEX = 1
    private val UNPAID_TIME_CLOCK_OUT_INDEX = 2
    private val UNPAID_TIME_WAGE_INDEX = 3

    private val BREAKS_START_INDEX = 0
    private val BREAKS_END_INDEX = 1

    private val mContext = context
    private val mOnFinishedTransactionListener = onFinishedTransactionListener

    private val UNPAID_WORKLOG_QUERY = "SELECT " +
            TimeClockContract.Timeclock.TIMECLOCK_ID + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + ", " +
            TimeClockContract.Timeclock.TIMECLOCK_WAGE + " FROM " +
            TimeClockContract.Timeclock.TABLE_TIMECLOCK + " WHERE " +
            TimeClockContract.Timeclock.TIMECLOCK_EMP_ID + "=? AND " +
            TimeClockContract.Timeclock.TIMECLOCK_PAID + "=? ORDER BY " +
            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + " DESC"

    private val BREAKS_QUERY = "SELECT " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_START + ", " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " FROM " +
            TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
            TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + "=?"

    override fun doInBackground(vararg params: Int?): ArrayList<Timeclock> {
        val timeClockArrayList = java.util.ArrayList<Timeclock>()
        val mTimeClockDbHelper = TimeClockDbHelper(mContext)
        val db = mTimeClockDbHelper.readableDatabase

        val empIdString = params[0].toString()
        val unpaidTimeString = UNPAID_STATUS.toString()

        try {
            db.beginTransaction()
            val timeClockCursor = db.rawQuery(UNPAID_WORKLOG_QUERY, arrayOf(empIdString, unpaidTimeString))
            if (timeClockCursor.moveToFirst()) {
                do {
                    val timeId = timeClockCursor.getInt(UNPAID_TIME_ID_INDEX)
                    val clockIn = timeClockCursor.getLong(UNPAID_TIME_CLOCK_IN_INDEX)
                    val clockOut = timeClockCursor.getLong(UNPAID_TIME_CLOCK_OUT_INDEX)
                    val wage = timeClockCursor.getDouble(UNPAID_TIME_WAGE_INDEX)

                    val breaksCursor = db.rawQuery(BREAKS_QUERY, arrayOf(timeId.toString()))

                    val timeClock = TimeCalculations.Factory.createTimeClockItem(breaksCursor, BREAKS_START_INDEX, BREAKS_END_INDEX, timeId, clockIn, clockOut, wage)
                    timeClockArrayList.add(timeClock)
                } while (timeClockCursor.moveToNext())
            }
            timeClockCursor.close()
        } catch (e: Exception) {
            Log.w("Exception: ", e)
        } finally {
            db.endTransaction()
        }

        return timeClockArrayList
    }

    override fun onPostExecute(result: ArrayList<Timeclock>) {
        mOnFinishedTransactionListener.onReadWorkLogByEmployeeSuccess(result)
    }

}