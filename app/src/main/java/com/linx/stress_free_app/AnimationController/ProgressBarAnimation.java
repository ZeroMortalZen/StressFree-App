package com.linx.stress_free_app.AnimationController;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAnimation extends Animation {
    private Button btn;
    private ProgressBar progressBar;
    private TextView textView;
    private float from;
    private float to;
    private AnimationEndListener animationEndListener;

    public ProgressBarAnimation(Button btn, ProgressBar progressBar, TextView textView, float from, float to, AnimationEndListener listener) {
        this.btn = btn;
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;
        this.animationEndListener = listener;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView.setText((int) value + " %");
        if (value == to) {
            btn.setBackgroundResource(android.R.drawable.btn_default);
            btn.setEnabled(true);
        }
        if (interpolatedTime == 1) {
            animationEndListener.onAnimationEnd();
        }
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }
}
