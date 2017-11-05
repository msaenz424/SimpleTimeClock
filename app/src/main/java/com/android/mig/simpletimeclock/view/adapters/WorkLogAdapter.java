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

    private Context mContext;
    private ArrayList<Timeclock> mTimeclockArrayList;

    public WorkLogAdapter(Context context) {
        this.mContext = context;
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

                int totalInSeconds = mTimeclockArrayList.get(position).getHoursInSecondsWorked();
                int totalHours = totalInSeconds / 3600;
                int totalMinutes = (totalInSeconds % 3600) / 60;
                viewHolder.mHoursTextView.setText(totalHours + "h " + totalMinutes + "m");

                int totalBreaksInSeconds = mTimeclockArrayList.get(position).getBreaksInSeconds();
                int totalBreaksHours = totalBreaksInSeconds / 3600;
                int totalBreakMinutes = (totalBreaksInSeconds % 3600) / 60;
                viewHolder.mBreaksTextView.setText(totalBreaksHours + "h " + totalBreakMinutes + "m");

                String earned = String.format(Locale.US, "%.2f", mTimeclockArrayList.get(position).getEarned());
                viewHolder.mEarningsTextView.setText(mContext.getResources().getString(R.string.dollar_currency_symbol) + earned);
            }
        } else if (holder instanceof WorkLogFooterViewHolder) {

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

    public class WorkLogViewHolder extends RecyclerView.ViewHolder {

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
}
