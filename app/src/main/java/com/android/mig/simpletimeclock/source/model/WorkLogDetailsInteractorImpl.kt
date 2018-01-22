package com.android.mig.simpletimeclock.source.model

import android.content.Context
import com.android.mig.simpletimeclock.source.model.tasks.ReadBreaks
import com.android.mig.simpletimeclock.source.model.tasks.UpdateBreaksTask
import com.android.mig.simpletimeclock.source.model.tasks.UpdateWorkLogTask

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

}