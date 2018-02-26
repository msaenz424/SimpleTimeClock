package com.android.mig.simpletimeclock.source.model

import android.content.Context
import com.android.mig.simpletimeclock.source.model.tasks.*

class WorkLogDetailsInteractorImpl constructor(context: Context): WorkLogDetailsInteractor{
    private val mContext = context

    override fun readBreaks(timeId: Int, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) {
        val readBreaks = ReadBreaks(mContext, onFinishedTransactionListener)
        readBreaks.execute(timeId)
    }

    override fun updateWorkLog(timeclock: Timeclock, breakArrayList: ArrayList<Break>, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) {
        val updateWorkLog = UpdateWorkLogTask(mContext, onFinishedTransactionListener)
        val updateBreaks = UpdateBreaksTask(mContext)
        val clockIn = timeclock.clockIn
        val clockOut = timeclock.clockOut
        val timeId = timeclock.timeId

        updateWorkLog.execute(clockIn, clockOut, timeId.toLong())
        if (breakArrayList.size > 0) {
            updateBreaks.execute(breakArrayList)
        }
    }

    override fun addBreak(timeId: Int, breakStart: Long, breakEnd: Long, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) {
        val addBreak = InsertBreakTask(mContext, onFinishedTransactionListener)
        addBreak.execute(timeId, breakStart.toInt(), breakEnd.toInt())
    }


    override fun deleteBreak(breakId: Int, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) {
        val deleteBreak = DeleteBreakTask(mContext, onFinishedTransactionListener)
        deleteBreak.execute(breakId)
    }

    override fun deleteTime(timeId: Int, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) {
        val deleteTime = DeleteTimeTask(mContext, onFinishedTransactionListener)
        deleteTime.execute(timeId)
    }

}