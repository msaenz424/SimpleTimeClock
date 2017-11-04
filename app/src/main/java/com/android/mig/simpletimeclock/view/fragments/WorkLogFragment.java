package com.android.mig.simpletimeclock.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.source.model.Timeclock;
import com.android.mig.simpletimeclock.view.adapters.WorkLogAdapter;

import java.util.ArrayList;

public class WorkLogFragment extends Fragment {

    private ArrayList<Timeclock> mTimeclockArrayList;

    private RecyclerView mWorkLogRecyclerView;

    public WorkLogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work_log, container, false);

        mWorkLogRecyclerView = rootView.findViewById(R.id.work_log_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mWorkLogRecyclerView.setLayoutManager(layoutManager);
        mWorkLogRecyclerView.hasFixedSize();

        mTimeclockArrayList = getArguments().getParcelableArrayList(Intent.EXTRA_TEXT);
        WorkLogAdapter workLogAdapter = new WorkLogAdapter(getContext());
        mWorkLogRecyclerView.setAdapter(workLogAdapter);
        workLogAdapter.setWorkLogData(mTimeclockArrayList);

        return rootView;
    }
}
