package com.android.mig.simpletimeclock.source.model.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.mig.simpletimeclock.source.TimeClockContract
import com.android.mig.simpletimeclock.source.TimeClockDbHelper
import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractor

class InsertBreakTask constructor(context: Context, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) : AsyncTask<Int, Void, Break>() {

    val mContext = context
    val mOnFinishedTransactionListener = onFinishedTransactionListener

    override fun doInBackground(vararg params: Int?): Break? {
        val mTimeClockDbHelper = TimeClockDbHelper(mContext)
        val db = mTimeClockDbHelper.writableDatabase
        val timeId = params[0]
        val breakStart = params[1]
        val breakEnd = params[2]

        var breakObject: Break? = null
        try {
            db.beginTransaction()

            val sqlUpdateQuery = "INSERT INTO " + TimeClockContract.Breaks.TABLE_BREAKS + " (" +
                    TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + ", " +
                    TimeClockContract.Breaks.TIMECLOCK_BREAK_START + ", " +
                    TimeClockContract.Breaks.TIMECLOCK_BREAK_END + ") VALUES (?,?,?);"
            val sqLiteStatement = db.compileStatement(sqlUpdateQuery)
            sqLiteStatement.clearBindings()
            sqLiteStatement.bindLong(1, timeId!!.toLong())
            sqLiteStatement.bindLong(2, breakStart!!.toLong())
            sqLiteStatement.bindLong(3, breakEnd!!.toLong())
            val breakId = sqLiteStatement.executeInsert().toInt()
            if (breakId != -1){
                breakObject = Break(breakId, breakStart.toLong(), breakEnd.toLong())
            }
            sqLiteStatement.close()
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.w("Exception: ", e)
        } finally {
            db.endTransaction()
        }
        return breakObject
    }

    override fun onPostExecute(breakObject: Break?) {
        if (breakObject != null){
            mOnFinishedTransactionListener.onBreakInsertSuccess(breakObject)
        }
    }
}