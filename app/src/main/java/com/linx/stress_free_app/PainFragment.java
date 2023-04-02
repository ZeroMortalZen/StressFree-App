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

public class PainFragment extends Fragment {

   View view;
   TextView PainNumberView;
   Slider PainSlider;
   Button PainSubmit;
   HelperClass helperClass = new HelperClass();
   FirebaseDatabase database = FirebaseDatabase.getInstance();
   StressCalacutorSystem stressCalacutorSystem = new StressCalacutorSystem();


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
                String getPain = String.valueOf(helperClass.getPain());
                Toast.makeText(getActivity(), getPain, Toast.LENGTH_SHORT).show();

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
                        float pain = dataSnapshot.child("pain").getValue(Float.class);
                        float time = dataSnapshot.child("time").getValue(Float.class);
                        float totalAppUsage = dataSnapshot.child("totalAppUsage").getValue(Float.class);
                        float totalScreenTime = dataSnapshot.child("totalScreenTime").getValue(Float.class);

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





}