package com.android.mig.simpletimeclock.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.view.utils.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private static final int EMPLOYEE_COL_TIME_ID_INDEX = 0;
    private static final int EMPLOYEE_COL_ID_INDEX = 1;
    private static final int EMPLOYEE_COL_NAME_INDEX = 2;
    private static final int EMPLOYEE_COL_PHOTO_INDEX = 3;
    private static final int EMPLOYEE_COL_CLOCK_IN_INDEX = 4;
    private static final int EMPLOYEE_COL_BREAK_START_INDEX = 5;
    private static final int EMPLOYEE_COL_BREAK_END_INDEX = 6;
    private static final int TIMER_START_DELAY = 0;
    private static final int TIMER_UPDATE_FREQUENCY = 60000;

    private static final int BREAK_START_CODE = 1;
    private static final int BREAK_END_CODE = 2;

    private final OnClickHandler mOnClickHandler;
    private Context mContext;

    private Cursor mEmployeesCursor = null;
    private ArrayList<Timer> mTimerArrayList;

    public EmployeeAdapter(OnClickHandler onClickHandler) {
        this.mOnClickHandler = onClickHandler;
        this.mContext = (Context) onClickHandler;

    }

    public void setEmployeesData(Cursor employeesData) {
        this.mEmployeesCursor = employeesData;
        if (mTimerArrayList != null) {
            for (int i = 0; i < mTimerArrayList.size(); i++){
                mTimerArrayList.get(i).cancel();
                Log.d("setData", "cancelling array item at position " + i);
            }
            mTimerArrayList.clear();
            mTimerArrayList = null;
        }
        mTimerArrayList = new ArrayList<>();
        Log.d("setData", "new array created");
        notifyDataSetChanged();
    }

    /**
     * Obtains the timeclock id
     *
     * @param position adapter position
     * @return timeclock id
     */
    public int getTimeId(int position) {
        mEmployeesCursor.moveToPosition(position);
        return mEmployeesCursor.getInt(0);
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_employee;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        EmployeeViewHolder viewHolder = new EmployeeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EmployeeViewHolder holder, int position) {
        mEmployeesCursor.moveToPosition(position);
        holder.itemView.setTag(mEmployeesCursor.getString(EMPLOYEE_COL_ID_INDEX));
        holder.mEmployeeNameTextView.setText(mEmployeesCursor.getString(EMPLOYEE_COL_NAME_INDEX));
        String photoUri = mEmployeesCursor.getString(EMPLOYEE_COL_PHOTO_INDEX);

        if (photoUri.isEmpty() || photoUri.equals("null")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.im_blank_profile)
                    .transform(new CircleTransform(holder.itemView.getContext()))
                    .into(holder.mPhotoImageView);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(photoUri)
                    .transform(new CircleTransform(holder.itemView.getContext()))
                    .into(holder.mPhotoImageView);
        }

        final long clockIn = mEmployeesCursor.getInt(EMPLOYEE_COL_CLOCK_IN_INDEX);
        final long breakStart = mEmployeesCursor.getInt(EMPLOYEE_COL_BREAK_START_INDEX);
        final long breakEnd = mEmployeesCursor.getInt(EMPLOYEE_COL_BREAK_END_INDEX);

        setTimers(breakStart, breakEnd, clockIn, holder.mTimerTextView);
    }

    @Override
    public int getItemCount() {
        if (mEmployeesCursor != null) {
            return mEmployeesCursor.getCount();
        } else {
            return 0;
        }
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FrameLayout mItemFrameLayout;
        ImageView mPhotoImageView;
        TextView mEmployeeNameTextView, mTimerTextView;
        public LinearLayout mForegroundLayout;
        public ImageView mLeftClockOutIcon, mRightClockOutIcon;
        public TextView mLeftClockOutTextView, mRightClockOutTextView;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            mItemFrameLayout = itemView.findViewById(R.id.item_active_employees_linear_layout);
            mPhotoImageView = itemView.findViewById(R.id.item_active_photo_image_view);
            mEmployeeNameTextView = itemView.findViewById(R.id.active_employee_text_view);
            mForegroundLayout = itemView.findViewById(R.id.view_foreground);
            mLeftClockOutIcon = itemView.findViewById(R.id.clock_out_icon_left);
            mRightClockOutIcon = itemView.findViewById(R.id.clock_out_icon_right);
            mLeftClockOutTextView = itemView.findViewById(R.id.clock_out_text_view_left);
            mRightClockOutTextView = itemView.findViewById(R.id.clock_out_text_view_right);
            mTimerTextView = itemView.findViewById(R.id.item_timer_text_view);
            itemView.setOnClickListener(this);
            mTimerTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mEmployeesCursor.moveToPosition(getAdapterPosition());
            switch (view.getId()) {
                case R.id.item_timer_text_view:
                    long start = mEmployeesCursor.getInt(EMPLOYEE_COL_BREAK_START_INDEX);
                    long end = mEmployeesCursor.getInt(EMPLOYEE_COL_BREAK_END_INDEX);
                    int empId = mEmployeesCursor.getInt(EMPLOYEE_COL_TIME_ID_INDEX);
                    if (start == 0){
                        Log.d("ADAPTER", String.valueOf(BREAK_START_CODE));
                        mOnClickHandler.onItemTimerClick(empId, BREAK_START_CODE);
                    } else {
                        if (end == 0) {
                            Log.d("ADAPTER", String.valueOf(BREAK_END_CODE));
                            mOnClickHandler.onItemTimerClick(empId, BREAK_END_CODE);
                        }
                    }
                    break;
                case R.id.item_active_employees_linear_layout:
                    mOnClickHandler.onItemClick(mEmployeesCursor.getInt(EMPLOYEE_COL_ID_INDEX), mPhotoImageView);
                    break;
                default:
                    break;
            }

        }
    }

    private void setTimers(final long breakStart, final long breakEnd, final long clockIn, final TextView timerTextView){
        // updates the timer every 60 seconds
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            long time;
                            // if employee in currently on break
                            if (breakStart != 0 && breakEnd == 0) {
                                time = breakStart - clockIn;
                                timerTextView.setTextColor(ContextCompat.getColor(mContext, R.color.inactive_timer));
                                Log.d("run", "RED");
                            } else {
                                timerTextView.setTextColor(ContextCompat.getColor(mContext, R.color.active_timer));
                                Log.d("run", "GREEN");
                                long currentTime = System.currentTimeMillis() / 1000;
                                int breakTime = 0;
                                // if breaking has finished
                                if (breakEnd != 0 && breakStart != 0) {
                                    breakTime = (int) (breakEnd - breakStart);
                                }
                                time = currentTime - clockIn - breakTime;
                            }

                            long hours = time / 3600;
                            long minutes = (time % 3600) / 60;
                            String result = String.format("%02d:%02d", hours, minutes);
                            timerTextView.setText(result);
                        } catch (Exception e) {
                            Log.e("Exception", String.valueOf(e));
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, TIMER_START_DELAY, TIMER_UPDATE_FREQUENCY);
        mTimerArrayList.add(timer);
        Log.d("TIMER ARRAY", String.valueOf(mTimerArrayList.size()));
        Log.d("SETTIMERS", "set and added timer to array list");
    }

    public interface OnClickHandler {
        void onItemClick(int employeeId, View photoImageView);

        /**
         * Handles action when timer is clicked
         *
         * @param employeeId id of employee
         * @param actionCode type of action (start break or end break)
         */
        boolean onItemTimerClick(int employeeId, int actionCode);
    }

}
