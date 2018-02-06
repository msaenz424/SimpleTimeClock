package com.android.mig.simpletimeclock.source.model

interface WorkLogInteractor {

    interface OnFinishedTransactionListener{

        fun onReadWorkLogByEmployeeSuccess(timeClockArrayList: ArrayList<Timeclock>)

    }

    fun readWorkLogByEmployee(empId: Int, onFinishedTransactionListener: OnFinishedTransactionListener)

}