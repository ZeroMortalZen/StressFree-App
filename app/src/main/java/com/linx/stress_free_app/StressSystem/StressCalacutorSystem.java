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

    //HelperClass Var
    float painScore ;
    float TotalScreenTimeScore;
    float TotalAppUsageScore ;
    float BedTimeScore;

    //Vars
    boolean hasStresslevel = false;
    float HighStresslevels =1;
    float MediumStresslebls =2;
    float LowStresslevels =3;


    public void CalaculateStressLevel(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data as a User object
                HelperClass user = dataSnapshot.getValue(HelperClass.class);

                // Display the user's name and email in a TextView
                float painScore = user.getPain();
                float TotalScreenTimeScore = user.getTotalScreenTime();
                float TotalAppUsageScore = user.getTotalAppUsage();
                float BedTimeScore = user.getTime();


                //Debug
                String getTime= String.valueOf(helperClass.getTime());
                Log.d("Debug", getTime);


                //Logic
                if (painScore >= 0 && painScore <= 3) {
                    // do something here

                } else if (painScore >= 4 && painScore <= 6) {
                    // do something else here

                } else {
                    // do something else if pain is outside both ranges

                }

                //
                if (TotalScreenTimeScore >= 0 && TotalScreenTimeScore  <= 180) {
                    // do something here

                } else if (TotalScreenTimeScore  >= 240 && TotalScreenTimeScore  <= 360) {
                    // do something else here

                } else {
                    // do something else if pain is outside both ranges

                }


                if (TotalAppUsageScore >= 0 && TotalAppUsageScore <= 3) {
                    // do something here

                } else if (TotalAppUsageScore >= 4 && TotalAppUsageScore <= 6) {
                    // do something else here

                } else {
                    // do something else if pain is outside both ranges

                }


                if (BedTimeScore >= 12.0 && BedTimeScore<= 23.0) {
                    // do something here

                } else if (BedTimeScore >= 24.0 && BedTimeScore<= 6) {
                    // do something else here

                } else {
                    // do something else if pain is outside both ranges

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    }

}
