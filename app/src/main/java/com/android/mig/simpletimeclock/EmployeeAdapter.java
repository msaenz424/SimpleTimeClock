package com.android.mig.simpletimeclock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    ArrayList<String> mEmployeesArray = new ArrayList<>();

    public void setEmployeesData(ArrayList employeesData){
        mEmployeesArray = employeesData;
        notifyDataSetChanged();
    }

    public void addNewEmployeeToArrayList(String name){
        mEmployeesArray.add(name);
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
        holder.tvEmployee.setText(mEmployeesArray.get(position));
    }

    @Override
    public int getItemCount() {
        return mEmployeesArray.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder{
        TextView tvEmployee;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            tvEmployee = (TextView) itemView.findViewById(R.id.tv_employee);
        }
    }

}
