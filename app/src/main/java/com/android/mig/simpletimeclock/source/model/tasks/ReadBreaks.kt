package com.android.mig.simpletimeclock.source.model.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.mig.simpletimeclock.source.TimeClockContract
import com.android.mig.simpletimeclock.source.TimeClockDbHelper
import com.android.mig.simpletimeclock.source.model.Break
import com.android.mig.simpletimeclock.source.model.WorkLogDetailsInteractor

class ReadBreaks constructor(context: Context, onFinishedTransactionListener: WorkLogDetailsInteractor.OnFinishedTransactionListener): AsyncTask<Int, Void, ArrayList<Break>>() {

    private val BREAKS_ID_INDEX = 0
    private val BREAKS_START_INDEX = 1
    private val BREAKS_END_INDEX = 2

    private val BREAKS_QUERY = "SELECT " +
            TimeClockContract.Breaks.BREAK_ID + ", " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_START + ", " +
            TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " FROM " +
            TimeClockContract.Breaks.TABLE_BREAKS + " WHERE " +
            TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + "=?"

    var mContext: Context = context
    private var mFinishedTransactionListener = onFinishedTransactionListener

    override fun doInBackground(vararg params: Int?): ArrayList<Break> {
        val mTimeClockDbHelper = TimeClockDbHelper(mContext)
        val db = mTimeClockDbHelper.readableDatabase
        val breakArrayList: ArrayList<Break> = ArrayList()

        val empId = params[0].toString()
        val currentCursor = db.rawQuery(BREAKS_QUERY, arrayOf(empId))

        if (currentCursor.moveToFirst()) {
            do {
                breakArrayList.add(Break(currentCursor.getInt(BREAKS_ID_INDEX), currentCursor.getLong(BREAKS_START_INDEX), currentCursor.getLong(BREAKS_END_INDEX)))
            } while (currentCursor.moveToNext())
        }
        currentCursor.close()

        return breakArrayList
    }

    override fun onPostExecute(result: ArrayList<Break>) {
        if (mFinishedTransactionListener == null){
            Log.d("finished", "is null")
        }
        mFinishedTransactionListener.onReadSuccess(result)
    }
}