package com.android.mig.simpletimeclock.view

import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.Timeclock

interface WorkLogDetailsView {

    fun displayWorkLogDetails(timeclock: Timeclock)

    fun displayWorkLogBreaks(breaksArrayList: ArrayList<Break>)

    fun displayAddedBreakItem(breakObject: Break)

    fun displayCorrectionSuccessMessage()

    fun displayCorrectionFailMessage()

    fun displayDeleteSuccessMessage()

    fun passTimeClockArray(timeClockArrayList: ArrayList<Timeclock>)

    fun finishActivity()

}