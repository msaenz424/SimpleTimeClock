package com.android.mig.simpletimeclock.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeConverter {

    companion object Factory {

        /**
         * Converts year, month, day, hour and minute entry values to seconds
         * @param year      int value of year
         * @param month     int value of month
         * @param day       int value of day
         * @param hour      int value of hour
         * @param minute    int value of minute
         * @return          representation in seconds
         *
         */
        fun getSeconds(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long{
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis / 1000
        }

        /**
         * Converts a MM/dd/yyyy date to seconds
         *
         * @param stringDate    date in MM/dd/yyyy format
         * @return              representation of specified date in seconds
         */
        fun getSeconds(stringDate: String?): Long {
            if (stringDate != null) {
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                var date: Date? = null
                try {
                    date = dateFormat.parse(stringDate)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                var seconds: Long = 0
                if (date != null) {
                    seconds = date.time / 1000
                }
                return seconds
            } else {
                return 0
            }
        }

        fun getDayOfMonth(timeInMillis: Long): Int {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            return calendar.get(Calendar.DAY_OF_MONTH)
        }

        fun getMonth(timeInMillis: Long): Int {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            return calendar.get(Calendar.MONTH)
        }

        fun getYear(timeInMillis: Long): Int {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            return calendar.get(Calendar.YEAR)
        }

    }

}