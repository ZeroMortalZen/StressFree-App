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
import com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity;
import com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity2;
import com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity3;
import com.linx.stress_free_app.ExercisePlayer.ImageData;
import com.linx.stress_free_app.ExercisePlayer.ImageFragment;
import com.linx.stress_free_app.ExercisePlayer.ImagesAdapter;
import com.linx.stress_free_app.ExercisePlayer.TutorialPlayerActivity2;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.linx.stress_free_app.MeditationPlayer.OnStepCompletedListener;
import com.linx.stress_free_app.R;

import java.util.ArrayList;
import java.util.List;


public class ExerciseFragment extends Fragment implements OnStepCompletedListener {

    private LinearLayout verticalStepProgressBar;
    private int[] stepIndicatorIds = {R.id.step1, R.id.step2, R.id.step3}; // Add more step IDs if needed
    private int currentStep = 0;
    ImageButton button1;
    ImageButton button2;
    ImageButton button3;
    Button ytbutton;
    TextView textviewtaskcom2;
    TextView rewardText2;
    ImageView rewardmedicon2;
    ImageView imageTask2;

    private RecyclerView imagesRecyclerView;
    private ImagesAdapter imagesAdapter;
    private List<ImageData> imagesData = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        textviewtaskcom2 =view.findViewById(R.id.textviewtaskcom2);
        imageTask2 = view.findViewById(R.id.imageTask2);
        rewardmedicon2 = view.findViewById(R.id.rewardmedicon2);
        rewardText2=view.findViewById(R.id.rewardText2);



        button2 = view.findViewById(R.id.button2);
        button1 = view.findViewById(R.id.medbutton1);
        button3 = view.findViewById(R.id.medbutton2);
        ytbutton = view.findViewById(R.id.YTbutton);



        verticalStepProgressBar = view.findViewById(R.id.verticalStepProgressBar);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        int completedStep = sharedPreferences.getInt("completed_step", 0);
        updateUIForCompletedStep(completedStep);

        currentStep = loadSteps(); // Load the steps
        updateStepIndicators(currentStep);






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
                setCurrentStep(step);
                onStepCompleted(step);
            }
        }
    }

    private void saveSteps(int steps) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_steps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("steps", steps);
        editor.putLong("timestamp", System.currentTimeMillis());
        editor.apply();
    }

    private int loadSteps() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_steps", Context.MODE_PRIVATE);
        long savedTimestamp = sharedPreferences.getLong("timestamp", 0);
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - savedTimestamp;

        // Check if a day has passed (24 hours * 60 minutes * 60 seconds * 1000 milliseconds)
        if (timeDifference >= 24 * 60 * 60 * 1000) {
            saveSteps(0); // Reset the steps
            updateUIForCompletedStep(0); // Update UI based on reset steps
            return 0;
        }

        return sharedPreferences.getInt("steps", 0);
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


    public void onStepCompleted(int step) {
        if (step == 1) {
            imageTask2.setImageResource(R.drawable.num1); // Replace with the appropriate image resource
            textviewtaskcom2.setText("Daily Meditation Completed 1");
            Toast.makeText(getActivity(), "You have completed your morning exercise, you have earned a point.", Toast.LENGTH_SHORT).show();
        } else if (step == 2) {
            imageTask2.setImageResource(R.drawable.num2); // Replace with the appropriate image resource
            textviewtaskcom2.setText("Daily Meditation Completed 2");
            Toast.makeText(getActivity(), "You have completed your afternoon exercise, you have earned a point.", Toast.LENGTH_SHORT).show();
        } else if (step == 3) {
            imageTask2.setImageResource(R.drawable.num3); // Replace with the appropriate image resource
            textviewtaskcom2.setText("Daily Meditation Completed 3");
            rewardmedicon2.setVisibility(View.VISIBLE);
            rewardText2.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "You have completed your night exercise, you have earned a point.", Toast.LENGTH_SHORT).show();
        }
        saveCompletedStep(step);
    }

    private void saveCompletedStep(int step) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("completed_step", step);
        editor.apply();
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
            rewardmedicon2.setImageResource(R.drawable.reward);
            rewardText2.setText("You Earned A Point");
            textviewtaskcom2.setText("Daily Exercise Completed 3");
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



}