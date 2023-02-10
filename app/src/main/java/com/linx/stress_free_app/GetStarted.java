package com.linx.stress_free_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linx.stress_free_app.AnimationController.Typewriter;
import com.nitish.typewriterview.TypeWriterView;

public class GetStarted extends Activity {

    int count =0;
    boolean isAnimating = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        TypeWriterView typeWriterView = findViewById(R.id.typeWriterView);
        final String GetStartedDialog = getResources().getString(R.string.GetStartedDialog);
        final Button animateBtn = findViewById(R.id.animateBtn);

        //Animate Text
        typeWriterView.animateText(GetStartedDialog);




        animateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStarted.this, MoodSurveyActivity.class);

                startActivity(intent);

            }
        });
    }
}