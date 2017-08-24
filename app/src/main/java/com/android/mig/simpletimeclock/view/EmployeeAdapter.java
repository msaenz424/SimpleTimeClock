package com.android.mig.simpletimeclock.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.model.Employee;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    ArrayList<Employee> mEmployeesArrayList = new ArrayList<>();

    public void setEmployeesData(ArrayList<Employee> employeesData){
        mEmployeesArrayList = employeesData;
        notifyDataSetChanged();
    }

    public void addNewEmployeeToArrayList(int empID, String empName){
        mEmployeesArrayList.add(new Employee(empID, empName));
        notifyDataSetChanged();
    }

    public void deleteEmployee(int position){
        mEmployeesArrayList.remove(position);
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
        holder.itemView.setTag(mEmployeesArrayList.get(position).getEmployeeID());
        holder.tvEmployee.setText(mEmployeesArrayList.get(position).getEmployeeName());
    }



    @Override
    public int getItemCount() {
        return mEmployeesArrayList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployee;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            tvEmployee = (TextView) itemView.findViewById(R.id.tv_employee);
        }
    }

}
