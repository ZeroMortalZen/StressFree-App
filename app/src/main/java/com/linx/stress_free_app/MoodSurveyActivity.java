package com.linx.stress_free_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linx.stress_free_app.AnimationController.ProgressBarAnimation;
import com.linx.stress_free_app.AnimationController.Typewriter;

public class MoodSurveyActivity extends AppCompatActivity {

    TextView currentUserTextView;
    private FirebaseAuth mAuth;
    Button BackBtn, NextBtn;
    ProgressBar progressBar;
    TextView textView;
    long characterDelay = 40;
    boolean avoidTextOverflowAtEdge = true;
    private int currentFragmentIndex = 0;
    private final Fragment[] fragments = {new PainFragment(), new BedTimeFragment(), new GetScreenTimeFragment(), new DisplayAppUsageFragment()};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_survey);
        // Check if user is signed in (non-null) and update UI accordingly.

        // Initialize mAuth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Redirect to LoginActivity if the user is not logged in
            Intent intent = new Intent(MoodSurveyActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        Typewriter typewriterLoadingDialog = findViewById(R.id.typewriterLoadingDialog);
        final String Loading_Dialog =  getString(R.string.Loading_dialog);

        BackBtn = findViewById(R.id.Nextbtn);
        NextBtn = findViewById(R.id.Backbtn);
        currentUserTextView = findViewById(R.id.current_user_text_view);





        progressBar =findViewById(R.id.progressBar);
        textView = findViewById(R.id.text_view);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        typewriterLoadingDialog.setCharacterDelay(characterDelay);
        typewriterLoadingDialog.animateText(Loading_Dialog);

        progessAnimation();





      BackBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              currentFragmentIndex--;
              if (currentFragmentIndex < 0) {
                  currentFragmentIndex = fragments.length - 1;
              }
              switchFragment(fragments[currentFragmentIndex]);

          }
      });

      NextBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              currentFragmentIndex++;
              if (currentFragmentIndex >= fragments.length) {
                  currentFragmentIndex = 0;
              }
              switchFragment(fragments[currentFragmentIndex]);
          }
      });






    }

    public void switchFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    public void progessAnimation(){
        ProgressBarAnimation anim = new ProgressBarAnimation(BackBtn,progressBar,textView,0f,100f);
        anim.setDuration(8000);
        progressBar.setAnimation(anim);
        if(anim.hasEnded()){
            NextBtn.setVisibility(View.VISIBLE);
        }

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            String displayName = currentUser.getDisplayName();
            if (displayName != null) {
                currentUserTextView.setText(displayName);
            } else {
                currentUserTextView.setText("user");
            }
        }else {
            currentUserTextView.setText("Error");
        }
    }

}