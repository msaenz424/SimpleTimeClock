package com.android.mig.simpletimeclock.utils

import android.database.Cursor
import com.android.mig.simpletimeclock.source.model.Timeclock
import java.math.BigDecimal

class TimeCalculations {

    companion object Factory {

        private val mTimeCalculations = TimeCalculations()

        var mTotalTimeWorkedInMinutes: Int = 0
        var mTotalEarnings: Double = 0.0

        /**
         * Creates a new Timeclock object and sums up totals of minutes and earnings
         *
         * @param breaksCursor      cursor containing breaks from db
         * @param breakStartIndex   index of break start in cursor
         * @param breakEndIndex     index of break end in cursor
         * @param timeId            timeclock id
         * @param clockStart        clock in time in seconds
         * @param clockEnd          clock out time in seconds
         * @param wage              employee's wage
         * @return                  a new Timeclock object
         */
        fun createTimeClockItemWithTotals(breaksCursor: Cursor, breakStartIndex: Int, breakEndIndex: Int, timeId: Int, clockStart: Long, clockEnd: Long, wage: Double): Timeclock {
            val currentBreakInMinutes = mTimeCalculations.calculateBreakInMinutes(breaksCursor, breakStartIndex, breakEndIndex, clockEnd)
            val currentTimeSpanInMinutes = mTimeCalculations.calculateTimeSpanInMinutes(clockEnd, clockStart)
            val currentTimeWorkedInMinutes = currentTimeSpanInMinutes - currentBreakInMinutes
            val currentEarningsInDecimals = mTimeCalculations.calculateEarningsInDecimals(currentTimeWorkedInMinutes, wage)

            mTimeCalculations.sumTotals(currentTimeWorkedInMinutes, currentEarningsInDecimals)

            return Timeclock(timeId, clockStart, clockEnd, currentTimeWorkedInMinutes, currentBreakInMinutes, currentEarningsInDecimals)
        }
        /**
         * Creates a new Timeclock object
         *
         * @param breaksCursor      cursor containing breaks from db
         * @param breakStartIndex   index of break start in cursor
         * @param breakEndIndex     index of break end in cursor
         * @param timeId            timeclock id
         * @param clockStart        clock in time in seconds
         * @param clockEnd          clock out time in seconds
         * @param wage              employee's wage
         * @return                  a new Timeclock object
         */
        fun createTimeClockItem(breaksCursor: Cursor, breakStartIndex: Int, breakEndIndex: Int, timeId: Int, clockStart: Long, clockEnd: Long, wage: Double): Timeclock {
            val currentBreakInMinutes = mTimeCalculations.calculateBreakInMinutes(breaksCursor, breakStartIndex, breakEndIndex, clockEnd)
            val currentTimeSpanInMinutes = mTimeCalculations.calculateTimeSpanInMinutes(clockEnd, clockStart)
            val currentTimeWorkedInMinutes = currentTimeSpanInMinutes - currentBreakInMinutes
            val currentEarningsInDecimals = mTimeCalculations.calculateEarningsInDecimals(currentTimeWorkedInMinutes, wage)

            return Timeclock(timeId, clockStart, clockEnd, currentTimeWorkedInMinutes, currentBreakInMinutes, currentEarningsInDecimals)
        }

    }

    /**
     * Calculates the breaks in minutes
     *
     * @param breaksCursor      cursor containing breaks from db
     * @param breakStartIndex   index of break start in cursor
     * @param breakEndIndex     index of break end in cursor
     * @param timeNow           system time used as starting point
     * @return                  total break rounded in minutes
     */
    private fun calculateBreakInMinutes(breaksCursor: Cursor, breakStartIndex: Int, breakEndIndex: Int, timeNow: Long): Int {
        var breakSum = 0.0
        if (breaksCursor.count > 0) {
            breaksCursor.moveToFirst()
            do {
                val breakStart = breaksCursor.getLong(breakStartIndex)
                var breakEnd = breaksCursor.getLong(breakEndIndex)
                if (breakEnd == 0L) {
                    breakEnd = timeNow
                }
                breakSum += (breakEnd - breakStart).toInt().toDouble()
            } while (breaksCursor.moveToNext())
        }
        breaksCursor.close()
        return Math.round(breakSum / 60).toInt()
    }

    /**
     * Calculates time span from two values
     *
     * @param clockOutTime  end time value
     * @param clockInTime   start time value
     * @return              time span in minutes
     */
    private fun calculateTimeSpanInMinutes(clockOutTime: Long, clockInTime: Long): Int {

        val doubleClockOutTime = if (clockOutTime == 0L) {
            (System.currentTimeMillis() / 1000).toDouble()
        } else {
            clockOutTime.toDouble()
        }
        val doubleClockIntTime = clockInTime.toDouble()
        return Math.round((doubleClockOutTime - doubleClockIntTime) / 60).toInt()
    }

    /**
     * Calculates the earning in decimals
     *
     * @param minutesWorked actual minutes worked
     * @param wage          employee's wage
     * @return              decimal expression of earnings
     */
    private fun calculateEarningsInDecimals(minutesWorked: Int, wage: Double): Double {
        var earned: Double
        val decimalHours = minutesWorked.toDouble() / 60
        earned = decimalHours * wage
        var earningsBigDecimal = BigDecimal(earned.toString())                 // string on BigDecimal helps to preserve precision
        earningsBigDecimal = earningsBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
        earned = earningsBigDecimal.toDouble()
        return earned
    }

    /**
     * Adds up the totals of time worked and earnings
     *
     * @param timeWorked    time worked in minutes
     * @param earned        earning
     */
    private fun sumTotals(timeWorked: Int, earned: Double) {
        mTotalTimeWorkedInMinutes += timeWorked
        mTotalEarnings += earned
    }

}