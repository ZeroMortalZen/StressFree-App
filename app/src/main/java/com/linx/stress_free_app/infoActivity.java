package com.linx.stress_free_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.linx.stress_free_app.InfoSystem.InfoAdapter;
import com.linx.stress_free_app.InfoSystem.InfoItem;
import com.linx.stress_free_app.MainMenu.MainMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class infoActivity extends AppCompatActivity {

    private Button backbtn;
    private RecyclerView infoRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        backbtn = findViewById(R.id.bckbtn);
        infoRecyclerView = findViewById(R.id.infoRecyclerView);

        // Create a list of InfoItem objects
        List<InfoItem> infoItems = new ArrayList<>();
        infoItems.add(new InfoItem(R.drawable.appusage, "App usage and stress", "The relationship between addictive use of social media, narcissism, and self-esteem", "https://www.sciencedirect.com/journal/computers-in-human-behavior"));
        infoItems.add(new InfoItem(R.drawable.screentimeicon, "Screen time and stress:", "Preventive Medicine Reports (2018) Associations between screen time and lower psychological well-being among children and adolescent", "https://www.sciencedirect.com/journal/preventive-medicine-reports"));
        infoItems.add(new InfoItem(R.drawable.pain, "Pains and stress:", " Information between chronic pain and stress is from the American Psychological Association (APA).", "www.apa.org"));
        infoItems.add(new InfoItem(R.drawable.day, "Sleep patterns and stress:", "A study published in the journal \"Sleep\" (2015) found that people who reported shorter sleep durations and poorer sleep quality had higher levels of perceived stress.", "https://pubmed.ncbi.nlm.nih.gov/22210572/"));

        // Add more items as needed

        // Set up the RecyclerView with a LinearLayoutManager and the custom adapter
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        infoRecyclerView.setAdapter(new InfoAdapter(this, infoItems));



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(infoActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
