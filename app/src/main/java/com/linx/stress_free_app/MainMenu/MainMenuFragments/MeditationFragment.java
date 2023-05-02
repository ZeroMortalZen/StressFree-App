package com.linx.stress_free_app.MainMenu.MainMenuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.viewmodels.SharedViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linx.stress_free_app.MeditationPlayer.MeditationPlayerActivity;
import com.linx.stress_free_app.MeditationPlayer.MeditationPlayerActivity2;
import com.linx.stress_free_app.MeditationPlayer.MeditationPlayerActivity3;
import com.linx.stress_free_app.MeditationPlayer.OnStepCompletedListener;
import com.linx.stress_free_app.MeditationPlayer.TutorialPlayerActivity;
import com.linx.stress_free_app.R;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;


public class MeditationFragment extends Fragment implements OnStepCompletedListener {

    private SharedViewModel sharedViewModel;
    private LinearLayout verticalStepProgressBar;
    private int[] stepIndicatorIds = {R.id.step1, R.id.step2, R.id.step3}; // Add more step IDs if needed
    private int currentStep = 0;
    Button button1 ;
    Button button2;
    Button button3;
    ImageButton ytbutton;
    TextView textviewtaskcom;
    ImageView imageTask;
    private boolean showDialogOnLoad = true;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditation, container, false);

        textviewtaskcom =view.findViewById(R.id.textviewtaskcom);
        imageTask = view.findViewById(R.id.imageTask);
        button3 = view.findViewById(R.id.medbutton3);
        button1 = view.findViewById(R.id.medbutton1);
        button2 = view.findViewById(R.id.exbutton3);
        ytbutton = view.findViewById(R.id.YTbutton);



        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getMeditationProgress().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer progress) {
                setCurrentStep(progress);
                onStepCompleted(progress);
            }
        });



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        int completedStep = sharedPreferences.getInt("completed_step", 0);
        updateUIForCompletedStep(completedStep);


        verticalStepProgressBar = view.findViewById(R.id.verticalStepProgressBar);
        loadSteps(); // Load the steps
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
                Intent intent = new Intent(getActivity(), MeditationPlayerActivity2.class);
                startActivityForResult(intent, 2);

            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MeditationPlayerActivity3.class);
                startActivityForResult(intent, 3);
            }
        });

        ytbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TutorialPlayerActivity.class);
                startActivity(intent);
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
        saveSteps(currentStep);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2 || requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                int step = data.getIntExtra("step", 0);
                sharedViewModel.setMeditationProgress(step);
            }
        }
    }


    private void saveSteps(int steps) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference stepsRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            stepsRef.child("meditation_steps").child("steps").setValue(steps);
            stepsRef.child("meditation_steps").child("timestamp").setValue(System.currentTimeMillis());
        }
    }



    private void loadSteps() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference stepsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("meditation_steps");

            stepsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long savedTimestamp = dataSnapshot.child("timestamp").getValue(Long.class);
                    long currentTime = System.currentTimeMillis();
                    long timeDifference = currentTime - (savedTimestamp != null ? savedTimestamp.longValue() : 0L);

                    // Check if a day has passed (24 hours * 60 minutes * 60 seconds * 1000 milliseconds)
                    if (timeDifference >= 24 * 60 * 60 * 1000) {
                        saveSteps(0); // Reset the steps
                        updateUIForCompletedStep(0); // Update UI based on reset steps
                        setCurrentStep(0);
                    } else {
                        int steps = dataSnapshot.child("steps").getValue(Integer.class);
                        setCurrentStep(steps);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }




    @Override

    public void onStepCompleted(int step) {
        String title = "";
        String message = "";
        if (step == 1) {
            imageTask.setImageResource(R.drawable.num1); // Replace with the appropriate image resource
            textviewtaskcom.setText("Daily Meditation Completed 1");
            title = "Well Done";
            message = "Daily Meditation Completed 1 Check your progress bar";
            showEmotionDialog(title,message);
        } else if (step == 2) {
            imageTask.setImageResource(R.drawable.num2); // Replace with the appropriate image resource
            textviewtaskcom.setText("Daily Meditation Completed 2");
            title = "Well Done";
            message = "Daily Meditation Completed 2 Check your progress bar";
            showEmotionDialog(title,message);
        } else if (step == 3) {
            imageTask.setImageResource(R.drawable.num3); // Replace with the appropriate image resource
            textviewtaskcom.setText("Daily Meditation Completed 3");
            title = "Well Done";
            message = "Daily Meditation Completed 3 Check your progress bar";
            showEmotionDialog(title,message);
        }
        saveCompletedStep(step);

        if (showDialogOnLoad) {
            //showEmotionDialog(title, message);
        }
    }

    private void saveCompletedStep(int step) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("completed_step", step);
        editor.apply();
    }


    private void updateUIForCompletedStep(int step) {

        if (step == 1) {
            imageTask.setImageResource(R.drawable.num1);
            textviewtaskcom.setText("Daily Meditation Completed 1");

        } else if (step == 2) {
            imageTask.setImageResource(R.drawable.num2);
            textviewtaskcom.setText("Daily Meditation Completed 2");

        } else if (step == 3) {
            imageTask.setImageResource(R.drawable.num3);
            textviewtaskcom.setText("Daily Meditation Completed 3");

        }

        // Disable button2 and button3 initially
        button2.setEnabled(false);
        button3.setEnabled(false);

        // Enable button2 if step 1 is completed
        if (step >= 1) {
            button2.setEnabled(true);
        }

       // Enable button3 if step 2 is completed
        if (step >= 2) {
            button3.setEnabled(true);
        }




    }

    private void showEmotionDialog(String title, String message) {
        if (showDialogOnLoad) {
            AestheticDialog.Builder builder = new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.SUCCESS);
            builder.setTitle(title)
                    .setMessage(message)
                    .setCancelable(true)
                    .setOnClickListener(new OnDialogClickListener() {
                        @Override
                        public void onClick(AestheticDialog.Builder dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showDialogOnLoad = false;
    }


}

