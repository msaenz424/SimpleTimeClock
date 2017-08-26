package com.android.mig.simpletimeclock.view.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;

public class AllEmployeesAdapter extends RecyclerView.Adapter<AllEmployeesAdapter.AllEmployeesViewHolder>{

    private static final int EMPLOYEE_COL_ID_INDEX = 0;
    private static final int EMPLOYEE_COL_NAME_INDEX = 1;

    Cursor mAllEmployeesCursor = null;

    public void setAllEmployeesData(Cursor employeesData){
        mAllEmployeesCursor = employeesData;
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
        return 0;
    }

    public class AllEmployeesViewHolder extends RecyclerView.ViewHolder {

        TextView mNameTextView;

        public AllEmployeesViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.employee_name_text_view);
        }
    }
}
