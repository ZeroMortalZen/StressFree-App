package com.linx.stress_free_app;

import androidx.fragment.app.Fragment;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.stress_free_app.AppUsageController.AppUsageAdapter;
import com.linx.stress_free_app.AppUsageController.AppUsageAsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayAppUsageFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppUsageAdapter adapter;
    private AppUsageAsyncTask appUsageAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_app_usage, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AppUsageAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        appUsageAsyncTask = new AppUsageAsyncTask(getActivity(), recyclerView, adapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appUsageAsyncTask.execute();
    }
}

