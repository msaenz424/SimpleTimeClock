package com.android.mig.simpletimeclock.source.model.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.mig.simpletimeclock.source.TimeClockContract
import com.android.mig.simpletimeclock.source.TimeClockDbHelper
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractor

class UpdateWorkLogTask constructor(context: Context, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) : AsyncTask<Long, Void, Boolean>() {

    val mContext: Context = context
    private val mOnFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener = onFinishedTransactionListener

    override fun doInBackground(vararg params: Long?): Boolean {
        val mTimeClockDbHelper = TimeClockDbHelper(mContext)
        val db = mTimeClockDbHelper.writableDatabase
        var isSuccess = false
        val clockIn = params[0]
        val clockOut = params[1]
        val timeId = params[2]

        try {
            db.beginTransaction()
            val sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                    TimeClockContract.Timeclock.TIMECLOCK_CLOCK_IN + "=?, " +
                    TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + "=? WHERE " +
                    TimeClockContract.Timeclock.TIMECLOCK_ID + " =?"
            val sqLiteStatement = db.compileStatement(sqlUpdateQuery)

            sqLiteStatement.clearBindings()
            sqLiteStatement.bindLong(1, clockIn!!)
            sqLiteStatement.bindLong(2, clockOut!!)
            sqLiteStatement.bindLong(3, timeId!!)
            sqLiteStatement.execute()
            db.setTransactionSuccessful()
            isSuccess = true
        } catch (e: Exception) {
            Log.w("Exception: ", e)
        } finally {
            db.endTransaction()
        }
        return isSuccess
    }

    override fun onPostExecute(result: Boolean) {
        if (result) {
            mOnFinishedTransactionListener.onUpdateSuccess()
        }
    }
}