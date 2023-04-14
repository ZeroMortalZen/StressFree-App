package com.linx.stress_free_app.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.ExerciseFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.HomeFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.MeditationFragment;
import com.linx.stress_free_app.MainMenu.MainMenuFragments.MusicFragment;
import com.linx.stress_free_app.ProfileActivity;
import com.linx.stress_free_app.R;
import com.linx.stress_free_app.StressSystem.StressCalacutorSystem;

public class MainMenuActivity extends AppCompatActivity {

    private Fragment currentFragment;
    FloatingActionButton fab ;
    StressCalacutorSystem  stressCalacutorSystem = new StressCalacutorSystem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


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
                // Starts Profile Activity
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





}