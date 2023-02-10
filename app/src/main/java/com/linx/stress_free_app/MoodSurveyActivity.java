package com.linx.stress_free_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MoodSurveyActivity extends AppCompatActivity {

    Button firstFragmentBtn, secondFragmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_survey);
        firstFragmentBtn = findViewById(R.id.fragment1btn);
        secondFragmentBtn = findViewById(R.id.fragment2btn);

      firstFragmentBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              replaceFragment(new PainFragment());

          }
      });

      secondFragmentBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              replaceFragment(new BedTimeFragment());
          }
      });
    }

    public void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}