package com.android.mig.simpletimeclock.source.model

import android.content.Context
import com.android.mig.simpletimeclock.source.model.tasks.ReadBreaks

class WorkLogDetailsInteractorImpl constructor(context: Context): WorkLogDetailsInteractor{

    private val mContext = context

    override fun readBreaks(timeId: Int, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) {
        val readBreaks = ReadBreaks(mContext, onFinishedTransactionListener)
        readBreaks.execute(timeId)
    }

}