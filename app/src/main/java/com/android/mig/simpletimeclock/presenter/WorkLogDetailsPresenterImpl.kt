package com.android.mig.simpletimeclock.presenter

import android.content.Context
import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.Timeclock
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractor
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractorImpl
import com.android.mig.simpletimeclock.view.WorkLogDetailsView

class WorkLogDetailsPresenterImpl constructor(workLogDetailsView: WorkLogDetailsView, context: Context) : WorkLogDetailsPresenter, WorkLogDetailsInteractor.OnFinishedTransactionListener {

    val mWorkLogDetailsInteractor: WorkLogDetailsInteractor = WorkLogDetailsInteractorImpl(context)
    val mWorkLogDetailsView = workLogDetailsView

    override fun onCreate(timeId: Int) {
        mWorkLogDetailsInteractor.readBreaks(timeId, this)
    }

    override fun onActionSaveClick(timeclock: Timeclock, breaksArrayList: ArrayList<Break>) {
        mWorkLogDetailsInteractor.updateWorkLog(timeclock, breaksArrayList, this)
    }

    override fun onAddBreakClicked(timeId: Int, breakStart: Long, breakEnd: Long) {
        mWorkLogDetailsInteractor.addBreak(timeId, breakStart, breakEnd, this)
    }

    override fun onDeleteBreakClicked(breakId: Int) {
        mWorkLogDetailsInteractor.deleteBreak(breakId,this)
    }

    override fun onReadSuccess(breakArrayList: ArrayList<Break>) {
        mWorkLogDetailsView.displayWorkLogBreaks(breakArrayList)
    }

    override fun onUpdateSuccess() {
        mWorkLogDetailsView.displayCorrectionSuccessMessage()
    }

    override fun onBreakInsertSuccess(breakObject: Break) {
        mWorkLogDetailsView.displayAddedBreakItem(breakObject)
    }

    override fun onDeleteSuccess() {
        mWorkLogDetailsView.displayDeleteSuccessMessage()
    }

    override fun onClockedInButtonClicked(clockedIntTime: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClockedOutButtonClicked(clockedOutTime: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}