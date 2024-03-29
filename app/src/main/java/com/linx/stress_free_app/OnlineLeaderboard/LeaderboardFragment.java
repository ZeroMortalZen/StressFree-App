package com.linx.stress_free_app.OnlineLeaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linx.stress_free_app.DiarySystem.UserDiaryEntryActivity;
import com.linx.stress_free_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardFragment extends Fragment implements LeaderboardAdapter.OnUserItemClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference usersRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        fetchLeaderboardData();

        return view;
    }

    private void fetchLeaderboardData() {
        progressBar.setVisibility(View.VISIBLE);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    String profilePicUrl = userSnapshot.child("profilePicUrl").getValue(String.class);

                    user.setUserId(userSnapshot.getKey()); // Set the userId

                    user.setExerciseLevel(userSnapshot.child("exerciselevel").getValue(Double.class));
                    user.setMusicLevel(userSnapshot.child("musicLevel").getValue(Double.class));
                    user.setMedLevel(userSnapshot.child("medLevel").getValue(Double.class));

                    Log.d("LeaderboardFragment", "Profile Pic URL: " + profilePicUrl);

                    if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                        user.setProfilePicRef(profilePicUrl);
                    }

                    userList.add(user);
                }

                // Sort userList by the highest average of exerciseLevel, musicLevel, medLevel,
                // and then by the alphabetical order of the email
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User u1, User u2) {
                        double avg1 = (u1.getExerciseLevel() + u1.getMusicLevel() + u1.getMedLevel()) / 3.0;
                        double avg2 = (u2.getExerciseLevel() + u2.getMusicLevel() + u2.getMedLevel()) / 3.0;

                        if (Double.compare(avg2, avg1) != 0) {
                            return Double.compare(avg2, avg1);
                        } else {
                            return u1.getEmail().compareToIgnoreCase(u2.getEmail());
                        }
                    }
                });

                LeaderboardAdapter adapter = new LeaderboardAdapter(userList, LeaderboardFragment.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }




    public void onUserItemClick(String userId) {
        Intent intent = new Intent(getContext(), UserDiaryEntryActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }



}


