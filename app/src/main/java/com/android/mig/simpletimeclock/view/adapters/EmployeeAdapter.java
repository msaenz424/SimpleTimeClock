package com.android.mig.simpletimeclock.view.adapters;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.source.model.ActiveEmployee;
import com.android.mig.simpletimeclock.source.model.Break;
import com.android.mig.simpletimeclock.view.utils.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BODY = 0;
    private static final int TYPE_FOOTER = 1;

    private final OnClickHandler mOnClickHandler;
    private Context mContext;

    private ArrayList<ActiveEmployee> mActiveEmployeesArrayList = null;
    private ArrayList<Chronometer> mChronometerArrayList;

    public EmployeeAdapter(OnClickHandler onClickHandler) {
        this.mOnClickHandler = onClickHandler;
        this.mContext = (Context) onClickHandler;
    }

    public void setEmployeesData(ArrayList<ActiveEmployee> activeEmployeesArrayList) {
        this.mActiveEmployeesArrayList = activeEmployeesArrayList;
        if (mChronometerArrayList != null) {
            for (int i = 0; i < mChronometerArrayList.size(); i++){
                mChronometerArrayList.get(i).stop();
            }
            mChronometerArrayList.clear();
            mChronometerArrayList = null;
        }
        mChronometerArrayList = new ArrayList<>();
        Log.d("setEmployeesData", "new chronometer array created");
        notifyDataSetChanged();
    }

    /**
     * Obtains the timeclock id
     *
     * @param position adapter position
     * @return timeclock id
     */
    public int getTimeId(int position) {
        return mActiveEmployeesArrayList.get(position).getTimeID();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (getItemCount() - 1)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_BODY){
            int layoutIdForListItem = R.layout.item_employee;
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            EmployeeViewHolder viewHolder = new EmployeeViewHolder(view);
            return viewHolder;
        } else {
            int layoutIdForListItem = R.layout.item_employee_footer;
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            EmployeeFooterViewHolder viewHolder = new EmployeeFooterViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmployeeViewHolder){
            EmployeeViewHolder employeeViewHolder = (EmployeeViewHolder) holder;
            employeeViewHolder.itemView.setTag(mActiveEmployeesArrayList.get(position).getEmployeeID());
            employeeViewHolder.mEmployeeNameTextView.setText(mActiveEmployeesArrayList.get(position).getEmployeeName());
            String photoUri = mActiveEmployeesArrayList.get(position).getPhotoPath();

            if (photoUri.isEmpty() || photoUri.equals("null")) {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.im_blank_profile)
                        .transform(new CircleTransform(holder.itemView.getContext()))
                        .into(employeeViewHolder.mPhotoImageView);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(photoUri)
                        .transform(new CircleTransform(holder.itemView.getContext()))
                        .into(employeeViewHolder.mPhotoImageView);
            }

            int currentWorked = calculateCurrentWorked(position);
            boolean isOnBreak = mActiveEmployeesArrayList.get(position).getIsOnBreak();

            setChronometers(isOnBreak, currentWorked, employeeViewHolder.mChronometer);
        }
    }

    @Override
    public int getItemCount() {
        if (mActiveEmployeesArrayList != null) {
            return mActiveEmployeesArrayList.size();
        }
        return 0;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FrameLayout mItemFrameLayout;
        ImageView mPhotoImageView;
        TextView mEmployeeNameTextView;
        Chronometer mChronometer;
        public LinearLayout mForegroundLayout;
        public ImageView mLeftClockOutIcon, mRightClockOutIcon;
        public TextView mLeftClockOutTextView, mRightClockOutTextView;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            mItemFrameLayout = itemView.findViewById(R.id.item_active_employees_linear_layout);
            mPhotoImageView = itemView.findViewById(R.id.item_active_photo_image_view);
            mEmployeeNameTextView = itemView.findViewById(R.id.active_employee_text_view);
            mChronometer = itemView.findViewById(R.id.item_chronometer);
            mForegroundLayout = itemView.findViewById(R.id.view_foreground);
            mLeftClockOutIcon = itemView.findViewById(R.id.clock_out_icon_left);
            mRightClockOutIcon = itemView.findViewById(R.id.clock_out_icon_right);
            mLeftClockOutTextView = itemView.findViewById(R.id.clock_out_text_view_left);
            mRightClockOutTextView = itemView.findViewById(R.id.clock_out_text_view_right);
            itemView.setOnClickListener(this);
            mChronometer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.item_chronometer:
                    int timeId = mActiveEmployeesArrayList.get(getAdapterPosition()).getTimeID();
                    int breakId = 0;
                    ArrayList<Break> breaksArrayList = mActiveEmployeesArrayList.get(getAdapterPosition()).getBreaksArrayList();
                    if (breaksArrayList != null) {
                        Break breakObject = breaksArrayList.get(breaksArrayList.size() - 1);
                        breakId = breakObject.getBreakID();
                    }
                    boolean isOnBreak = mActiveEmployeesArrayList.get(getAdapterPosition()).getIsOnBreak();
                    mOnClickHandler.onItemTimerClick(timeId, breakId, isOnBreak);
                    break;
                case R.id.item_active_employees_linear_layout:
                    mOnClickHandler.onItemClick(mActiveEmployeesArrayList.get(getAdapterPosition()).getEmployeeID(), mPhotoImageView);
                    break;
                default:
                    break;
            }

        }
    }

    public class EmployeeFooterViewHolder extends RecyclerView.ViewHolder{

        EmployeeFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * Calculates the time worked in seconds of employee on current active time
     *
     * @param position      current position of array list of active employees
     * @return              hours worked in seconds
     */
    private int calculateCurrentWorked(int position){
        // obtains the breaks if there is any
        ArrayList<Break> breaksArrayList = mActiveEmployeesArrayList.get(position).getBreaksArrayList();
        int currentWorked;
        int totalBreak = 0;
        long currentTime = System.currentTimeMillis() / 1000;
        if (breaksArrayList != null) {
            Log.d("calculateCurrentWorked", "onBindViewHolder: Break array exists");

            int lastBreakIndex = breaksArrayList.size() - 1;
            long lastBreakDuration = 0;
            int lastRun;
            // checks if current employee is on break. Most recent break is the key to find that out.
            if (breaksArrayList.get(lastBreakIndex).getBreakEnd() == 0) {
                Log.d("calculateCurrentWorked", mActiveEmployeesArrayList.get(position).getEmployeeName() + " break true");
                mActiveEmployeesArrayList.get(position).setIsOnBreak(true);
                lastRun = breaksArrayList.size() - 1;
                lastBreakDuration = currentTime - breaksArrayList.get(lastBreakIndex).getBreakStart();
            } else {
                Log.d("calculateCurrentWorked", mActiveEmployeesArrayList.get(position).getEmployeeName() + " break false");
                mActiveEmployeesArrayList.get(position).setIsOnBreak(false);
                lastRun = breaksArrayList.size();
            }

            for (int i = 0; i < lastRun; i++) {
                long breakStart = breaksArrayList.get(i).getBreakStart();
                long breakEnd = breaksArrayList.get(i).getBreakEnd();
                totalBreak += (int) (breakEnd - breakStart);
            }
            totalBreak += lastBreakDuration;
            Log.d("calculateCurrentWorked", String.valueOf(totalBreak));
        }

        Log.d("calculateCurrentWorked", mActiveEmployeesArrayList.get(position).getEmployeeName());
        Log.d("calculateCurrentWorked", "currentTime =" + String.valueOf(currentTime));
        Log.d("calculateCurrentWorked", "clockIn= " + String.valueOf(mActiveEmployeesArrayList.get(position).getClockIn()));

        boolean isOnBreak = mActiveEmployeesArrayList.get(position).getIsOnBreak();
        long clockIn = mActiveEmployeesArrayList.get(position).getClockIn();
        if (isOnBreak){
            Log.d("break start", String.valueOf(breaksArrayList.get(breaksArrayList.size() - 1).getBreakStart()));
            Log.d("clock in", String.valueOf(clockIn));
            Log.d("total break", String.valueOf(totalBreak));
            currentWorked = (int) (currentTime - clockIn - totalBreak);
            Log.d("calculateCurrentWorked", String.valueOf(currentWorked));
        } else {
            currentWorked = (int) (currentTime - clockIn - totalBreak);
            Log.d("calculateCurrentWorked", String.valueOf(currentWorked));
        }
        return currentWorked;
    }

    /**
     * Sets a timer for each active employee in the list
     *
     * @param isOnBreak         boolean value to check if employee is currently on break
     * @param currentWorked     time in seconds of hours worked of employee on the current active time
     * @param chronometer       View to display the timer
     */
    private void setChronometers(boolean isOnBreak, final int currentWorked, Chronometer chronometer){
        long currentWorkedMillis = currentWorked * 1000;

        if (isOnBreak) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                chronometer.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_chronometer_clock_in, null));
            }
            Log.d("setChronometers", "RED");
            int h = currentWorked / 3600;
            int m = (currentWorked % 3600) / 60;
            chronometer.setText(String.format("%02d:%02d", h, m));
            chronometer.setTextColor(ContextCompat.getColor(mContext, R.color.inactive_timer));
            //chronometer.getRootView().findViewById(R.id.ripple_view).setBackground(Color.RED);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                chronometer.setBackground(mContext.getResources().getDrawable(R.drawable.ripple_chronometer_clock_out, null));
            }
            Log.d("setChronometers", "GREEN");
            Log.d("setChronometers", "system time elapsed: " + String.valueOf(SystemClock.elapsedRealtime()));
            Log.d("setChronometers", "current in millis: " + String.valueOf(currentWorkedMillis));
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    int h   = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;

                    Log.d("onChronometerTick", "elapsed = " + String.valueOf(SystemClock.elapsedRealtime()));
                    Log.d("onChronometerTick", "base = " + String.valueOf( chronometer.getBase()));
                    Log.d("onChronometerTick", "elaspsed - base = " + String.valueOf(time));
                    Log.d("onChronometerTick", "hours = " + String.valueOf(h));
                    Log.d("onChronometerTick", "minutes = " + String.valueOf(m));
                    Log.d("onChronometerTick", "--------------------------");

                    chronometer.setText(String.format("%02d:%02d", h, m));
                }
            });
            chronometer.setTextColor(ContextCompat.getColor(mContext, R.color.active_timer));
            chronometer.setBase(SystemClock.elapsedRealtime() - currentWorkedMillis);
            chronometer.start();
            mChronometerArrayList.add(chronometer);
        }
    }

    public interface OnClickHandler {
        void onItemClick(int employeeId, View photoImageView);

        /**
         * Handles action when timer is clicked
         *
         * @param timeId    id of active time
         * @param breakId   id of the most recent break
         * @param isOnBreak differentiates between start break and end break
         */
        void onItemTimerClick(int timeId, int breakId, boolean isOnBreak);
    }

}
