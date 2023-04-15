package com.linx.stress_free_app;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JavaScriptInterface {
    private ProgressBar progressBar;
    private int score = 0;
    private int watchedTime = 0;
    private boolean videoWatched = false;
    private static final int UPDATE_INTERVAL = 10000; // 10 seconds
    private long lastUpdateTime = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    DatabaseReference userRef = database.getReference("users").child(userId);

    public JavaScriptInterface(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @JavascriptInterface
    public void updateProgress(final float progress) {
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress((int) progress);
            }
        });
    }

    @JavascriptInterface
    public void videoEnded() {
        if (!videoWatched) {
            score = 1;
            videoWatched = true;
            // Perform any actions you want after the video ends and the user gets a score of 1
        } else {
            // The video has already been watched; do not increase the score
        }
    }

    @JavascriptInterface
    public void updateWatchedTime(int currentTime) {
        Log.d("MyApp", "updateWatchedTime called with currentTime: " + currentTime);

        watchedTime = currentTime;
        progressBar.setProgress(watchedTime);

        if (watchedTime >= 10) { // 2 hours in seconds
            score += 1;
            watchedTime = 0;
        }

        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastUpdateTime >= UPDATE_INTERVAL) {
            Log.d("MyApp", "Storing watchedTime: " + watchedTime + " and score: " + score);
            storeWatchedTimeAndScoreInFirebase(watchedTime, score);
            lastUpdateTime = currentTimeMillis;
        }
    }


    private void storeWatchedTimeAndScoreInFirebase(int watchedTime, int score) {
        userRef.child("musicTime").setValue(watchedTime, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved: " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
            }
        });

        userRef.child("musicLevel").setValue(score, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved: " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
            }
        });
    }



}
