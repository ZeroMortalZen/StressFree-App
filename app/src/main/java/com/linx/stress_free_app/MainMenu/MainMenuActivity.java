package com.linx.stress_free_app.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.ExerciseFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.HomeFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.MeditationFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.MusicFragment;
import com.linx.stress_free_app.NotificationSystem.NotificationReceiver;
import com.linx.stress_free_app.OnlineLeaderboard.UserProfile;
import com.linx.stress_free_app.OnlineLeaderboard.UserProfileAdapter;
import com.linx.stress_free_app.ProfileActivity;
import com.linx.stress_free_app.R;
import com.linx.stress_free_app.StressSystem.StressCalacutorSystem;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private Fragment currentFragment;
    FloatingActionButton fab ;
    StressCalacutorSystem  stressCalacutorSystem = new StressCalacutorSystem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        createNotificationChannels();
        scheduleNotifications();


        // Set up the ProfileRecycle RecyclerView
        RecyclerView profileRecycle = findViewById(R.id.ProfileRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        profileRecycle.setLayoutManager(layoutManager);


        // Create an empty list for the user's profile data
        List<UserProfile> userProfiles = new ArrayList<>();
        UserProfileAdapter userProfileAdapter = new UserProfileAdapter(this, userProfiles);
        profileRecycle.setAdapter(userProfileAdapter);



        // Get the current user's uid
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Create a reference to the user's profile data in the Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            // Set up a listener to load the user's profile data
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String profileImageUrl = dataSnapshot.child("profilePicUrl").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String rank = dataSnapshot.child("medalRank").getValue(String.class);

                    // Update the userProfiles list and notify the adapter
                    userProfiles.clear();
                    if (profileImageUrl != null && email != null && rank != null) {
                        userProfiles.add(new UserProfile(profileImageUrl, email, rank));
                    }
                    userProfileAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Load the first fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, currentFragment).commit();
        Intent intent = new Intent(this, ProfileActivity.class);




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.homeMenu:
                currentFragment = new HomeFragment();
                break;
            case R.id.meditationMenu:
                currentFragment = new MeditationFragment();
                break;
            case R.id.excerciseMenu:
                currentFragment = new ExerciseFragment();
                break;

            case R.id.listenMenu:
                currentFragment = new MusicFragment();

                break;
        }

        fragmentManager.beginTransaction().replace(R.id.fragment_container, currentFragment).commit();
        return true;
    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "channel1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    "channel2",
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }



    private void scheduleNotifications() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // For 4-hour notifications
        Intent intent1 = new Intent(this, NotificationReceiver.class);
        intent1.putExtra("channelId", "channel1");
        PendingIntent pendingIntent1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent1 = PendingIntent.getBroadcast(this, 1, intent1, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent1 = PendingIntent.getBroadcast(this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (4 * 60 * 60 * 1000), (4 * 60 * 60 * 1000), pendingIntent1);

        // For 24-hour notifications
        Intent intent2 = new Intent(this, NotificationReceiver.class);
        intent2.putExtra("channelId", "channel2");
        PendingIntent pendingIntent2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent2 = PendingIntent.getBroadcast(this, 2, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent2 = PendingIntent.getBroadcast(this, 2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (24 * 60 * 60 * 1000), (24 * 60 * 60 * 1000), pendingIntent2);
    }








}