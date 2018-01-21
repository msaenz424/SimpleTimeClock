package com.android.mig.simpletimeclock.presenter

import android.content.Context
import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractor
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractorImpl
import com.android.mig.simpletimeclock.view.WorkLogDetailsView
import com.android.mig.simpletimeclock.view.ui.WorkLogDetailsActivity


class WorkLogDetailsPresenterImpl constructor(workLogDetailsView: WorkLogDetailsView, context: Context) : WorkLogDetailsPresenter, WorkLogDetailsInteractor.OnFinishedTransactionListener {

    val mWorkLogDetailsInteractor: WorkLogDetailsInteractor = WorkLogDetailsInteractorImpl(context)
    val mWorkLogDetailsView = workLogDetailsView

    override fun onCreate(timeId: Int) {
        mWorkLogDetailsInteractor.readBreaks(timeId, this)
    }

    override fun onReadSuccess(breakArrayList: ArrayList<Break>) {
        mWorkLogDetailsView.displayWorkLogBreaks(breakArrayList)
    }

}