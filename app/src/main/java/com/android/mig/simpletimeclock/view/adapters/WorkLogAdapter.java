package com.android.mig.simpletimeclock.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.source.model.Timeclock;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class WorkLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;
    private static final int TYPE_FOOTER = 2;

    private OnClickHandler mOnClickHandler;
    private Context mContext;
    private ArrayList<Timeclock> mTimeclockArrayList;
    private double mTotalEarnings;
    private int mTotalHours, mTotalMinutes, mTotalBreakHours, mTotalBreakMinutes;

    public WorkLogAdapter(Context context, OnClickHandler onClickHandler) {
        this.mContext = context;
        mOnClickHandler = onClickHandler;
    }

    public void setWorkLogData(ArrayList<Timeclock> timeclockArrayList) {
        this.mTimeclockArrayList = timeclockArrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_HEADER){
            int layoutIdForListItem = R.layout.item_work_log_header;
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            WorkLogHeaderViewHolder viewHolder = new WorkLogHeaderViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_BODY){
            int layoutIdForListItem = R.layout.item_work_log;
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            WorkLogViewHolder viewHolder = new WorkLogViewHolder(view);
            return viewHolder;
        } else {
            int layoutIdForListItem = R.layout.item_work_log_footer;
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            WorkLogFooterViewHolder viewHolder = new WorkLogFooterViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkLogViewHolder){
            if (position > 0 && position < mTimeclockArrayList.size() + 1) {
                WorkLogViewHolder viewHolder = (WorkLogViewHolder) holder;
                position--;
                if (position % 2 != 0){
                    viewHolder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.work_log_item_background));
                }
                long clockIn = mTimeclockArrayList.get(position).getClockIn() * 1000L;
                long clockOut = mTimeclockArrayList.get(position).getClockOut() * 1000L;

                // sets the format of the date
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd", Locale.US);
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

                // displays the date. Ex. Tue, Oct 31
                Timestamp clockInStamp = new Timestamp(clockIn);
                Date startDate = new Date(clockInStamp.getTime());
                String dateString = dayFormat.format(startDate);
                viewHolder.mDateTextView.setText(dateString);

                // displays the time frame. Ex. 09:30 - 17:30
                Timestamp clockOutStamp = new Timestamp(clockOut);
                Date endDate = new Date(clockOutStamp.getTime());
                String startTimeString = timeFormat.format(startDate);
                String endTimeString;
                if (clockOut == 0) {
                    endTimeString = "Now";
                } else {
                    endTimeString = timeFormat.format(endDate);
                }
                viewHolder.mTimeTextView.setText(startTimeString + " - " + endTimeString);

                /** TODO needs to use calcs using minutes, not seconds (subtract break (in min) from time worked (in min) */

                int totalInMinutes = mTimeclockArrayList.get(position).getHoursWorkedInMinutes();
                int totalHours = totalInMinutes / 60;
                int totalMinutes = totalInMinutes % 60;
                viewHolder.mHoursTextView.setText(totalHours + "h " + totalMinutes + "m");

                int totalBreaksInMinutes = mTimeclockArrayList.get(position).getBreaksInMinutes();
                int totalBreaksHours = totalBreaksInMinutes / 60;
                int totalBreakMinutes = totalBreaksInMinutes % 60;
                viewHolder.mBreaksTextView.setText(totalBreaksHours + "h " + totalBreakMinutes + "m");

                double earned = mTimeclockArrayList.get(position).getEarned();
                String earnedString = String.format(Locale.US, "%.2f", earned);
                viewHolder.mEarningsTextView.setText(mContext.getResources().getString(R.string.dollar_currency_symbol) + earnedString);

                // sums up totals for later use in footer
                int minSum = mTotalMinutes + totalMinutes;
                if (minSum >= 60) {
                    mTotalHours++;
                    mTotalMinutes = minSum - 60;
                } else {
                    mTotalMinutes += totalMinutes;
                }
                int minBreakSum = mTotalBreakMinutes + totalBreakMinutes;
                if (minBreakSum >= 60){
                    mTotalBreakHours++;
                    mTotalBreakMinutes = minBreakSum - 60;
                } else {
                    mTotalBreakMinutes += totalBreakMinutes;
                }
                mTotalHours += totalHours;
                mTotalBreakHours += totalBreaksHours;
                mTotalEarnings += earned;
            }
        } else if (holder instanceof WorkLogFooterViewHolder) {
                WorkLogFooterViewHolder viewHolder = (WorkLogFooterViewHolder) holder;
                viewHolder.mHoursTextView.setText(mTotalHours + "h " + mTotalMinutes + "m");
                viewHolder.mBreaksTextView.setText(mTotalBreakHours + "h " + mTotalBreakMinutes + "m");
                String earnedString = String.format(Locale.US, "%.2f", mTotalEarnings);
                viewHolder.mEarningsTextView.setText(mContext.getResources().getString(R.string.dollar_currency_symbol) + earnedString);
        }
    }

    @Override
    public int getItemCount() {
        if (mTimeclockArrayList != null) {
            return mTimeclockArrayList.size() + 2;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == mTimeclockArrayList.size() + 1){
            return TYPE_FOOTER;
        } else {
            return TYPE_BODY;
        }
    }

    public class WorkLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout mLinearLayout;
        TextView mDateTextView, mTimeTextView, mHoursTextView, mBreaksTextView, mEarningsTextView;

        public WorkLogViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.work_log_item_linear_layout);
            mDateTextView = itemView.findViewById(R.id.item_work_log_date);
            mTimeTextView = itemView.findViewById(R.id.item_work_log_time);
            mHoursTextView = itemView.findViewById(R.id.item_work_log_hours);
            mBreaksTextView = itemView.findViewById(R.id.item_work_log_breaks);
            mEarningsTextView = itemView.findViewById(R.id.item_work_log_earnings);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Timeclock timeclock = mTimeclockArrayList.get(getAdapterPosition() - 1);
            mOnClickHandler.onItemClick(timeclock);
        }
    }

    public class WorkLogHeaderViewHolder extends RecyclerView.ViewHolder {

        public WorkLogHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class WorkLogFooterViewHolder extends RecyclerView.ViewHolder {

        TextView mHoursTextView, mBreaksTextView, mEarningsTextView;

        public WorkLogFooterViewHolder(View itemView) {
            super(itemView);
            mHoursTextView = itemView.findViewById(R.id.item_work_log_footer_hours);
            mBreaksTextView = itemView.findViewById(R.id.item_work_log_footer_breaks);
            mEarningsTextView = itemView.findViewById(R.id.item_work_log_footer_earnings);
        }
    }

    public interface OnClickHandler {
        void onItemClick(Timeclock timeclock);
    }

}
