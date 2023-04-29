package com.linx.stress_free_app.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.linx.stress_free_app.R;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch tutorialSwitch = findViewById(R.id.tutorial_switch);
        SharedPreferences sharedPreferences = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        tutorialSwitch.setChecked(!isFirstTime);

        tutorialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstTime", !isChecked);
                editor.apply();
            }
        });
    }
}
