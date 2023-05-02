package com.linx.stress_free_app.ExercisePlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linx.stress_free_app.MeditationPlayer.OnStepCompletedListener;
import com.linx.stress_free_app.R;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ExercisePlayerActivity3 extends AppCompatActivity {

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
    private TextView ExDialog;
    private TextView countdownTimer;

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
        setContentView(R.layout.activity_exercise_player3);
        countdownTimer = findViewById(R.id.countdown_timer3);
        ExDialog =findViewById(R.id.ExDialog);

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

            String randomFileName;
            do {
                int randomIndex = getRandomIndex(exerciseFiles.size());
                randomFileName = exerciseFiles.get(randomIndex);
            } while (isFileUsedInOtherActivities(randomFileName));

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
        targetTime = TimeUnit.SECONDS.toMillis(80); // Set the target time, e.g., 10 seconds
        userScore = 0;

        // Set the MediaPlayer to loop the audio clip
        mediaPlayerLocal.setLooping(true);
    }

    private int getRandomIndex(int listSize) {
        return random.nextInt(listSize);
    }

    private boolean isFileUsedInOtherActivities(String fileName) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("ExercisePlayerActivity", MODE_PRIVATE);
        String chosenFileName1 = sharedPreferences1.getString("chosenFileName", "");

        SharedPreferences sharedPreferences2 = getSharedPreferences("ExercisePlayerActivity2", MODE_PRIVATE);
        String chosenFileName2 = sharedPreferences2.getString("chosenFileName", "");

        return fileName.equals(chosenFileName1) || fileName.equals(chosenFileName2);
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
                long elapsedTimeInMinutes = elapsedTime / 60000;


                // Calculate the remaining time
                long remainingTime = targetTime - elapsedTime;

                // Format the remaining time as MM:SS
                String formattedRemainingTime = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));

                // Update the countdown timer TextView
                countdownTimer.setText(formattedRemainingTime);

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
                    storeDataInFirebase(elapsedTimeInMinutes, exerciselevel);

                    // Reset the elapsedTime after storing the data
                    elapsedTime = 0;

                    if (stepCompletedListener != null) {
                        stepCompletedListener.onStepCompleted(3);
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("step", 3);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    // Store data in Firebase periodically (e.g., every 5 seconds)
                    if (elapsedTime % 5000 == 0) {
                        storeDataInFirebase(elapsedTimeInMinutes, exerciselevel);
                    }
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

    private void storeDataInFirebase(long elapsedTimeInMinutes, int exerciselevel) {
        // Update exerciseTime
        userRef.child("exerciseTime").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long currentExerciseTime = mutableData.getValue(Long.class);
                if (currentExerciseTime == null) {
                    mutableData.setValue(elapsedTimeInMinutes);
                } else {
                    mutableData.setValue(currentExerciseTime + elapsedTimeInMinutes);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                // Handle completion of the transaction here.
            }
        });

        // Update exerciselevel
        userRef.child("exerciselevel").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentExerciseLevel = mutableData.getValue(Integer.class);
                if (currentExerciseLevel == null) {
                    mutableData.setValue(exerciselevel);
                } else {
                    mutableData.setValue(currentExerciseLevel + exerciselevel);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                // Handle completion of the transaction here.
            }
        });
    }


    private void loadGifFromFirebaseStorage(String fileName) {
        String gifPath = "ExerciseVids/" + fileName + ".gif";
        StorageReference gifRef = mStorageRef.child(gifPath);

        int stringId = getResources().getIdentifier(fileName, "string", getPackageName());
        ExDialog.setText(getString(stringId));

        gifRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(com.linx.stress_free_app.ExercisePlayer.ExercisePlayerActivity3.this)
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