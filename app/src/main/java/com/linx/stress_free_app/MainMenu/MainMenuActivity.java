package com.linx.stress_free_app.MainMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.LoginActivity;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.ExerciseFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.HomeFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.MeditationFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.MusicFragment;
import com.linx.stress_free_app.NotificationSystem.NotificationReceiver;
import com.linx.stress_free_app.OnlineLeaderboard.UserProfile;
import com.linx.stress_free_app.OnlineLeaderboard.UserProfileAdapter;
import com.linx.stress_free_app.ProfileActivity;
import com.linx.stress_free_app.R;
import com.linx.stress_free_app.Settings.SettingsActivity;
import com.linx.stress_free_app.StressSystem.StressCalacutorSystem;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private Fragment currentFragment;
    FloatingActionButton fab;
    StressCalacutorSystem stressCalacutorSystem = new StressCalacutorSystem();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    DatabaseReference isFirstTimeRef = database.getReference("users").child(userId).child("isFirstTime");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        showTutorialSequence();

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
                startActivityForResult(intent, 1);
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


    //Tutorial
    private void showSequence1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome to the CalmCompass")
                .setMessage("The calmness that your app will guide users towards a more peaceful life. \n" +
                        "\n" +
                        "This is the main menu, where you can access different features like music, meditation, exercise let me explain more about the home page")
                .setIcon(R.mipmap.ic_launcher) // Add your icon resource here
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showSequence2();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSequence2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Home page")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("STARTSURVEY BUTTON: This will take you to a page where you can do a small survey where you do a small survey that will help us gauge your stress level. And from that survey will advise you on an activity in the app.\n" +
                        "\n" +
                        "GOAL PROGRESS: This where you can see your daily progress of activities that need to be done\n" +
                        "\n" +
                        "There are various News and articles about stress and mindfulness click on them will bring you to the website so you can read more of it\n" +
                        "\n" +
                        "Let me take you to the Exercise page")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setSelectedItemId(R.id.excerciseMenu);
                        showSequence3();
                    }
                })
                .show();
    }

    private void showSequence3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exercise")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("These are the Exercise Pages, where you can access exercise-related features.\n" +
                        "\n"+
                        "You have the 3 daily exercise that needs to be done in a span of 24hrs.\n" +
                        "\n"+
                        "When you have completed each you earn a point which goes to increase your rank in the leaderboard which will be explained later\n" +
                        "\n"+
                        "Below that there is more exercise to do if you're bored.\n")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setSelectedItemId(R.id.meditationMenu);
                        showSequence4();
                    }
                })
                .show();
    }



    private void showSequence4() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Meditation")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("This is the Meditation, where you can access meditation-related features. Again there 3 daily tasks that need to be done.\n" +
                        "\n" +
                        "On the breathing exercise page, a progress bar will indicate the remaining time until it is finished.\n" +
                        "\n" +
                        "Complete each task to gain to increase your place in the leaderboard.\n")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setSelectedItemId(R.id.listenMenu);
                        showSequence5();
                    }
                })
                .show();
    }

    private void showSequence5() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Music Icon")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("This music will allow you to listen to mindfulness music and melodies that are pulled.\n" +
                        "\n" +
                        "Once when you click, progress will increase and give you a score added to your overall score to display on the leaderboard.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }




    private void showTutorialSequence() {
        isFirstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isFirstTime = dataSnapshot.getValue(Boolean.class);

                if (isFirstTime == null || isFirstTime) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showSequence1();
                        }
                    }, 500); // 500 milliseconds delay

                    // Save the updated isFirstTime value to the database
                    isFirstTimeRef.setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Redirect to the sign-in or sign-up screen
            Intent signInIntent = new Intent(MainMenuActivity.this, LoginActivity.class);
            startActivity(signInIntent);
            finish();
        }
    }


}

