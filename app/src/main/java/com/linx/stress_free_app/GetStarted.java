package com.linx.stress_free_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linx.stress_free_app.AnimationController.Typewriter;
import com.nitish.typewriterview.TypeWriterView;

public class GetStarted extends Activity {

    int count =0;
    boolean isAnimating = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        TypeWriterView typeWriterView = findViewById(R.id.typeWriterView);
        final String GetStartedDialog = getResources().getString(R.string.GetStartedDialog);
        final Button animateBtn = findViewById(R.id.animateBtn);
        final Button GetPermissionBtn = findViewById(R.id.GetPremissionBtn);

        ImageView imageView2 = findViewById(R.id.imageView2);
        Glide.with(this).load(R.drawable.docanim).into(imageView2);

        animateBtn.setVisibility(View.GONE);

        //Animate Text
        typeWriterView.animateText(GetStartedDialog);


         if (granted){
             //set button to visable and set permission button invisalbe
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
    }
}