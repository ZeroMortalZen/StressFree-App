package com.linx.stress_free_app.AnimationController;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAnimation extends Animation {

   //private Context context;
   private ProgressBar progressBar;
   private TextView textView;
   private Button button;
   private  float from;
   private  float to;

   public ProgressBarAnimation(Button button, ProgressBar progressBar, TextView textView, float from ,float to){
       //this.context = context;
       this.button = button;
       this.progressBar= progressBar;
       this.textView= textView;
       this.from = from;
       this.to = to;

   }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value =from +(to - from)* interpolatedTime;
        progressBar.setProgress((int)value);
        textView.setText((int)value +" %");

        if(value == to){
            button.setVisibility(View.VISIBLE);


        }
    }
}
