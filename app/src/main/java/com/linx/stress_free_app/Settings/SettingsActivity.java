package com.linx.stress_free_app.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.MainMenu.MainMenuActivity;
import com.linx.stress_free_app.R;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private ImageButton settingsbckbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsbckbtn = findViewById(R.id.settingbackbutton);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // Handle user not signed in
            return;
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(currentUser.getUid()).child("isFirstTime");

        Switch tutorialSwitch = findViewById(R.id.tutorial_switch);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFirstTime = dataSnapshot.exists() ? dataSnapshot.getValue(Boolean.class) : true;

                tutorialSwitch.setChecked(!isFirstTime);

                if (isFirstTime) {
                    showSequence7();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        tutorialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                databaseReference.setValue(!isChecked);
            }
        });

        settingsbckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showSequence7() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings Icon")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("This is the Settings menu where you can adjust various things.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can call any further actions or simply end the tutorial here
                        // Save the updated isFirstTime value to the database

                    }
                })
                .show();
    }
}
