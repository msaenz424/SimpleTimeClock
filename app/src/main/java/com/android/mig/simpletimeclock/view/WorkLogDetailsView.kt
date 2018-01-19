package com.android.mig.simpletimeclock.view

import com.android.mig.simpletimeclock.source.model.Timeclock

interface WorkLogDetailsView {

    fun displayWorkLogDetails(timeclock: Timeclock)

    fun displayCorrectionSuccessMessage()

    fun displayCorrectionFailMessage()

}