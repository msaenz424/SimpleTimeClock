package com.android.mig.simpletimeclock.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private static final int EMPLOYEE_COL_ID_INDEX = 0;
    private static final int EMPLOYEE_COL_NAME_INDEX = 1;

    Cursor mEmployeesCursor = null;

    public void setEmployeesData(Cursor employeesData){
        mEmployeesCursor = employeesData;
        notifyDataSetChanged();
    }

    public void addNewEmployeeToArrayList(int empID, String empName){
        //mEmployeesCursor.add(new Employee(empID, empName));
        notifyDataSetChanged();
    }

    public void deleteEmployee(int position){
        //mEmployeesArrayList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_employee;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        EmployeeViewHolder viewHolder = new EmployeeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        mEmployeesCursor.moveToPosition(position);
        holder.itemView.setTag(mEmployeesCursor.getString(EMPLOYEE_COL_ID_INDEX));
        holder.tvEmployee.setText(mEmployeesCursor.getString(EMPLOYEE_COL_NAME_INDEX));
    }

    @Override
    public int getItemCount() {
        if(mEmployeesCursor != null){
            return mEmployeesCursor.getCount();
        } else {
            return 0;
        }
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployee;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            tvEmployee = (TextView) itemView.findViewById(R.id.tv_employee);
        }
    }

}
