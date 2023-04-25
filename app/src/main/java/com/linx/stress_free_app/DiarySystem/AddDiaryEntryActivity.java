package com.linx.stress_free_app.DiarySystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linx.stress_free_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddDiaryEntryActivity extends AppCompatActivity {

    // UI elements for title and content input, e.g., EditText
    private EditText etTitle;
    private EditText etContent;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary_entry);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        Button btnSaveEntry = findViewById(R.id.btnSaveEntry);
         btnSaveEntry.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();

            // Save diary entry to Firebase Database
            saveDiaryEntry(title, content);
        });

    }

    private void saveDiaryEntry(String title, String content) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Handle unauthenticated user
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userDiaryEntriesRef = database.child("users").child(userId).child("diary_entries");

        String entryId = userDiaryEntriesRef.push().getKey();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        DiaryEntry diaryEntry = new DiaryEntry(title, content, date);

        userDiaryEntriesRef.child(entryId).setValue(diaryEntry)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }





}