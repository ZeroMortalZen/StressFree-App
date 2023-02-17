package com.linx.stress_free_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

public class PainFragment extends Fragment {

   View view;
   TextView PainNumberView;
   Slider PainSlider;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pain, container, false);

        PainSlider =(Slider) view.findViewById(R.id.PainSlider);
        PainNumberView =(TextView) view.findViewById(R.id.PainNumberView);

        PainSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                PainNumberView.setText(Float.toString(value));
            }
        });






        return view;
    }


}