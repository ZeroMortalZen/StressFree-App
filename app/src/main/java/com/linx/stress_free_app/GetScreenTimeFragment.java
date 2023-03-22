package com.linx.stress_free_app;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.linx.stress_free_app.AnimationController.ProgressBarAnimation;
import com.linx.stress_free_app.StressSystem.PersonStressHelperClass;
import com.nitish.typewriterview.TypeWriterView;

import java.util.Calendar;
import java.util.List;

public class GetScreenTimeFragment extends Fragment {

    PersonStressHelperClass stressHelperClass = new PersonStressHelperClass();
    ProgressBar progressBar;
    TextView PrecentageView;
    Button NextScreenDataBtn;
    TextView typewriterText;





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

         progressBar= view.findViewById(R.id.progressBar2);
        progressBar.setMax(100);
        progressBar.setScaleY(3f);
         PrecentageView = view.findViewById(R.id.precentageView);
        NextScreenDataBtn =view.findViewById(R.id.NextScreenDataBtn);
        TextView screenTimeTextView = view.findViewById(R.id.screenTimeTextView);
        typewriterText = (TextView) view.findViewById(R.id.typewriter_text);
        handler.postDelayed(runnable, 500); // adjust the delay to make it start after a certain time

        long screenTime = getScreenTime(getContext());

        screenTimeTextView.setText("Screen Time: " + screenTime + "ms");
        progessAnimation();


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

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        int i = 0;
        final String text = "Checking Screen Usage";
        @Override
        public void run() {
            if (i <= text.length()) {
                String str = text.substring(0, i);
                typewriterText.setText(str);
                i++;
                handler.postDelayed(this, 50); // adjust the delay to make it faster or slower
            }
        }
    };

}