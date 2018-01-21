package com.android.mig.simpletimeclock.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.mig.simpletimeclock.R
import com.android.mig.simpletimeclock.source.model.Break
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class BreaksAdapter constructor(breaksArrayList: ArrayList<Break>) : RecyclerView.Adapter<BreaksAdapter.BreaksViewHolder>() {

    private var mBreaksArrayList: ArrayList<Break> = breaksArrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreaksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_break, parent, false)

        return BreaksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreaksViewHolder, position: Int) {
        holder.mBreakStartTextView.text = formatTime(mBreaksArrayList[position].breakStart)
        holder.mBreakEndTextView.text = formatTime(mBreaksArrayList[position].breakEnd)
    }

    override fun getItemCount(): Int {
        return mBreaksArrayList.size
    }

    class BreaksViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mBreakStartTextView: TextView = itemView.findViewById(R.id.item_break_start)
        var mBreakEndTextView: TextView = itemView.findViewById(R.id.item_break_end)

    }

    private fun formatTime(time: Long): String{
        val date = Date(time)
        val formatter = SimpleDateFormat("MMM dd, h:mm a", Locale.US)
        return formatter.format(date).toString()
    }
}