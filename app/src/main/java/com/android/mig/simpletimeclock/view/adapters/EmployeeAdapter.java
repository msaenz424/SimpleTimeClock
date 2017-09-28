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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private static final int EMPLOYEE_COL_ID_INDEX = 1;
    private static final int EMPLOYEE_COL_NAME_INDEX = 2;
    private static final int EMPLOYEE_COL_PHOTO_INDEX = 3;

    private Context mContext;
    private final OnClickHandler mOnClickHandler;

    private Cursor mEmployeesCursor = null;

    public EmployeeAdapter(Context context, OnClickHandler onClickHandler) {
        this.mContext = context;
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

        if (photoUri.isEmpty()){
            Glide.with(mContext)
                    .load(R.drawable.im_blank_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mPhotoImageView);
        } else {
            Glide.with(mContext)
                    .load(photoUri)
                    .apply(RequestOptions.circleCropTransform())
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

    class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout mItemLinearLayout;
        ImageView mPhotoImageView;
        TextView mEmployeeNameTextView;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            mItemLinearLayout = itemView.findViewById(R.id.item_all_employees_linear_layout);
            mPhotoImageView = itemView.findViewById(R.id.item_photo_image_view);
            mEmployeeNameTextView = itemView.findViewById(R.id.active_employee_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mEmployeesCursor.moveToPosition(getAdapterPosition());
            mOnClickHandler.onItemClick(mEmployeesCursor.getInt(EMPLOYEE_COL_ID_INDEX));
        }
    }

    public interface OnClickHandler{
        void onItemClick(int employeeId);
    }

}
