package com.android.mig.simpletimeclock.presenter

import android.content.Context
import com.android.mig.simpletimeclock.source.model.Timeclock
import com.android.mig.simpletimeclock.source.model.WorkLogInteractor
import com.android.mig.simpletimeclock.source.model.WorkLogInteractorImpl
import com.android.mig.simpletimeclock.view.WorkLogView

class WorkLogPresenterImpl constructor(workLogView: WorkLogView, context: Context) : WorkLogPresenter, WorkLogInteractor.OnFinishedTransactionListener {

    val mWorkLogView = workLogView
    val mWorkLogInteractor : WorkLogInteractor = WorkLogInteractorImpl(context)

    override fun onResume(empId: Int) {
        mWorkLogInteractor.readWorkLogByEmployee(empId, this)
    }

    override fun onReadWorkLogByEmployeeSuccess(timeClockArrayList: ArrayList<Timeclock>) {
        mWorkLogView.displayWorkLog(timeClockArrayList)
    }

}