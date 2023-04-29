package com.linx.stress_free_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

public class MoodSurveyActivity extends AppCompatActivity implements FragmentSubmitListener {

    Button NextBtn;
    TextView tallyScore;
    int currentFragmentIndex = 0;
    int submittedFragmentsCount = 0;
    final int totalFragments = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_survey);

        NextBtn = findViewById(R.id.Nextbtn);
        tallyScore = findViewById(R.id.tally_score);

        updateTallyScore();

        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submittedFragmentsCount == totalFragments) {
                    // Go to MainMenuActivity and display the toast
                    Toast.makeText(MoodSurveyActivity.this, "Well done for completing the survey! Look at your stress level below and find recommendations for reducing your stress.", Toast.LENGTH_LONG).show();
                    // Navigate to MainMenuActivity
                } else {
                    navigateToNextFragment();
                }
            }
        });

        // Load the initial fragment
        navigateToNextFragment();
    }

    private void navigateToNextFragment() {
        Fragment fragment;

        switch (currentFragmentIndex) {
            case 0:
                fragment = new GetScreenTimeFragment();
                break;
            case 1:
                fragment = new BedTimeFragment();
                break;
            case 2:
                fragment = new DisplayAppUsageFragment();
                break;
            case 3:
                fragment = new PainFragment(); // Replace with the actual fourth fragment class
                break;
            default:
                throw new IllegalStateException("Invalid fragment index");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        NextBtn.setVisibility(View.GONE);
        currentFragmentIndex++;
    }

    private void updateTallyScore() {
        tallyScore.setText("Survey Completed"+" "+submittedFragmentsCount + "/" + totalFragments);
    }

    @Override
    public void onSubmit(int fragmentIndex) {
        submittedFragmentsCount++;
        updateTallyScore();
        new AestheticDialog.Builder(this, DialogStyle.EMOTION, DialogType.SUCCESS)
                .setTitle("Well done")
                .setMessage("Thank you for submitting the data")
                .show();
        NextBtn.setVisibility(View.VISIBLE);
    }
}
