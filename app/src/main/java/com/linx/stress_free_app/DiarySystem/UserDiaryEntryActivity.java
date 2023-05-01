package com.linx.stress_free_app.DiarySystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.GetStarted;
import com.linx.stress_free_app.MoodSurveyActivity;
import com.linx.stress_free_app.ProfileActivity;
import com.linx.stress_free_app.R;

public class UserDiaryEntryActivity extends AppCompatActivity {

    private TextView tvDiaryEntry;
    private DatabaseReference diaryEntriesRef;
    private Button BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_diary_entry);

        tvDiaryEntry = findViewById(R.id.tv_diary_entry);
        BackBtn = findViewById(R.id.DiaryBackBtn);

        String userId = getIntent().getStringExtra("userId");
        diaryEntriesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("diary_entries");

        fetchLatestDiaryEntry();
        
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDiaryEntryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchLatestDiaryEntry() {
        diaryEntriesRef.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    String diaryEntry = entrySnapshot.child("content").getValue(String.class);
                    tvDiaryEntry.setText(diaryEntry);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
