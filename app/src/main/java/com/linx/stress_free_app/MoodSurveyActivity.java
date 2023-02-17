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
import com.linx.stress_free_app.AnimationController.Typewriter;

public class MoodSurveyActivity extends AppCompatActivity {

    Button firstFragmentBtn, secondFragmentBtn;
    ProgressBar progressBar;
    TextView textView;
    long characterDelay = 40;
    boolean avoidTextOverflowAtEdge = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_survey);


        Typewriter typewriterLoadingDialog = findViewById(R.id.typewriterLoadingDialog);
        final String Loading_Dialog =  getString(R.string.Loading_dialog);

        firstFragmentBtn = findViewById(R.id.fragment1btn);
        secondFragmentBtn = findViewById(R.id.fragment2btn);

        firstFragmentBtn.setVisibility(View.GONE);
        secondFragmentBtn.setVisibility(View.GONE);





        progressBar =findViewById(R.id.progressBar);
        textView = findViewById(R.id.text_view);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        typewriterLoadingDialog.setCharacterDelay(characterDelay);
        typewriterLoadingDialog.animateText(Loading_Dialog);

        progessAnimation();


      firstFragmentBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              replaceFragment(new PainFragment());
              firstFragmentBtn.setVisibility(View.GONE);

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