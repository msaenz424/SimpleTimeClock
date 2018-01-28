package com.android.mig.simpletimeclock.source.model.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.mig.simpletimeclock.source.TimeClockContract
import com.android.mig.simpletimeclock.source.TimeClockDbHelper
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractor

class DeleteBreakTask constructor(context: Context, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener) : AsyncTask<Int, Void, Boolean>() {

    val mContext: Context = context
    val mOnFinishedTransactionListener = onFinishedTransactionListener

    override fun doInBackground(vararg params: Int?): Boolean {
        val mTimeClockDbHelper = TimeClockDbHelper(mContext)
        val db = mTimeClockDbHelper.writableDatabase
        val breakId: Int = params[0]!!
        var isDeleted = false

        try {
            db.beginTransaction()

            val sqlUpdateQuery = "DELETE * FROM " + TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
                    TimeClockContract.Breaks.BREAK_ID + " =?"
            val sqLiteStatement = db.compileStatement(sqlUpdateQuery)

            sqLiteStatement.clearBindings()
            sqLiteStatement.bindLong(1, breakId.toLong())
            sqLiteStatement.execute()

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
            mOnFinishedTransactionListener.onDeleteSuccess()
        }
    }
}