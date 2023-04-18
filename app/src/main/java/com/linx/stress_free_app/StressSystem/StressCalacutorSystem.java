package com.linx.stress_free_app.StressSystem;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.EditProfileActivity;
import com.linx.stress_free_app.ProfileActivity;

public class StressCalacutorSystem {
    HelperClass helperClass = new HelperClass();


    //Vars
    boolean hasStresslevel = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public void CalaculateStressLevel(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference userRef = database.getReference("users").child(userId);

       userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data as a User object
                HelperClass user = dataSnapshot.getValue(HelperClass.class);

                // Display the user's name and email in a TextView
                // Get the values from the database
               float painScore = dataSnapshot.child("pain").getValue(Float.class);
               float BedTimeScore = dataSnapshot.child("time").getValue(Float.class);
               float TotalAppUsageScore = dataSnapshot.child("totalAppUsage").getValue(Float.class);
               float TotalScreenTimeScore = dataSnapshot.child("totalScreenTime").getValue(Float.class);

               float Score =0;
               float TotalScore;

                //Debug
                String getTime= String.valueOf(helperClass.getTime());
                Log.d("Debug", getTime);


                //Logic
                if (painScore >= 0 && painScore <= 3) {
                     TotalScore = Score+1;

                } else if (painScore >= 4 && painScore <= 6) {
                    // do something else here
                    TotalScore = Score+2;

                } else {
                    // do something else if pain is outside both ranges
                    TotalScore = Score+3;

                }

                //
                if (TotalScreenTimeScore >= 0 && TotalScreenTimeScore  <= 180) {
                    // do something here
                    TotalScore = Score+1;

                } else if (TotalScreenTimeScore  >= 240 && TotalScreenTimeScore  <= 360) {
                    // do something else here
                    TotalScore = Score+2;

                } else {
                    // do something else if pain is outside both ranges
                    TotalScore = Score+3;

                }


                if (TotalAppUsageScore >= 0 && TotalAppUsageScore <= 3) {
                    // do something here
                    TotalScore = Score+1;

                } else if (TotalAppUsageScore >= 4 && TotalAppUsageScore <= 6) {
                    // do something else here
                    TotalScore = Score+2;

                } else {
                    // do something else if pain is outside both ranges
                    TotalScore = Score+3;

                }


                if (BedTimeScore >= 12.0 && BedTimeScore<= 23.0) {
                    // do something here
                    TotalScore = Score+1;

                } else if (BedTimeScore >= 24.0 && BedTimeScore<= 6) {
                    // do something else here
                    TotalScore = Score+2;

                } else {
                    // do something else if pain is outside both ranges
                    TotalScore = Score+3;

                }

                if (TotalScore >=0 && TotalScore <=4){
                    //Assign  Stresslevel core to 1
                    userRef.child("stressLevelScore").setValue(1);

                    //Display setText to  talk about assign Stresslevel

                }
                else if (TotalScore >=5 && TotalScore <=8){
                    //Assign StresslevelScore to 2
                    userRef.child("stressLevelScore").setValue(2);

                }
                else{
                    //Assign StresslevelScore to 3;
                    userRef.child("stressLevelScore").setValue(3);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    }

}
