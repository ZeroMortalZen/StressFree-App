package com.linx.stress_free_app.DiarySystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.R;

public class UserDiaryEntryActivity extends AppCompatActivity {

    private TextView tvDiaryEntry;
    private DatabaseReference diaryEntriesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_diary_entry);

        tvDiaryEntry = findViewById(R.id.tv_diary_entry);

        String userId = getIntent().getStringExtra("userId");
        diaryEntriesRef = FirebaseDatabase.getInstance().getReference("diaryEntries").child(userId);

        fetchLatestDiaryEntry();
    }

    private void fetchLatestDiaryEntry() {
        diaryEntriesRef.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    String diaryEntry = entrySnapshot.child("entry").getValue(String.class);
                    tvDiaryEntry.setText(diaryEntry);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
