package com.android.mig.simpletimeclock.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.view.utils.CircleTransform;
import com.bumptech.glide.Glide;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private static final int EMPLOYEE_COL_ID_INDEX = 1;
    private static final int EMPLOYEE_COL_NAME_INDEX = 2;
    private static final int EMPLOYEE_COL_PHOTO_INDEX = 3;

    private final OnClickHandler mOnClickHandler;

    private Cursor mEmployeesCursor = null;

    public EmployeeAdapter(OnClickHandler onClickHandler) {
        this.mOnClickHandler = onClickHandler;
    }

    public void setEmployeesData(Cursor employeesData){
        this.mEmployeesCursor = employeesData;
        notifyDataSetChanged();
    }

    /**
     * Obtains the timeclock id and the employee id
     *
     * @param position adapter position
     * @return both ids
     */
    public Integer[] getItemIds(int position){
        Integer[] ids = new Integer[2];
        mEmployeesCursor.moveToPosition(position);
        ids[0] = mEmployeesCursor.getInt(0);
        ids[1] = mEmployeesCursor.getInt(1);
        return ids;
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
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        mEmployeesCursor.moveToPosition(position);
        holder.itemView.setTag(mEmployeesCursor.getString(EMPLOYEE_COL_ID_INDEX));
        holder.mEmployeeNameTextView.setText(mEmployeesCursor.getString(EMPLOYEE_COL_NAME_INDEX));
        String photoUri = mEmployeesCursor.getString(EMPLOYEE_COL_PHOTO_INDEX);

        if (photoUri.isEmpty() || photoUri.equals("null")){
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
    }

    @Override
    public int getItemCount() {
        if(mEmployeesCursor != null){
            return mEmployeesCursor.getCount();
        } else {
            return 0;
        }
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout mItemLinearLayout;
        ImageView mPhotoImageView;
        TextView mEmployeeNameTextView;
        public LinearLayout mForegroundLayout;
        public ImageView mLeftClockOutIcon, mRightClockOutIcon;
        public TextView mLeftClockOutTextView, mRightClockOutTextView;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            mItemLinearLayout = itemView.findViewById(R.id.item_all_employees_linear_layout);
            mPhotoImageView = itemView.findViewById(R.id.item_active_photo_image_view);
            mEmployeeNameTextView = itemView.findViewById(R.id.active_employee_text_view);
            mForegroundLayout = itemView.findViewById(R.id.view_foreground);
            mLeftClockOutIcon = itemView.findViewById(R.id.clock_out_icon_left);
            mRightClockOutIcon = itemView.findViewById(R.id.clock_out_icon_right);
            mLeftClockOutTextView = itemView.findViewById(R.id.clock_out_text_view_left);
            mRightClockOutTextView = itemView.findViewById(R.id.clock_out_text_view_right);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mEmployeesCursor.moveToPosition(getAdapterPosition());
            mOnClickHandler.onItemClick(mEmployeesCursor.getInt(EMPLOYEE_COL_ID_INDEX), mPhotoImageView);
        }
    }

    public interface OnClickHandler{
        void onItemClick(int employeeId, View photoImageView);
    }

}
