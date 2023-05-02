package com.linx.stress_free_app.MainMenu.MainMenuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity;
import com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity2;
import com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity3;
import com.linx.stress_free_app.ExercisePlayer.ImageData;
import com.linx.stress_free_app.ExercisePlayer.ImageFragment;
import com.linx.stress_free_app.ExercisePlayer.ImagesAdapter;
import com.linx.stress_free_app.ExercisePlayer.TutorialPlayerActivity2;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider;
import com.linx.stress_free_app.viewmodels.SharedViewModel;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.linx.stress_free_app.MeditationPlayer.OnStepCompletedListener;
import com.linx.stress_free_app.R;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.util.ArrayList;
import java.util.List;


public class ExerciseFragment extends Fragment implements OnStepCompletedListener {

    private LinearLayout verticalStepProgressBar;
    private int[] stepIndicatorIds = {R.id.step1, R.id.step2, R.id.step3}; // Add more step IDs if needed
    private int currentStep = 0;
    Button button1;
    Button button2;
    Button button3;
    ImageButton ytbutton;
    TextView textviewtaskcom2;
    ImageView imageTask2;

    private RecyclerView imagesRecyclerView;
    private ImagesAdapter imagesAdapter;
    private List<ImageData> imagesData = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private boolean showDialogOnLoad = true;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private int exerciseCurrentStep = 0;
    private boolean stepCompleted;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);


        textviewtaskcom2 =view.findViewById(R.id.textviewtaskcom2);
        imageTask2 = view.findViewById(R.id.imageTask2);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        loadSteps();


        button2 = view.findViewById(R.id.button2);
        button1 = view.findViewById(R.id.medbutton1);
        button3 = view.findViewById(R.id.exbutton3);
        ytbutton = view.findViewById(R.id.YT);

        verticalStepProgressBar = view.findViewById(R.id.verticalStepProgressBar);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        updateUIForCompletedStep(exerciseCurrentStep);

        updateStepIndicators(exerciseCurrentStep);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExercisePlayerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExercisePlayerActivity2.class);
                startActivityForResult(intent, 2);

            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExercisePlayerActivity3.class);
                startActivityForResult(intent, 3);
            }
        });

        ytbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TutorialPlayerActivity2.class);
                startActivity(intent);
            }
        });


        // Set up the RecyclerView and Adapter
        imagesRecyclerView = view.findViewById(R.id.images_recycler_view);
        imagesAdapter = new ImagesAdapter(imagesData, new ImagesAdapter.OnItemClickListener() {
            public void onItemClick(ImageData imageData, int position) {
                // Pass both imageUrl and audioUrl when creating a new instance of the ImageFragment
                ImageFragment imageFragment = ImageFragment.newInstance(imageData.getImageUrl(), imageData.getAudioUrl());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, imageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        imagesRecyclerView.setAdapter(imagesAdapter);

        // Load images and gifs from Firebase Storage
        loadImagesFromFirebase();
        return view;
    }

    private void updateStepIndicators(int exerciseCurrentStep) {
        for (int i = 0; i < stepIndicatorIds.length; i++) {
            Log.d("Current Step", String.valueOf(exerciseCurrentStep));
            ImageView stepIndicator = verticalStepProgressBar.findViewById(stepIndicatorIds[i]);

            if (i < exerciseCurrentStep) {
                stepIndicator.setImageResource(R.drawable.check);
            } else {
                stepIndicator.setImageResource(R.drawable.circle_uncompleted);
            }
        }
    }

    // Call this method to update the current step and refresh the step indicators
    public void setCurrentStep(int currentStep) {
        this.exerciseCurrentStep = currentStep;
        updateStepIndicators(currentStep);
        saveSteps(currentStep);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2 || requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                int step = data.getIntExtra("step", 0);
                setCurrentStep(step);
                onStepCompleted(step);
            }
        }
    }


    private void saveSteps(int steps) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference stepsRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            stepsRef.child("exercise_steps").child("steps").setValue(steps);
            stepsRef.child("exercise_steps").child("timestamp").setValue(System.currentTimeMillis());
        }
    }


    private void loadSteps() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            ValueEventListener exerciseStepsListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer steps = dataSnapshot.getValue(Integer.class);
                    if (steps != null) {
                        exerciseCurrentStep = steps;
                    } else {
                        exerciseCurrentStep = 0;
                    }
                    updateUIForCompletedStep(exerciseCurrentStep);
                    updateStepIndicators(exerciseCurrentStep);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            };

            // Load exercise_steps
            DatabaseReference exerciseStepsRef = userRef.child("exercise_steps").child("steps");
            exerciseStepsRef.addListenerForSingleValueEvent(exerciseStepsListener);
        }
    }


    private void loadImagesFromFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("/ExerciseVids"); // folder name in Firebase Storage

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // Check if the file is a GIF by examining the file extension
                            String fileName = item.getName();
                            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                            if (fileExtension.equalsIgnoreCase("gif")) {
                                item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Remove the file extension (.gif) from the file name
                                        String displayName = fileName.substring(0, fileName.lastIndexOf("."));
                                        String audioUrl = uri.toString().replace(".gif", ".wav");
                                        imagesData.add(new ImageData(displayName, uri.toString(), uri.toString(), audioUrl));
                                        imagesAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });
    }


    public void onStepCompleted(int exerciseCurrentStep) {
        sharedViewModel.setYogaProgress(exerciseCurrentStep);
        String title = "";
        String message = "";
        if (exerciseCurrentStep == 1) {

            imageTask2.setImageResource(R.drawable.num1); // Replace with the appropriate image resource
            textviewtaskcom2.setText("Daily Meditation Completed 1");
            title = "Well done!";
            message = "You have completed your Morning  exercise.";
            showEmotionDialog(title, message);

        } else if (exerciseCurrentStep == 2) {
            imageTask2.setImageResource(R.drawable.num2); // Replace with the appropriate image resource
            textviewtaskcom2.setText("Daily Meditation Completed 2");
            title = "Well done!";
            message = "You have completed your Afternoon exercise.";
            showEmotionDialog(title, message);
        } else if (exerciseCurrentStep == 3) {
            imageTask2.setImageResource(R.drawable.num3); // Replace with the appropriate image resource
            textviewtaskcom2.setText("Daily Meditation Completed 3");
            title = "Well done!";
            message = "You have completed your Evening exercise.";
            showEmotionDialog(title, message);
        }
        saveCompletedStep(exerciseCurrentStep);

        // Disable button2 and button3 initially
        button2.setEnabled(false);
        button3.setEnabled(false);

        // Enable button2 if step 1 is completed
        if (exerciseCurrentStep >= 1) {
            button2.setEnabled(true);
            button1.setEnabled(false);
        }

        // Enable button3 if step 2 is completed
        if (exerciseCurrentStep >= 2) {
            button3.setEnabled(true);
            button2.setEnabled(false);
            button1.setEnabled(false);

        }

    }

    private void saveCompletedStep(int step) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("completed_step").child(userId).setValue(step);
        }
    }

    private void updateUIForCompletedStep(int step) {


        if (step == 1) {
            imageTask2.setImageResource(R.drawable.num1);
            textviewtaskcom2.setText("Daily Exercise Completed 1");

        } else if (step == 2) {
            imageTask2.setImageResource(R.drawable.num2);
            textviewtaskcom2.setText("Daily Exercise Completed 2");

        } else if (step == 3) {
            imageTask2.setImageResource(R.drawable.num3);
            textviewtaskcom2.setText("Daily Exercise Completed 3");

        }

        // Disable button2 and button3 initially
        button2.setEnabled(false);
        button3.setEnabled(false);

        // Enable button2 if step 1 is completed
        if (step >= 1) {
            button2.setEnabled(true);
            button1.setEnabled(false);
        }

        // Enable button3 if step 2 is completed
        if (step >= 2) {
            button3.setEnabled(true);
            button2.setEnabled(false);
            button1.setEnabled(false);

        }

        if (showDialogOnLoad) {
            //showEmotionDialog(title, message);
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