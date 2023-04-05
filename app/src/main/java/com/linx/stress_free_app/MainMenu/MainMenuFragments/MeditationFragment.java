package com.linx.stress_free_app.MainMenu.MainMenuFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linx.stress_free_app.MeditationPlayer.MeditationPlayerActivity;
import com.linx.stress_free_app.MeditationPlayer.OnStepCompletedListener;
import com.linx.stress_free_app.MeditationPlayer.TutorialPlayerActivity;
import com.linx.stress_free_app.R;


public class MeditationFragment extends Fragment {

    private LinearLayout verticalStepProgressBar;
    private int[] stepIndicatorIds = {R.id.step1, R.id.step2, R.id.step3}; // Add more step IDs if needed
    private int currentStep = 0;
    Button button1 ;
    Button button2;
    Button button3;
    Button ytbutton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditation, container, false);

        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button3);
        button3 = view.findViewById(R.id.button2);
        ytbutton = view.findViewById(R.id.YTbutton);

        verticalStepProgressBar = view.findViewById(R.id.verticalStepProgressBar);
        updateStepIndicators(currentStep);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MeditationPlayerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                //startActivity(intent);
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                //startActivity(intent);
            }
        });

        ytbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TutorialPlayerActivity.class);
                startActivity(intent);
            }
        });

        MeditationPlayerActivity meditationPlayerActivity = new MeditationPlayerActivity();
        meditationPlayerActivity.setOnStepCompletedListener(new OnStepCompletedListener() {
            @Override
            public void onStepCompleted(int step) {
                if (step == 1) {
                    ImageView step1ImageView = view.findViewById(R.id.step1);
                    step1ImageView.setImageResource(R.drawable.circle_completed);
                }
            }
        });






        return view;
    }

    private void updateStepIndicators(int currentStep) {
        for (int i = 0; i < stepIndicatorIds.length; i++) {
            ImageView stepIndicator = verticalStepProgressBar.findViewById(stepIndicatorIds[i]);
            if (i < currentStep) {
                stepIndicator.setImageResource(R.drawable.circle_completed);
            } else {
                stepIndicator.setImageResource(R.drawable.circle_uncompleted);
            }
        }
    }

    // Call this method to update the current step and refresh the step indicators
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        updateStepIndicators(currentStep);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int step = data.getIntExtra("step", 0);
                setCurrentStep(step);
            }
        }
    }


}

