package com.linx.stress_free_app;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.linx.stress_free_app.AnimationController.ProgressBarAnimation;
import com.linx.stress_free_app.StressSystem.HelperClass;

import java.util.Calendar;
import java.util.List;

public class GetScreenTimeFragment extends Fragment {

    private FirebaseAuth mAuth;
    HelperClass helperClass = new HelperClass();
    ProgressBar progressBar;
    TextView PrecentageView;
    Button NextScreenDataBtn;
    TextView typewriterText;
    private static final long WEEK_IN_MILLIS = 7 * 24 * 60 * 60 * 1000; // 7 days in milliseconds
    FirebaseDatabase database = FirebaseDatabase.getInstance();





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();



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

        screenTimeTextView.setText("Screen Time: " + screenTime + "Min");
        progessAnimation();

        startResetScreenUsageTimer();



        NextScreenDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Store in Database
                //Database connection & Store Values
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();
                DatabaseReference userRef = database.getReference("users").child(userId);

                userRef.child("totalScreenTime").setValue(helperClass.getTotalScreenTime());

            }
        });



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

        helperClass.setTotalScreenTime(minutes);

        //Store in Database
        //Database connection & Store Values







        String getTotalScreenTime= String.valueOf(helperClass.getTotalScreenTime());
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


    private void resetScreenUsageTime() {
        helperClass.setTotalScreenTime(0);
    }

    Runnable resetScreenUsageRunnable = new Runnable() {
        @Override
        public void run() {
            resetScreenUsageTime();
            handler.postDelayed(this, WEEK_IN_MILLIS);
        }
    };

    private void startResetScreenUsageTimer() {
        handler.postDelayed(resetScreenUsageRunnable, WEEK_IN_MILLIS);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();

        }
    }


}