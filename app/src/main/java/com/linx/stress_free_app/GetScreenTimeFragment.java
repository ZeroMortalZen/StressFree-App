package com.linx.stress_free_app;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.linx.stress_free_app.AnimationController.ProgressBarAnimation;
import com.linx.stress_free_app.StressSystem.PersonStressHelperClass;

import java.util.Calendar;
import java.util.List;

public class GetScreenTimeFragment extends Fragment {

    private TextView screenTimeTextView;
    PersonStressHelperClass stressHelperClass = new PersonStressHelperClass();
    ProgressBar progressBar;
    TextView PrecentageView;
    Button NextScreenDataBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PrecentageView.findViewById(R.id.precentageView);

        //progressBar.findViewById(R.id.progressBar);
        //progressBar.setMax(100);
        //progressBar.setScaleY(3f);

        //progessAnimation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_screen_time, container, false);

        screenTimeTextView = view.findViewById(R.id.screenTimeTextView);

        long screenTime = getScreenTime(getContext());
        screenTimeTextView.setText("Screen Time: " + screenTime + "ms");

        return view;
    }

    private long getScreenTime(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        long totalScreenTime = 0;

        for (UsageStats usageStats : usageStatsList) {
            totalScreenTime += usageStats.getTotalTimeInForeground();
        }


        //Next Convert Ms to Minutes
        long minutes = (totalScreenTime / 1000) / 60;

        stressHelperClass.setTotalScreenTime(minutes);

        String getTotalScreenTime= String.valueOf(stressHelperClass.getTotalScreenTime());
        Toast.makeText(getActivity(),getTotalScreenTime,Toast.LENGTH_SHORT).show();

        return minutes;



    }


    public void progessAnimation(){
        ProgressBarAnimation anim = new ProgressBarAnimation(NextScreenDataBtn,progressBar,PrecentageView,0f,100f);
        anim.setDuration(8000);
        progressBar.setAnimation(anim);
    }
}