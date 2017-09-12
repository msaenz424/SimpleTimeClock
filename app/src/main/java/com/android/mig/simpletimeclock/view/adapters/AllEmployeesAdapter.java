package com.android.mig.simpletimeclock.view.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;

import java.util.ArrayList;

public class AllEmployeesAdapter extends RecyclerView.Adapter<AllEmployeesAdapter.AllEmployeesViewHolder>{

    private static final int EMPLOYEE_COL_ID_INDEX = 0;
    private static final int EMPLOYEE_COL_NAME_INDEX = 1;

    private Cursor mAllEmployeesCursor = null;
    private OnTapHandler mOnTapHangler;
    private ActionMode mActionMode;
    private boolean actionMode = false;
    private ArrayList<Integer> mSelectedItems = new ArrayList<>();
    private ActionMode.Callback mActionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActionMode = mode;
            actionMode = true;
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_all_employees_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            actionMode = false;
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

    public AllEmployeesAdapter(OnTapHandler onTapHandler){
        this.mOnTapHangler = onTapHandler;
    }

    public void setAllEmployeesData(Cursor employeesData){
        this.mAllEmployeesCursor = employeesData;
        notifyDataSetChanged();
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
        holder.itemView.setTag(mAllEmployeesCursor.getString(EMPLOYEE_COL_ID_INDEX));
        holder.mNameTextView.setText(mAllEmployeesCursor.getString(EMPLOYEE_COL_NAME_INDEX));
    }

    @Override
    public int getItemCount() {
        return mAllEmployeesCursor.getCount();
    }

    class AllEmployeesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        LinearLayout mItemLinearLayout;
        TextView mNameTextView;

        public AllEmployeesViewHolder(View itemView) {
            super(itemView);
            mItemLinearLayout = itemView.findViewById(R.id.item_all_employees_linear_layout);
            mNameTextView = (TextView) itemView.findViewById(R.id.employee_name_text_view);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectItem(getAdapterPosition());
            mOnTapHangler.onTap(actionMode);
        }

        @Override
        public boolean onLongClick(View view) {
            if (!actionMode){
                AppCompatActivity activity = (AppCompatActivity) view.getRootView().getContext();
                activity.startSupportActionMode(mActionModeCallbacks);
            }
            selectItem(getAdapterPosition());
            mOnTapHangler.onTap(actionMode);
            return true;
        }

        void selectItem(Integer item) {
            if (actionMode) {
                if (mSelectedItems.contains(item)) {
                    mSelectedItems.remove(item);
                    mItemLinearLayout.setBackgroundColor(Color.WHITE);
                    if (mSelectedItems.isEmpty()){
                        mActionMode.finish();
                        actionMode = false;
                    }
                } else {
                    mSelectedItems.add(item);
                    mItemLinearLayout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }
    }

    public interface OnTapHandler{
        void onTap(boolean actionMode);
    }
}