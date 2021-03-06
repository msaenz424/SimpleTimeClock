package com.android.mig.simpletimeclock.presenter

import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.Timeclock

interface WorkLogDetailsPresenter {

    fun onCreate(timeId: Int)

    fun onActionSaveClick(timeclock: Timeclock, breaksArrayList: ArrayList<Break>)

    fun onAddBreakClicked(timeId: Int, breakStart: Long, breakEnd: Long)

    fun onDeleteBreakClicked(breakId: Int)

    fun onClockedInButtonClicked(clockedIntTime: Long)

    fun onClockedOutButtonClicked(clockedOutTime: Long)

    fun onDeleteTimeClicked(timeId: Int)

}