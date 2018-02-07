package com.android.mig.simpletimeclock.presenter

interface WorkLogPresenter {

    fun onResume(empId: Int, dateStart: Long = 0, dateEnd: Long = 0)

}