package com.android.mig.simpletimeclock.view.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.android.mig.simpletimeclock.R
import com.android.mig.simpletimeclock.source.model.Break
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BreaksAdapter constructor(breaksArrayList: ArrayList<Break>, onClickHandler: OnClickHandler) : RecyclerView.Adapter<BreaksAdapter.BreaksViewHolder>() {

    private var mOnClickHandler: OnClickHandler = onClickHandler
    private var mBreaksArrayList: ArrayList<Break> = breaksArrayList

    fun updateBreakStart(breakStart: Long, position: Int) {
        mBreaksArrayList[position].breakStart = breakStart
        notifyDataSetChanged()
    }

    fun updateBreakEnd(breakEnd: Long, position: Int) {
        mBreaksArrayList[position].breakEnd = breakEnd
        notifyDataSetChanged()
    }

    fun updateBreakItem(position: Int){
        mBreaksArrayList.removeAt(position)
        notifyDataSetChanged()
    }

    fun getBreakStart(position: Int): Long {
        return mBreaksArrayList[position].breakStart
    }

    fun getBreakEnd(position: Int): Long {
        return mBreaksArrayList[position].breakEnd
    }

    fun getBreaksArrayList(): ArrayList<Break> {
        return mBreaksArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreaksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_break, parent, false)

        return BreaksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreaksViewHolder, position: Int) {
        Log.d("onBind", mBreaksArrayList[position].breakStart.toString())
        holder.mBreakStartTextView.text = formatTime(mBreaksArrayList[position].breakStart * 1000L)
        holder.mBreakEndTextView.text = formatTime(mBreaksArrayList[position].breakEnd * 1000L)
        holder.mBreakStartButton.setOnClickListener { mOnClickHandler.onStartBreakClicked(position) }
        holder.mBreakEndButton.setOnClickListener { mOnClickHandler.onEndBreakClicked(position) }
        holder.mBreakDeleteButton.setOnClickListener{ mOnClickHandler.onDeleteBreakClicked(mBreaksArrayList[position].breakID) }
    }

    override fun getItemCount(): Int {
        return mBreaksArrayList.size
    }

    class BreaksViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mBreakStartTextView: TextView = itemView.findViewById(R.id.item_break_start)
        var mBreakEndTextView: TextView = itemView.findViewById(R.id.item_break_end)
        var mBreakStartButton: ImageButton = itemView.findViewById(R.id.item_break_start_button)
        var mBreakEndButton: ImageButton = itemView.findViewById(R.id.item_break_end_button)
        var mBreakDeleteButton: ImageButton = itemView.findViewById(R.id.item_break_delete)
    }

    private fun formatTime(time: Long): String {
        Log.d("format", time.toString())
        val date = Date(time)
        val formatter = SimpleDateFormat("h:mm a", Locale.US)
        return formatter.format(date).toString()
    }

    interface OnClickHandler {

        fun onStartBreakClicked(position: Int)

        fun onEndBreakClicked(position: Int)

        fun onDeleteBreakClicked(breakId: Int)

    }
}