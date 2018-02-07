package com.android.mig.simpletimeclock.source.model

interface WorkLogInteractor {

    interface OnFinishedTransactionListener{

        fun onReadWorkLogByEmployeeSuccess(timeClockArrayList: ArrayList<Timeclock>)

        fun onReadWorkLogByDateRangeSuccess(timeClockArrayList: ArrayList<Timeclock>)

    }

    fun readWorkLogByEmployee(empId: Int, onFinishedTransactionListener: OnFinishedTransactionListener)

    fun readWorkLogByDateRange(empId: Int, dateStart: Long, dateEnd: Long, onFinishedTransactionListener: OnFinishedTransactionListener)

}