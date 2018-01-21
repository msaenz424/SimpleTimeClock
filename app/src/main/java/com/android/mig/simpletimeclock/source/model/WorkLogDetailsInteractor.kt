package com.android.mig.simpletimeclock.source.model

interface WorkLogDetailsInteractor {

    interface OnFinishedTransactionListener{

        fun onReadSuccess(breakArrayList: ArrayList<Break>)

    }

    fun readBreaks(timeId: Int, onFinishedTransactionListener: OnFinishedTransactionListener)

}