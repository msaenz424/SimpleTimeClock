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

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenter;
import com.android.mig.simpletimeclock.presenter.EmployeeDetailsPresenterImpl;
import com.android.mig.simpletimeclock.view.EmployeeDetailsView;

public class EmployeeDetailsFragment extends Fragment implements EmployeeDetailsView{

    EmployeeDetailsPresenter mEmployeeDetailsPresenter;

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
        mEmployeeDetailsPresenter = new EmployeeDetailsPresenterImpl(this, getContext());
        mEmployeeDetailsPresenter.onResume(id);

        return rootView;
    }

    @Override
    public void showPeriodicData() {

    }
}
