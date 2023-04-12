package com.linx.stress_free_app.ExercisePlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linx.stress_free_app.MeditationPlayer.OnStepCompletedListener;
import com.linx.stress_free_app.R;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ExercisePlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayerLocal;
    private MediaPlayer mediaPlayerRemote;
    private Handler handler;
    private long elapsedTime;
    private long targetTime;
    private int userScore;
    private Runnable updateElapsedTime;
    private ImageView step1ImageView;
    private OnStepCompletedListener stepCompletedListener;
    private ProgressBar progressBar;
    int exerciselevel;
    ImageView ExerciseGif;
    private StorageReference mStorageRef;

    private List<String> exerciseFiles;
    private long lastUpdateTimestamp;
    private Random random;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    DatabaseReference userRef = database.getReference("users").child(userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_player);
        progressBar = findViewById(R.id.progress_bar3);

        // Initialize Firebase Storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Initialize the exercise files list and the random generator
        exerciseFiles = new ArrayList<>();
        exerciseFiles.add("glue"); // Replace these with your actual file names
        exerciseFiles.add("pigeon");
        exerciseFiles.add("fold");
        exerciseFiles.add("dog");
        exerciseFiles.add("flexor");
        exerciseFiles.add("puppy");
        exerciseFiles.add("cowcat");
        exerciseFiles.add("child");
        random = new Random();

        // Get the last update timestamp from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("ExercisePlayerActivity", MODE_PRIVATE);
        lastUpdateTimestamp = sharedPreferences.getLong("lastUpdateTimestamp", 0);

        // If it has been 24 hours since the last update or it's the first time, update the timestamp and pick a new file
        if (System.currentTimeMillis() - lastUpdateTimestamp >= TimeUnit.HOURS.toMillis(24) || lastUpdateTimestamp == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("lastUpdateTimestamp", System.currentTimeMillis());
            editor.apply();

            int randomIndex = getRandomIndex(exerciseFiles.size());
            String randomFileName = exerciseFiles.get(randomIndex);
            editor.putString("chosenFileName", randomFileName);
            editor.apply();

            // Initialize the ExerciseGif ImageView
            ExerciseGif = findViewById(R.id.imageViewExerciseGif);

            loadGifFromFirebaseStorage(randomFileName);
            loadAudioFromFirebaseStorageAndPlay(randomFileName);
        } else {
            // Load the previously chosen files
            String chosenFileName = sharedPreferences.getString("chosenFileName", exerciseFiles.get(0));

            // Initialize the ExerciseGif ImageView
            ExerciseGif = findViewById(R.id.imageViewExerciseGif);

            loadGifFromFirebaseStorage(chosenFileName);
            loadAudioFromFirebaseStorageAndPlay(chosenFileName);
        }

        mediaPlayerLocal = MediaPlayer.create(this, R.raw.afternoonbreathing);
        handler = new Handler();
        startPlaying();
        elapsedTime = 0;
        targetTime = TimeUnit.SECONDS.toMillis(40); // Set the target time, e.g., 10 seconds
        userScore = 0;

        // Set the MediaPlayer to loop the audio clip
        mediaPlayerLocal.setLooping(true);
    }

    private int getRandomIndex(int listSize) {
        return random.nextInt(listSize);
    }


    public void setOnStepCompletedListener(OnStepCompletedListener listener) {
        this.stepCompletedListener = listener;
    }

    private void startPlaying() {
        if (mediaPlayerLocal != null) {
            mediaPlayerLocal.start();
        }
        if (mediaPlayerRemote != null) {
            mediaPlayerRemote.start();
        }
        updateElapsedTime = new Runnable() {
            @Override
            public void run() {
                elapsedTime += 1000;

                if (elapsedTime >= targetTime) {
                    userScore += 1;

                    // Update medlevel based on the userScore
                    if (userScore >= 1 && userScore <= 5) {
                        exerciselevel = 1;
                    } else if (userScore >= 6 && userScore <= 10) {
                       exerciselevel = 2;
                    } else if (userScore >= 11) {
                       exerciselevel = 3;
                    }

                    // Store data in Firebase when the target time is reached
                    storeDataInFirebase(elapsedTime, exerciselevel);

                    // Reset the elapsedTime after storing the data
                    elapsedTime = 0;
                    //mediaPlayerLocal.pause();




                    if (stepCompletedListener != null) {
                        stepCompletedListener.onStepCompleted(1);
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("step", 1);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    // Store data in Firebase periodically (e.g., every 5 seconds)
                    if (elapsedTime % 5000 == 0) {
                        storeDataInFirebase(elapsedTime, exerciselevel);
                    }
                    progressBar.setProgress((int) elapsedTime);
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(updateElapsedTime);
    }

    private void stopPlaying() {
        if (mediaPlayerLocal != null) {
            mediaPlayerLocal.stop();
        }
        if (mediaPlayerRemote != null) {
            mediaPlayerRemote.stop();
        }
        handler.removeCallbacks(updateElapsedTime);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerLocal != null) {
            mediaPlayerLocal.release();
            mediaPlayerLocal = null;
        }
        if (mediaPlayerRemote != null) {
            mediaPlayerRemote.release();
            mediaPlayerRemote = null;
        }
    }

    private void storeDataInFirebase(long elapsedTime, int exerciselevel) {
        userRef.child("exerciseTime").setValue(elapsedTime);
        userRef.child("exerciselevel").setValue(exerciselevel);
    }


    private void loadGifFromFirebaseStorage(String fileName) {
        String gifPath = "ExerciseVids/" + fileName + ".gif";
        StorageReference gifRef = mStorageRef.child(gifPath);

        gifRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity.this)
                        .asGif()
                        .load(uri)
                        .into(ExerciseGif);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("ExercisePlayer", "Failed to load GIF from Firebase Storage", exception);
            }
        });
    }


    private void loadAudioFromFirebaseStorageAndPlay(String fileName) {
        String audioPath = "ExerciseVids/" + fileName + ".wav";
        StorageReference audioRef = mStorageRef.child(audioPath);

        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    mediaPlayerRemote = new MediaPlayer();
                    mediaPlayerRemote.setDataSource(getApplicationContext(), uri);
                    mediaPlayerRemote.prepareAsync();
                    mediaPlayerRemote.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            startPlaying();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("MeditationPlayer", "Failed to load audio from Firebase Storage", exception);
            }
        });
    }





}