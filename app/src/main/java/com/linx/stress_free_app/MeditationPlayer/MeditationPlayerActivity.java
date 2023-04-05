package com.linx.stress_free_app.MeditationPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linx.stress_free_app.R;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MeditationPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Handler handler;
    private long elapsedTime;
    private long targetTime;
    private int userScore;
    private Runnable updateElapsedTime;
    private ImageView step1ImageView;
    private OnStepCompletedListener stepCompletedListener;
    private ProgressBar progressBar;
    int medlevel;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    DatabaseReference userRef = database.getReference("users").child(userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_player);
        progressBar = findViewById(R.id.progress_bar);

        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.morningmed).into(imageView);

        mediaPlayer = MediaPlayer.create(this, R.raw.breathingex1);
        handler = new Handler();
        startPlaying();
        elapsedTime = 0;
        targetTime = TimeUnit.SECONDS.toMillis(20); // Set the target time, e.g., 10 seconds
        userScore = 0;

        // Set the MediaPlayer to loop the audio clip
        mediaPlayer.setLooping(true);


    }

    public void setOnStepCompletedListener(OnStepCompletedListener listener) {
        this.stepCompletedListener = listener;
    }

    private void startPlaying() {
        mediaPlayer.start();
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
                    mediaPlayer.pause();




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
        mediaPlayer.stop();
        handler.removeCallbacks(updateElapsedTime);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void storeDataInFirebase(long elapsedTime, int medlevel) {
        userRef.child("mediationTime").setValue(elapsedTime);
        userRef.child("medLevel").setValue(medlevel);
    }


}