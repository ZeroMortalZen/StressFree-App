package com.linx.stress_free_app;

import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

public class JavaScriptInterface {
    private ProgressBar progressBar;
    private int score = 0;
    private boolean videoWatched = false;

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
}