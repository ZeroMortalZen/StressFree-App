package com.linx.stress_free_app.MeditationPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
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


}