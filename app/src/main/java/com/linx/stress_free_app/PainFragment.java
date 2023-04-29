package com.linx.stress_free_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.MainMenu.MainMenuActivity;
import com.linx.stress_free_app.StressSystem.HelperClass;
import com.linx.stress_free_app.StressSystem.StressCalacutorSystem;
import com.nitish.typewriterview.TypeWriterView;

import android.animation.ArgbEvaluator;

public class PainFragment extends Fragment {
    int count =0;
    boolean isAnimating = true;
    private FragmentSubmitListener submitListener;

   View view;
   TextView PainNumberView;
   Slider PainSlider;
   Button PainSubmit;
   HelperClass helperClass = new HelperClass();
   FirebaseDatabase database = FirebaseDatabase.getInstance();
   StressCalacutorSystem stressCalacutorSystem = new StressCalacutorSystem();
   ImageView PainIcon;
   ImageView NonPainIcon;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSubmitListener) {
            submitListener = (FragmentSubmitListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentSubmitListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pain, container, false);

        PainSlider =(Slider) view.findViewById(R.id.PainSlider);
        PainNumberView =(TextView) view.findViewById(R.id.PainNumberView);
        PainSubmit =(Button)view.findViewById(R.id.PainSubmit);
        NonPainIcon =(ImageView)view.findViewById(R.id.imageNonPain);
        PainIcon =(ImageView)view.findViewById(R.id.imagePain);
        TypeWriterView typeWriterView =(TypeWriterView)view.findViewById(R.id.typeWriterView2);
        final String PainDialog = getResources().getString(R.string.Pain);


        //Animate Text
        typeWriterView.animateText(PainDialog);


        PainSlider.addOnChangeListener(new Slider.OnChangeListener() {
            ArgbEvaluator argbEvaluator = new ArgbEvaluator(); //ArgbEvaluator instance
            int startColor = Color.parseColor("#FFFFFF"); //  starting color of the PainIcon
            int endColor = Color.parseColor("#FF0000"); //  red color when the slider is at its maximum value

            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                PainNumberView.setText(Float.toString(value));
                helperClass.setPain(Float.parseFloat(Float.toString(value)));


                float ratio = value / 10; // Calculate the ratio of the current value to the maximum value
                int interpolatedColor = (int) argbEvaluator.evaluate(ratio, startColor, endColor); // Interpolate the color based on the ratio
                ColorStateList tintList = ColorStateList.valueOf(interpolatedColor);
                PainIcon.setImageTintList(tintList); // Apply the interpolated color to the PainIcon
            }
        });

        PainSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String getPain = String.valueOf(helperClass.getPain());
                Toast.makeText(getActivity(), getPain, Toast.LENGTH_SHORT).show();

                if (submitListener != null) {
                    submitListener.onSubmit(3);
                }

                // Store in Database
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();
                DatabaseReference userRef = database.getReference("users").child(userId);

                userRef.child("pain").setValue(helperClass.getPain());

                // Add ValueEventListener
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get the values from the database
                        Float painValue = dataSnapshot.child("pain").getValue(Float.class);
                        Float timeValue = dataSnapshot.child("time").getValue(Float.class);
                        Float totalAppUsageValue = dataSnapshot.child("totalAppUsage").getValue(Float.class);
                        Float totalScreenTimeValue = dataSnapshot.child("totalScreenTime").getValue(Float.class);

                        // Check if any of the values are null
                        if (painValue == null || timeValue == null || totalAppUsageValue == null || totalScreenTimeValue == null) {
                            Toast.makeText(getActivity(), "Mood Survey Has been Uncompleted", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        float pain = painValue.floatValue();
                        float time = timeValue.floatValue();
                        float totalAppUsage = totalAppUsageValue.floatValue();
                        float totalScreenTime = totalScreenTimeValue.floatValue();

                        // Check if the survey has been filled
                        if (pain >= 0.1 && time >= 0.1 && totalAppUsage >= 0.1 && totalScreenTime >= 0.1) {
                            helperClass.setHasStresslevel(true);

                            // Database connection & Store Values
                            userRef.child("hasStresslevel").setValue(true);

                            //Calaute Stresslevel
                            stressCalacutorSystem.CalaculateStressLevel();

                            Toast.makeText(getActivity(), "Mood Survey Has been Completed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Mood Survey Has been Uncompleted", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                    }
                });
            }
        });




        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        submitListener = null;
    }





}