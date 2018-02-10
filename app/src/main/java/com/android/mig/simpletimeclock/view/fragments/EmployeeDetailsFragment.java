package com.android.mig.simpletimeclock.view.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenter;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenterImpl;
import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.source.model.Timeclock;
import com.android.mig.simpletimeclock.view.EmployeeDetailsView;
import com.android.mig.simpletimeclock.view.ui.EmployeeDetailsActivity;
import com.android.mig.simpletimeclock.view.ui.WorkLogActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EmployeeDetailsFragment extends Fragment
        implements EmployeeDetailsView, DatePickerDialog.OnDateSetListener{

    private static final String INTENT_DATE_START = "date_start";
    private static final String INTENT_DATE_END = "date_end";

    private static final int VIEW_UNPAID_WORKLOG = 1;
    private static final int VIEW_DATE_RANGE_WORKLOG = 2;

    private static final int PICK_START_DATE_CODE = 0;
    private static final int PICK_END_DATE_CODE = 1;
    private static final String DATE_PICKER_DIALOG_TAG = "DatePickerDialog";
    private static final int ONE_DAY_IN_SECONDS = 86400;

    private EmployeeDetailsPresenter mEmployeeDetailsPresenter;

    View mRootView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mPhotoImageView;
    private ImageView mStatusImageView;
    private TextView mWageTextView, mUnpaidHoursTextView, mUnpaidEarningsTextView, mStartDateTextView, mEndDateTextView;
    private LinearLayout mStartDateLinearLayout, mEndDateLinearLayout;
    private Button mViewWorkLogButton, mPayButton, mCustomWorkLogButton;
    private ImageButton mPickStartDateButton, mPickEndDateButton;

    private ArrayList<Timeclock> mTimeclockArrayList;
    int mEmployeeId;
    String mUnpaidEarnings;
    private int mDatePickId;

    private RequestListener<String, GlideDrawable> mRequestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            ActivityCompat.startPostponedEnterTransition(getActivity());
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            ActivityCompat.startPostponedEnterTransition(getActivity());
            return false;
        }
    };

    public EmployeeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_employee_detail, container, false);

        ActivityCompat.postponeEnterTransition(getActivity());

        final Toolbar toolbar = mRootView.findViewById(R.id.det_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmployeeId = getActivity().getIntent().getIntExtra(Intent.EXTRA_UID, 0);
        mEmployeeDetailsPresenter = new EmployeeDetailsPresenterImpl(this, getActivity());

        mCollapsingToolbarLayout    =  mRootView.findViewById(R.id.det_collapsing_layout);
        mPhotoImageView             =  mRootView.findViewById(R.id.det_photo_image_view);
        mStatusImageView            =  mRootView.findViewById(R.id.time_status_image_view);
        mWageTextView               =  mRootView.findViewById(R.id.det_wage_text_view);
        mUnpaidHoursTextView        =  mRootView.findViewById(R.id.det_unpaid_hours_text_view);
        mUnpaidEarningsTextView     =  mRootView.findViewById(R.id.det_unpaid_earnings_text_view);
        mViewWorkLogButton          =  mRootView.findViewById(R.id.det_view_work_log_button);
        mPayButton                  =  mRootView.findViewById(R.id.det_pay_button);
        mStartDateTextView          =  mRootView.findViewById(R.id.start_date_text_view);
        mEndDateTextView            =  mRootView.findViewById(R.id.end_date_text_view);
        mStartDateLinearLayout      =  mRootView.findViewById(R.id.start_date_linear_layout);
        mEndDateLinearLayout        =  mRootView.findViewById(R.id.end_date_linear_layout);
        mPickStartDateButton        =  mRootView.findViewById(R.id.pick_start_date_button);
        mPickEndDateButton          =  mRootView.findViewById(R.id.pick_end_date_button);
        mCustomWorkLogButton        =  mRootView.findViewById(R.id.det_custom_work_log_button);

        mViewWorkLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WorkLogActivity.class);
                intent.putExtra(Intent.EXTRA_UID, mEmployeeId);
                intent.putExtra(Intent.EXTRA_TEXT, VIEW_UNPAID_WORKLOG);
                //intent.putParcelableArrayListExtra(Intent.EXTRA_TEXT, mTimeclockArrayList);
                startActivity(intent);
            }
        });

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

        mPickStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickId = PICK_START_DATE_CODE;
                openDatePicker();
            }
        });

        mPickEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickId = PICK_END_DATE_CODE;
                openDatePicker();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStartDateLinearLayout.setBackground(getResources().getDrawable(R.drawable.ripple_date_text_view, null));
            mEndDateLinearLayout.setBackground(getResources().getDrawable(R.drawable.ripple_date_text_view, null));
        }
        mStartDateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickStartDateButton.callOnClick();
            }
        });

        mEndDateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickEndDateButton.callOnClick();
            }
        });

        mCustomWorkLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDateString = mStartDateTextView.getText().toString();
                String endDateString = mEndDateTextView.getText().toString();
                if (!startDateString.equals(getResources().getString(R.string.default_date_text)) && !endDateString.equals(getResources().getString(R.string.default_date_text))){
                    long startDate = convertDateToSeconds(startDateString);
                    long endDate = convertDateToSeconds(endDateString) + ONE_DAY_IN_SECONDS; // ensures day picked is included

                    Intent intent = new Intent(getActivity(), WorkLogActivity.class);
                    intent.putExtra(Intent.EXTRA_UID, mEmployeeId);
                    intent.putExtra(Intent.EXTRA_TEXT, VIEW_DATE_RANGE_WORKLOG);
                    intent.putExtra(INTENT_DATE_START, startDate);
                    intent.putExtra(INTENT_DATE_END, endDate);
                    startActivity(intent);
                } else {
                    Snackbar mySnackbar = Snackbar.make(mRootView, R.string.message_invalid_date_range, Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                }
            }
        });

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEmployeeDetailsPresenter.onResume(mEmployeeId);
    }

    @Override
    public void showEmployeeInfo(EmployeeDetails employeeDetails) {

        mCollapsingToolbarLayout.setTitle(employeeDetails.getName());
        String photoUri = employeeDetails.getPhotoPath();

        if (photoUri != null && !photoUri.equals("null") && !photoUri.isEmpty()){
            Glide.with(getContext())
                    .load(photoUri)
                    .listener(mRequestListener)
                    .into(mPhotoImageView);
        } else {
            ActivityCompat.startPostponedEnterTransition(getActivity());
        }

        mWageTextView.setText(getResources().getString(R.string.dollar_currency_symbol) + String.valueOf(employeeDetails.getWage()));
        int unpaidInMinutes = (int) employeeDetails.getUnpaidTimeWorked();
        int hours = unpaidInMinutes / 60;
        int minutes = unpaidInMinutes % 60;
        mUnpaidHoursTextView.setText(hours + "h " + minutes + "m");
        mUnpaidEarnings = String.format(Locale.US, "%.2f", employeeDetails.getUnpaidEarnings());
        mUnpaidEarningsTextView.setText(getResources().getString(R.string.dollar_currency_symbol) + mUnpaidEarnings);
        if (employeeDetails.getIsWorking()){
            mStatusImageView.setImageResource(R.drawable.im_green_light);
            mPayButton.setVisibility(View.GONE);
        } else {
            mStatusImageView.setImageResource(R.drawable.im_grey_light);
            mPayButton.setVisibility(View.VISIBLE);
            if (unpaidInMinutes == 0) {
                mPayButton.setEnabled(false);
            }
        }
        EmployeeDetailsActivity employeeDetailsActivity = (EmployeeDetailsActivity) getActivity();
        employeeDetailsActivity.setEmployeeDetails(new EmployeeDetails(mEmployeeId, employeeDetails.getName(), employeeDetails.getWage(), employeeDetails.getPhotoPath()));
    }

    @Override
    public void saveWorkLogInfo(ArrayList<Timeclock> timeclockArrayList) {
        this.mTimeclockArrayList = timeclockArrayList;
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

    public EmployeeDetailsPresenter getPresenter(){
        return mEmployeeDetailsPresenter;
    }

    private void openDatePicker(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(),DATE_PICKER_DIALOG_TAG);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String stringDate = String.valueOf(monthOfYear + 1) + "/" +  String.valueOf(dayOfMonth) + "/" +  String.valueOf(year);
        switch (mDatePickId){
            case PICK_START_DATE_CODE:
                mStartDateTextView.setText(stringDate);
                break;
            case PICK_END_DATE_CODE:
                mEndDateTextView.setText(stringDate);
                break;
        }
    }

    private long convertDateToSeconds(String stringDate){
        if (stringDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date date = null;
            try {
                date = dateFormat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long seconds = 0;
            if (date != null) {
                seconds = date.getTime() / 1000;
            }
            return seconds;
        } else {
            return 0;
        }
    }
}
