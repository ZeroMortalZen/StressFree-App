package com.linx.stress_free_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linx.stress_free_app.AnimationController.ProgressBarAnimation;

public class MoodSurveyActivity extends AppCompatActivity {

    Button firstFragmentBtn, secondFragmentBtn;
    ProgressBar progressBar;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_survey);



        firstFragmentBtn = findViewById(R.id.fragment1btn);
        secondFragmentBtn = findViewById(R.id.fragment2btn);

        firstFragmentBtn.setVisibility(View.GONE);
        secondFragmentBtn.setVisibility(View.GONE);


        progressBar =findViewById(R.id.progressBar);
        textView = findViewById(R.id.text_view);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        progessAnimation();


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

    public void progessAnimation(){
        ProgressBarAnimation anim = new ProgressBarAnimation(firstFragmentBtn,progressBar,textView,0f,100f);
        anim.setDuration(8000);
        progressBar.setAnimation(anim);
    }

}