package com.android.mig.simpletimeclock.source.model.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.mig.simpletimeclock.source.TimeClockContract
import com.android.mig.simpletimeclock.source.TimeClockDbHelper
import com.android.mig.simpletimeclock.source.model.Break

class UpdateBreaksTask constructor(context: Context) : AsyncTask<ArrayList<Break>, Void, Boolean>() {

    val mContext: Context = context

    override fun doInBackground(vararg params: ArrayList<Break>): Boolean? {
        val mTimeClockDbHelper = TimeClockDbHelper(mContext)
        val db = mTimeClockDbHelper.writableDatabase
        val breaksArrayList = params[0]

        try {
            db.beginTransaction()
            for (breakItem in breaksArrayList){
                val sqlUpdateQuery = "UPDATE " + TimeClockContract.Breaks.TABLE_BREAKS + " SET " +
                        TimeClockContract.Breaks.TIMECLOCK_BREAK_START + "=?, " +
                        TimeClockContract.Breaks.TIMECLOCK_BREAK_END + "=? WHERE " +
                        TimeClockContract.Breaks.BREAK_ID + " =?"
                val sqLiteStatement = db.compileStatement(sqlUpdateQuery)

                sqLiteStatement.clearBindings()
                sqLiteStatement.bindLong(1, breakItem.breakStart)
                sqLiteStatement.bindLong(2, breakItem.breakEnd)
                sqLiteStatement.bindLong(3, breakItem.breakID.toLong())
                sqLiteStatement.execute()
            }

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.w("Exception: ", e)
        } finally {
            db.endTransaction()
        }
        return true
    }
}