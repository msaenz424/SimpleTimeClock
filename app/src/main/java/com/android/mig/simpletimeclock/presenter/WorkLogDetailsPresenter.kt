package com.android.mig.simpletimeclock.presenter

import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.Timeclock

interface WorkLogDetailsPresenter {

    fun onCreate(timeId: Int)

    fun onActionSaveClick(timeclock: Timeclock, breaksArrayList: ArrayList<Break>)

}