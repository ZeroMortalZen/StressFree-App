package com.linx.stress_free_app.MeditationPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.linx.stress_free_app.R;
import android.media.MediaPlayer;
import android.os.Handler;
import java.util.concurrent.TimeUnit;

public class MeditationPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Handler handler;
    private long elapsedTime;
    private long targetTime;
    private int userScore;
    private Runnable updateElapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_player);

        mediaPlayer = MediaPlayer.create(this, R.raw.BreathingEx1);
        handler = new Handler();
        elapsedTime = 0;
        targetTime = TimeUnit.SECONDS.toMillis(10); // Set the target time, e.g., 10 seconds
        userScore = 0;

        // Set the MediaPlayer to loop the audio clip
        mediaPlayer.setLooping(true);

    }

    private void startPlaying() {
        mediaPlayer.start();
        updateElapsedTime = new Runnable() {
            @Override
            public void run() {
                elapsedTime += 1000; // Increment elapsed time by 1 second (1000 milliseconds)

                if (elapsedTime >= targetTime) {
                    userScore += 1;
                    elapsedTime = 0; // Reset the elapsed time counter
                }

                handler.postDelayed(this, 1000); // Schedule the next update after 1 second
            }
        };

        handler.post(updateElapsedTime); // Start updating the elapsed time
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