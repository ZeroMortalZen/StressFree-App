package com.linx.stress_free_app;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nitish.typewriterview.TypeWriterView;

public class GetStarted extends Activity {

    int count = 0;
    boolean isAnimating = true;
    private Button MoreInfoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        MoreInfoBtn = findViewById(R.id.infobtn);
        MoreInfoBtn.setVisibility(View.GONE); // Hide MoreInfoBtn initially

        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        TypeWriterView typeWriterView = findViewById(R.id.typeWriterView);
        final String GetStartedDialog = getResources().getString(R.string.GetStartedDialog);
        final Button animateBtn = findViewById(R.id.animateBtn);
        final Button GetPermissionBtn = findViewById(R.id.GetPremissionBtn);

        ImageView imageView2 = findViewById(R.id.imageView2);

        animateBtn.setVisibility(View.GONE);

        // Animate Text
        typeWriterView.animateText(GetStartedDialog);

        int characterDelay = 125; // Update this if you have set a custom character delay
        int duration = GetStartedDialog.length() * characterDelay;

        int reducedDelay = 20000; // Set this to the amount of time you want to reduce the delay by
        int delayToShowMoreInfoBtn = Math.max(0, duration - reducedDelay);
        // Show MoreInfoBtn after the animation is complete
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MoreInfoBtn.setVisibility(View.VISIBLE);
            }
        }, delayToShowMoreInfoBtn);

        if (granted) {
            // Set button to visible and set permission button invisible
            animateBtn.setVisibility(View.VISIBLE);
            GetPermissionBtn.setVisibility(View.GONE);
        }

        animateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStarted.this, MoodSurveyActivity.class);
                startActivity(intent);
            }
        });

        GetPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });

        MoreInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStarted.this,infoActivity.class);
                startActivity(intent);
            }
        });
    }

}
