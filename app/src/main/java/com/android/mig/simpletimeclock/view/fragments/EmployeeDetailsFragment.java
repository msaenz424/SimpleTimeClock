package com.android.mig.simpletimeclock.view.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenter;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenterImpl;
import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.view.EmployeeDetailsView;

import java.util.Locale;

public class EmployeeDetailsFragment extends Fragment implements EmployeeDetailsView{

    EmployeeDetailsPresenter mEmployeeDetailsPresenter;

    View mRootView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mStatusImageView;
    private TextView mWageTextView;
    private TextView mUnpaidHoursTextView;
    private TextView mUnpaidEarningsTextView;
    private TextView mTotalHoursTextView;
    private TextView mTotalEarningsTextView;
    private Button mPayButton;

    int mEmployeeId;
    String mUnpaidEarnings;

    public EmployeeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_employee_detail, container, false);

        final Toolbar toolbar = mRootView.findViewById(R.id.det_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmployeeId = getActivity().getIntent().getIntExtra(Intent.EXTRA_UID, 0);
        mEmployeeDetailsPresenter = new EmployeeDetailsPresenterImpl(this, getContext());

        mCollapsingToolbarLayout = mRootView.findViewById(R.id.det_collapsing_layout);
        mStatusImageView = mRootView.findViewById(R.id.time_status_image_view);
        mWageTextView = mRootView.findViewById(R.id.det_wage_text_view);
        mUnpaidHoursTextView = mRootView.findViewById(R.id.det_unpaid_hours_text_view);
        mUnpaidEarningsTextView = mRootView.findViewById(R.id.det_unpaid_earnings_text_view);
        mTotalHoursTextView = mRootView.findViewById(R.id.det_total_hours_text_view);
        mTotalEarningsTextView = mRootView.findViewById(R.id.det_total_earnings_text_view);
        mPayButton = mRootView.findViewById(R.id.det_pay_button);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mEmployeeDetailsPresenter.onPayButtonClicked(mEmployeeId);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Pay " + getResources().getString(R.string.dollar_currency_symbol) + mUnpaidEarnings + "?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        mEmployeeDetailsPresenter.onResume(mEmployeeId);

        return mRootView;
    }

    @Override
    public void showEmployeeInfo(EmployeeDetails employeeDetails) {

        mCollapsingToolbarLayout.setTitle(employeeDetails.getName());
        mWageTextView.setText(getResources().getString(R.string.dollar_currency_symbol) + String.valueOf(employeeDetails.getWage()));
        int unpaidInSeconds = (int) employeeDetails.getUnpaidTimeWorked();
        int hours = unpaidInSeconds / 3600;
        int minutes = (unpaidInSeconds % 3600) / 60;
        mUnpaidHoursTextView.setText(hours + " hours " + minutes + " minutes");
        mUnpaidEarnings = String.format(Locale.US, "%.2f", employeeDetails.getUnpaidEarnings());
        mUnpaidEarningsTextView.setText(getResources().getString(R.string.dollar_currency_symbol) + mUnpaidEarnings);
        int totalInSeconds = (int) employeeDetails.getTotalTimeWorked();
        int totalHours = totalInSeconds / 3600;
        int totalMinutes = (totalInSeconds % 3600) / 60;
        mTotalHoursTextView.setText(totalHours + " hours " + totalMinutes + " minutes");
        String totalEarnings = String.format(Locale.US, "%.2f", employeeDetails.getTotalEarnings());
        mTotalEarningsTextView.setText(getResources().getString(R.string.dollar_currency_symbol) + totalEarnings);
        if (employeeDetails.getIsWorking()){
            mStatusImageView.setImageResource(R.drawable.im_green_light);
            mPayButton.setVisibility(View.INVISIBLE);
        } else {
            mStatusImageView.setImageResource(R.drawable.im_grey_light);
            mPayButton.setVisibility(View.VISIBLE);
            if (unpaidInSeconds == 0) {
                mPayButton.setEnabled(false);
            }
        }
    }

    @Override
    public void showPaidMessage() {
        Toast.makeText(getContext(),
                getResources().getString(R.string.dollar_currency_symbol) + mUnpaidEarnings + " " + getResources().getString(R.string.paid_earnings_message),
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void refreshEmployeeInfo() {
        mEmployeeDetailsPresenter.onResume(mEmployeeId);
    }
}
