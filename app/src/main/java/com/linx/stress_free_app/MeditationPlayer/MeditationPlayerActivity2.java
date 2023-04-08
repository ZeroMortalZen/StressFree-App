package com.linx.stress_free_app.MeditationPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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
import com.linx.stress_free_app.R;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MeditationPlayerActivity2 extends AppCompatActivity {

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
    int medlevel;
    ImageView AfternoonMedGif;
    private StorageReference mStorageRef;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    DatabaseReference userRef = database.getReference("users").child(userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_player2);
        progressBar = findViewById(R.id.progress_bar);

        // Initialize Firebase Storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        AfternoonMedGif = findViewById(R.id.imageViewNight);
        loadGifFromFirebaseStorage();
        loadAudioFromFirebaseStorageAndPlay();

        mediaPlayerLocal = MediaPlayer.create(this, R.raw.afternoonbreathing);
        handler = new Handler();
        startPlaying();
        elapsedTime = 0;
        targetTime = TimeUnit.SECONDS.toMillis(20); // Set the target time, e.g., 10 seconds
        userScore = 0;

        // Set the MediaPlayer to loop the audio clip
        mediaPlayerLocal.setLooping(true);


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
                        medlevel = 1;
                    } else if (userScore >= 6 && userScore <= 10) {
                        medlevel = 2;
                    } else if (userScore >= 11) {
                        medlevel = 3;
                    }

                    // Store data in Firebase when the target time is reached
                    storeDataInFirebase(elapsedTime, medlevel);

                    // Reset the elapsedTime after storing the data
                    elapsedTime = 0;
                    //mediaPlayerLocal.pause();




                    if (stepCompletedListener != null) {
                        stepCompletedListener.onStepCompleted(2);
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("step", 2);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    // Store data in Firebase periodically (e.g., every 5 seconds)
                    if (elapsedTime % 5000 == 0) {
                        storeDataInFirebase(elapsedTime, medlevel);
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

    private void storeDataInFirebase(long elapsedTime, int medlevel) {
        userRef.child("mediationTime").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Long currentValue = mutableData.getValue(Long.class);
                if (currentValue == null) {
                    mutableData.setValue(elapsedTime);
                } else {
                    mutableData.setValue(currentValue + elapsedTime);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                // Transaction completed
            }
        });

        userRef.child("medLevel").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(medlevel);
                } else {
                    mutableData.setValue(currentValue + medlevel);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                // Transaction completed
            }
        });
    }


    private void loadGifFromFirebaseStorage() {
        String gifUrl = "https://firebasestorage.googleapis.com/v0/b/stress-free-app-df840.appspot.com/o/MedGifs%2FMorningGifs%2Fmornin.gif?alt=media";

        Glide.with(MeditationPlayerActivity2.this)
                .asGif()
                .load(gifUrl)
                .into(AfternoonMedGif);
    }

    private void loadAudioFromFirebaseStorageAndPlay() {
        String audioPath = "Voiceovers/afternoonVoice/afternoonvoice.wav";
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