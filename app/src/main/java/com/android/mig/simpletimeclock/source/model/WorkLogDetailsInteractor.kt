package com.android.mig.simpletimeclock.source.model

interface WorkLogDetailsInteractor {

    interface OnFinishedTransactionListener{

        fun onReadSuccess(breakArrayList: ArrayList<Break>)

        fun onUpdateSuccess()

        fun onDeleteSuccess()

    }

    fun readBreaks(timeId: Int, onFinishedTransactionListener: OnFinishedTransactionListener)

    fun updateWorkLog(timeclock: Timeclock, breakArrayList: ArrayList<Break>, onFinishedTransactionListener: OnFinishedTransactionListener)

}