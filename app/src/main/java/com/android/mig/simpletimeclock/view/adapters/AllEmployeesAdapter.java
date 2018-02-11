package com.android.mig.simpletimeclock.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.utils.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AllEmployeesAdapter extends RecyclerView.Adapter<AllEmployeesAdapter.AllEmployeesViewHolder> {

    private static final int EMPLOYEE_COL_ID_INDEX = 0;
    private static final int EMPLOYEE_COL_NAME_INDEX = 1;
    private static final int EMPLOYEE_COL_PHOTO_INDEX = 3;

    private Context mContext;
    private Cursor mAllEmployeesCursor = null;
    private OnListTapHandler mOnTapHandler;
    private ArrayList<Integer> mSelectedItems = new ArrayList<>();
    private ArrayList<View> mSelectedCheckViews = new ArrayList<>();

    public AllEmployeesAdapter(Context context, OnListTapHandler onTapHandler) {
        this.mContext = context;
        this.mOnTapHandler = onTapHandler;
    }

    public void setAllEmployeesData(Cursor employeesData) {
        this.mAllEmployeesCursor = employeesData;
        notifyDataSetChanged();
    }

    public Integer[] getEmployeesIds() {
        Integer[] ids = mSelectedItems.toArray(new Integer[mSelectedItems.size()]);
        return ids;
    }

    public void clearSelection() {
        for (View view: mSelectedCheckViews){
            view.setVisibility(View.GONE);
        }
        mSelectedItems.clear();
        mSelectedCheckViews.clear();
    }

    @Override
    public AllEmployeesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_all_employees, parent, false);

        return new AllEmployeesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllEmployeesViewHolder holder, int position) {
        mAllEmployeesCursor.moveToPosition(position);
        holder.mItemLinearLayout.setBackgroundColor(Color.WHITE);
        holder.itemView.setTag(mAllEmployeesCursor.getString(EMPLOYEE_COL_ID_INDEX));
        holder.mNameTextView.setText(mAllEmployeesCursor.getString(EMPLOYEE_COL_NAME_INDEX));
        String photoUri = mAllEmployeesCursor.getString(EMPLOYEE_COL_PHOTO_INDEX);

        if (photoUri.isEmpty() || photoUri.equals("null")) {
            Glide.with(mContext)
                    .load(R.drawable.im_blank_profile)
                    .transform(new CircleTransform(mContext))
                    .into(holder.mPhotoImageView);
        } else {
            Glide.with(mContext)
                    .load(photoUri)
                    .transform(new CircleTransform(mContext))
                    .into(holder.mPhotoImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mAllEmployeesCursor != null) {
            return mAllEmployeesCursor.getCount();
        }
        return 0;
    }

    class AllEmployeesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        RelativeLayout mCheckIconRelativeLayout;
        LinearLayout mItemLinearLayout;
        ImageView mPhotoImageView;
        TextView mNameTextView;
        Animation mScaleUpAnimation, mScaleDownAnimation;

        AllEmployeesViewHolder(View itemView) {
            super(itemView);
            mCheckIconRelativeLayout = itemView.findViewById(R.id.check_icon_relative_layout);
            mItemLinearLayout = itemView.findViewById(R.id.item_all_employees_linear_layout);
            mPhotoImageView = itemView.findViewById(R.id.item_all_photo_image_view);
            mNameTextView = itemView.findViewById(R.id.employee_name_text_view);
            mScaleUpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_up);
            mScaleDownAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_down);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // if Selection exists
            if (!mSelectedItems.isEmpty()) {
                onLongClick(view);
            } else {
                int empId = Integer.valueOf(view.getTag().toString());
                mOnTapHandler.onClick(empId, mPhotoImageView);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            mOnTapHandler.onItemLongTap();
            int id = Integer.parseInt(view.getTag().toString());
            selectItem(id);
            return true;
        }

        void selectItem(int item) {
            if (mSelectedItems.contains(item)) {
                mSelectedItems.remove(Integer.valueOf(item));
                mItemLinearLayout.setBackgroundColor(Color.WHITE);
                mCheckIconRelativeLayout.startAnimation(mScaleDownAnimation);
                mCheckIconRelativeLayout.setVisibility(View.GONE);
                mSelectedCheckViews.remove(mCheckIconRelativeLayout);
                if (mSelectedItems.isEmpty()) {
                    mOnTapHandler.onLastSelectionItemRemoved();
                }
            } else {
                mSelectedItems.add(item);
                mItemLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.action_mode_item_selected));
                mCheckIconRelativeLayout.startAnimation(mScaleUpAnimation);
                mCheckIconRelativeLayout.setVisibility(View.VISIBLE);
                mSelectedCheckViews.add(mCheckIconRelativeLayout);
            }
        }
    }

    public interface OnListTapHandler {

        /**
         * Triggers a response when an item is tapped
         */
        void onItemLongTap();

        /**
         * Triggers a response when the last item of the
         * Selection list is removed.
         */
        void onLastSelectionItemRemoved();

        void onClick(int empId, View photoImageView);
    }
}