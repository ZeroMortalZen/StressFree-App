package com.linx.stress_free_app.DiarySystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.R;

import java.util.ArrayList;
import java.util.List;

public class DiaryFragment extends Fragment {

    private Button DiaryButton;
    private static final int ADD_DIARY_ENTRY_REQUEST_CODE = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        DiaryButton = view.findViewById(R.id.DiaryButton);
        loadDiaryEntries(view);


        DiaryButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         Intent intent = new Intent(getActivity(), AddDiaryEntryActivity.class);
            startActivityForResult(intent, ADD_DIARY_ENTRY_REQUEST_CODE);
        }
        });


        return view;
    }

    private void setupRecyclerView(View view, List<DiaryEntry> diaryEntries, String userEmail, String profilePicUrl) {
        RecyclerView rvDiaryEntries = view.findViewById(R.id.rvDiaryEntries);
        rvDiaryEntries.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Handle unauthenticated user
            return;
        }


        DiaryEntryAdapter adapter = new DiaryEntryAdapter(getActivity(), diaryEntries, userEmail, profilePicUrl);
        rvDiaryEntries.setAdapter(adapter);
    }

    private void loadDiaryEntries(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Handle unauthenticated user
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = database.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profilePicUrl = dataSnapshot.child("profilePicUrl").getValue(String.class);
                String userEmail = currentUser.getEmail();

                DatabaseReference diaryEntriesRef = userRef.child("diary_entries");
                diaryEntriesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<DiaryEntry> diaryEntries = new ArrayList<>();
                        for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                            DiaryEntry diaryEntry = entrySnapshot.getValue(DiaryEntry.class);
                            diaryEntries.add(diaryEntry);
                        }
                        // Set up RecyclerView with the diary entries list
                        setupRecyclerView(view, diaryEntries, userEmail, profilePicUrl);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle error
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

}

