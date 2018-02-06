package com.android.mig.simpletimeclock.view

import com.android.mig.simpletimeclock.source.model.Timeclock

interface WorkLogView {

    fun displayWorkLog(timeClockArrayList: ArrayList<Timeclock>?)

}