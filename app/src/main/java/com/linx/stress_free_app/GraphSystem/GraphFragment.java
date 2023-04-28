package com.linx.stress_free_app.GraphSystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.R;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle the case where the user is not logged in
            return;
        }
        String userId = currentUser.getUid();

        BarChart barChart = view.findViewById(R.id.barChart);
        setupBarChart(barChart);
       fetchDataAndUpdateChart(barChart, userId);
    }



    private void setupBarChart(BarChart barChart) {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
    }


    private void fetchDataAndUpdateChart(BarChart barChart, String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BarEntry> entries = new ArrayList<>();

                float exerciseTime = dataSnapshot.child("exerciseTime").getValue(Float.class);
                float meditationTime = dataSnapshot.child("mediationTime").getValue(Float.class);
                float musicTime = dataSnapshot.child("musicTime").getValue(Float.class);

                entries.add(new BarEntry(0, new float[]{exerciseTime, meditationTime, musicTime}));

                BarDataSet barDataSet = new BarDataSet(entries, "User Activities (Minutes)");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setStackLabels(new String[]{"Exercise", "Meditation", "Music"});

                BarData barData = new BarData(barDataSet);
                barData.setValueFormatter(new DefaultValueFormatter(0));
                barData.setValueTextSize(10f);

                barChart.setData(barData);
                barChart.invalidate();
                Log.d("TAG", "onDataChange called");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Error fetching data", databaseError.toException());
            }
        });
    }


}