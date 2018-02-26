package com.android.mig.simpletimeclock.source.model.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.mig.simpletimeclock.source.TimeClockContract
import com.android.mig.simpletimeclock.source.TimeClockDbHelper
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractor

class DeleteTimeTask constructor(context: Context, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) : AsyncTask<Int, Void, Boolean>() {

    val mContext = context
    val mOnFinishedTransactionListener = onFinishedTransactionListener

    override fun doInBackground(vararg params: Int?): Boolean {
        val mTimeClockDbHelper = TimeClockDbHelper(mContext)
        val db = mTimeClockDbHelper.writableDatabase
        val timeId: Int = params[0]!!
        var isDeleted = false

        try {
            db.beginTransaction()

            // first delete breaks related to given time Id
            val sqlDeleteBreaksQuery = "DELETE FROM " + TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
                    TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + " =?"
            val sqLiteBreakStatement = db.compileStatement(sqlDeleteBreaksQuery)

            sqLiteBreakStatement.clearBindings()
            sqLiteBreakStatement.bindLong(1, timeId.toLong())
            sqLiteBreakStatement.execute()

            // second delete give time
            val sqlDeleteTimeQuery = "DELETE FROM " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " WHERE " +
                    TimeClockContract.Timeclock.TIMECLOCK_ID + " =?"
            val sqLiteTimeStatement = db.compileStatement(sqlDeleteTimeQuery)

            sqLiteTimeStatement.clearBindings()
            sqLiteTimeStatement.bindLong(1, timeId.toLong())
            sqLiteTimeStatement.execute()

            db.setTransactionSuccessful()
            isDeleted = true
        } catch (e: Exception) {
            Log.w("Exception: ", e)
        } finally {
            db.endTransaction()
        }
        return isDeleted
    }

    override fun onPostExecute(result: Boolean) {
        if (result){
            mOnFinishedTransactionListener.onDeleteTimeSuccess()
        }
    }
}