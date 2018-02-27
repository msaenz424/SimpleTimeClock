package com.android.mig.simpletimeclock.view.ui;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenter;
import com.android.mig.simpletimeclock.source.model.EmployeeDetails;
import com.android.mig.simpletimeclock.view.fragments.AddEmployeeDialogFragment;
import com.android.mig.simpletimeclock.view.fragments.EmployeeDetailsFragment;

public class EmployeeDetailsActivity extends AppCompatActivity
        implements AddEmployeeDialogFragment.NoticeDialogListener, AddEmployeeDialogFragment.PhotoPickerListener{

    public static final String EMP_NAME_TAG = "emp_name";
    public static final String EMP_WAGE_TAG = "emp_wage";
    public static final String EMP_PHOTOPATH_TAG = "emp_photo_path";
    private final static String DIALOG_TAG = AddEmployeeDialogFragment.class.getName();

    private EmployeeDetailsFragment mEmployeeDetailsFragment;

    private String mPhotoPath;
    private EmployeeDetails mEmployeeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        if (savedInstanceState == null){
            mEmployeeDetailsFragment = new EmployeeDetailsFragment();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.employee_detail_container, mEmployeeDetailsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_employee_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_item_edit:
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragment dialogFragment = new AddEmployeeDialogFragment();
                // passes the employees info to dialog for displaying purposes
                Bundle bundle = new Bundle();
                bundle.putString(EMP_NAME_TAG, String.valueOf(mEmployeeDetails.getName()));
                bundle.putDouble(EMP_WAGE_TAG, mEmployeeDetails.getWage());
                bundle.putString(EMP_PHOTOPATH_TAG, String.valueOf(mEmployeeDetails.getPhotoPath()));
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, DIALOG_TAG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        final AppBarLayout mAppBarLayout = findViewById(R.id.det_appbar_layout);

        int scrollValue = 0;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int appbarHeight = mAppBarLayout.getHeight();

        // if height of AppBar if bigger than screen's height then the scrolling will be slightly more extended
        if (appbarHeight >= screenHeight){
            scrollValue = getResources().getInteger(R.integer.det_scroll_height_value);
        }

        // generates an up-scrolling animation (instructive motion) when activity just starts
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        final AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null && scrollValue != 0) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt();
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    behavior.setTopAndBottomOffset((Integer) animation.getAnimatedValue());
                    mAppBarLayout.requestLayout();
                }
            });
            valueAnimator.setIntValues(0, scrollValue);
            valueAnimator.setDuration(getResources().getInteger(R.integer.det_scroll_duration));
            valueAnimator.setStartDelay(getResources().getInteger(R.integer.det_scroll_delay));
            valueAnimator.start();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogView = dialog.getDialog();
        EditText nameEditText = dialogView.findViewById(R.id.name_edit_text);
        EditText wageEditText = dialogView.findViewById(R.id.wage_edit_text);
        String name = String.valueOf(nameEditText.getText());
        double wage = Double.parseDouble(String.valueOf(wageEditText.getText()));

        EmployeeDetailsPresenter employeeDetailsPresenter = mEmployeeDetailsFragment.getPresenter();
        employeeDetailsPresenter.onActionEditClicked(mEmployeeDetails.getID(), name, wage, mPhotoPath);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onPhotoTaken(String photoPath) {
        this.mPhotoPath = photoPath;
    }

    public void setEmployeeDetails(EmployeeDetails employeeDetails){
        this.mEmployeeDetails = employeeDetails;
    }
}