package com.android.mig.simpletimeclock.source.model

import android.content.Context
import com.android.mig.simpletimeclock.source.model.tasks.ReadWorkLogByEmployeeTask
import com.android.mig.simpletimeclock.source.model.tasks.ReadWorkLogRangeTask

class WorkLogInteractorImpl constructor(context: Context): WorkLogInteractor {

    val mContext = context

    override fun readWorkLogByEmployee(empId: Int, onFinishedTransactionListener: WorkLogInteractor.OnFinishedTransactionListener) {
        val readWorkLog = ReadWorkLogByEmployeeTask(mContext, onFinishedTransactionListener)
        readWorkLog.execute(empId)
    }

    override fun readWorkLogByDateRange(empId: Int, dateStart: Long, dateEnd: Long, onFinishedTransactionListener: WorkLogInteractor.OnFinishedTransactionListener) {
        val readWorkLog = ReadWorkLogRangeTask(mContext, onFinishedTransactionListener)
        readWorkLog.execute(empId.toLong(), dateStart, dateEnd)
    }

}