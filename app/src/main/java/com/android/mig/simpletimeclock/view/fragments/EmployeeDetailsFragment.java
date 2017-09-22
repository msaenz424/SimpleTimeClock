package com.android.mig.simpletimeclock.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenter;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenterImpl;
import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.view.EmployeeDetailsView;

public class EmployeeDetailsFragment extends Fragment implements EmployeeDetailsView{

    EmployeeDetailsPresenter mEmployeeDetailsPresenter;

    ImageView mStatusImageView;
    TextView mUnpaidHoursTextView;
    TextView mUnpaidEarningsTextView;
    TextView mTotalHoursTextView;
    TextView mTotalEarningsTextView;

    public EmployeeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_employee_detail, container, false);

        final Toolbar toolbar = rootView.findViewById(R.id.det_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getActivity().getIntent().getIntExtra(Intent.EXTRA_UID, 0);
        boolean isActiveTime = getActivity().getIntent().getBooleanExtra(Intent.EXTRA_KEY_EVENT, true);

        mStatusImageView = rootView.findViewById(R.id.time_status_image_view);
        mUnpaidHoursTextView = rootView.findViewById(R.id.det_unpaid_hours_text_view);
        mUnpaidEarningsTextView = rootView.findViewById(R.id.det_unpaid_earnings_text_view);
        mTotalHoursTextView = rootView.findViewById(R.id.det_total_hours_text_view);
        mTotalEarningsTextView = rootView.findViewById(R.id.det_total_earnings_text_view);

        if (isActiveTime){
            mStatusImageView.setImageResource(R.drawable.im_green_light);
        } else {
            mStatusImageView.setImageResource(R.drawable.im_grey_light);
        }

        mEmployeeDetailsPresenter = new EmployeeDetailsPresenterImpl(this, getContext());
        mEmployeeDetailsPresenter.onResume(id);

        return rootView;
    }

    @Override
    public void showEmployeeInfo(EmployeeDetails employeeDetails) {

        int unpaidInSeconds = (int) employeeDetails.getUnpaidTimeWorked();
        int hours = unpaidInSeconds / 3600;
        int minutes = (unpaidInSeconds % 3600) / 60;
        mUnpaidHoursTextView.setText(hours + " hours " + minutes + " minutes");
        String unpaidEarnings = Double.toString(employeeDetails.getUnpaidEarnings());
        mUnpaidEarningsTextView.setText(unpaidEarnings);
        int totalInSeconds = (int) employeeDetails.getTotalTimeWorked();
        int totalHours = totalInSeconds / 3600;
        int totalMinutes = (totalInSeconds % 3600) / 60;
        mTotalHoursTextView.setText(totalHours + " hours " + totalMinutes + " minutes");
        String totalEarnings = Double.toString(employeeDetails.getTotalEarnings());
        mTotalEarningsTextView.setText(totalEarnings);
    }
}
