package com.android.mig.simpletimeclock.view.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;

import java.util.ArrayList;

public class AllEmployeesAdapter extends RecyclerView.Adapter<AllEmployeesAdapter.AllEmployeesViewHolder> {

    private static final int EMPLOYEE_COL_ID_INDEX = 0;
    private static final int EMPLOYEE_COL_NAME_INDEX = 1;

    private Cursor mAllEmployeesCursor = null;
    private OnListTapHandler mOnTapHandler;
    private ArrayList<Integer> mSelectedItems = new ArrayList<>();

    public AllEmployeesAdapter(OnListTapHandler onTapHandler) {
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

    public void clearSelection(){
        mSelectedItems.clear();
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

        LinearLayout mItemLinearLayout;
        TextView mNameTextView;

        AllEmployeesViewHolder(View itemView) {
            super(itemView);
            mItemLinearLayout = itemView.findViewById(R.id.item_all_employees_linear_layout);
            mNameTextView = itemView.findViewById(R.id.employee_name_text_view);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // if Selection exists
            if (!mSelectedItems.isEmpty()){
                onLongClick(view);
            } else {
                int empId = Integer.valueOf(view.getTag().toString());
                mOnTapHandler.onClick(empId);
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
                if (mSelectedItems.isEmpty()) {
                    mOnTapHandler.onLastSelectionItemRemoved();
                }
            } else {
                mSelectedItems.add(item);
                mItemLinearLayout.setBackgroundColor(Color.LTGRAY);
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

        void onClick(int empId);
    }
}