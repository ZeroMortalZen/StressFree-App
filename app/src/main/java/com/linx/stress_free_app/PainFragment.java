package com.linx.stress_free_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.linx.stress_free_app.MainMenu.MainMenuActivity;
import com.linx.stress_free_app.StressSystem.HelperClass;

public class PainFragment extends Fragment {

   View view;
   TextView PainNumberView;
   Slider PainSlider;
   Button PainSubmit;
   HelperClass helperClass = new HelperClass();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pain, container, false);

        PainSlider =(Slider) view.findViewById(R.id.PainSlider);
        PainNumberView =(TextView) view.findViewById(R.id.PainNumberView);
        PainSubmit =(Button)view.findViewById(R.id.PainSubmit);

        PainSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                PainNumberView.setText(Float.toString(value));
                helperClass.setPain(Float.parseFloat(Float.toString(value)));
            }
        });

        PainSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String getPain= String.valueOf(helperClass.getPain());
                Toast.makeText(getActivity(),getPain,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                startActivity(intent);






            }
        });




        return view;
    }





}