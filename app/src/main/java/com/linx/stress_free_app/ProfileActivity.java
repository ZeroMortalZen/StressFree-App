package com.linx.stress_free_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.linx.stress_free_app.DiarySystem.AddDiaryEntryActivity;
import com.linx.stress_free_app.DiarySystem.DiaryFragment;
import com.linx.stress_free_app.MainMenu.MainMenuActivity;
import com.linx.stress_free_app.OnlineLeaderboard.LeaderboardFragment;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView medalRank;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button saveChangesButton;
    private Button DiaryButton;

    private static final int SELECT_PROFILE_PICTURE_REQUEST_CODE = 100;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    DatabaseReference userRef = database.getReference("users").child(userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Add the LeaderboardFragment to the container
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        fragmentTransaction.replace(R.id.leaderboard_fragment_container, leaderboardFragment);
        fragmentTransaction.commit();

        profileImage = findViewById(R.id.profile_image);
        medalRank = findViewById(R.id.medal_rank);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        saveChangesButton = findViewById(R.id.save_changes_button);
        //DiaryButton = findViewById(R.id.Diarybutton);

        fetchUserDataAndPopulateUI();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PROFILE_PICTURE_REQUEST_CODE);
            }
        });


        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.diaryFragment:
                        selectedFragment = new DiaryFragment();
                        break;
                    case R.id.graphFragment:
                        //selectedFragment = new GraphFragment();
                        break;
                    case R.id.leaderboardFragment:
                        selectedFragment = new LeaderboardFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.leaderboard_fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });


        //DiaryButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
               // Intent intent = new Intent(ProfileActivity.this, AddDiaryEntryActivity.class);
               // startActivity(intent);
            //}
        //});


    }

    private void fetchUserDataAndPopulateUI() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue(String.class);
                String password = dataSnapshot.child("password").getValue(String.class);
                int medLevel = dataSnapshot.child("medLevel").getValue(Integer.class);
                int exerciseLevel = dataSnapshot.child("exerciselevel").getValue(Integer.class);
                int musicLevel = dataSnapshot.child("musicLevel").getValue(Integer.class);
                String profilePicUrl = dataSnapshot.child("profilePicUrl").getValue(String.class);

                emailEditText.setText(email);
                passwordEditText.setText(password);

                // Set the medal rank
                String medalRankString = calculateMedalRank(medLevel, exerciseLevel, musicLevel);
                medalRank.setText(medalRankString);
                storeMedalRankInDatabase(medalRankString);

                // Load the profile picture
                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    Glide.with(ProfileActivity.this)
                            .load(profilePicUrl)
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private String calculateMedalRank(int medLevel, int exerciseLevel, int musicLevel) {
        String medalRank = "no rank";
        int lowestValue = Math.min(Math.min(medLevel, exerciseLevel), musicLevel);

        if (lowestValue >= 10 && lowestValue < 20) {
            medalRank = "bronze";
        } else if (lowestValue >= 20 && lowestValue < 30) {
            medalRank = "silver";
        } else if (lowestValue >= 30) {
            medalRank = "gold";
        }

        return medalRank;
    }

    private Uri newProfilePicUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PROFILE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            newProfilePicUri = data.getData();
            profileImage.setImageURI(newProfilePicUri);
        }
    }

    private void updateUserData() {
        String newEmail = emailEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();

        // Update email and password in the Firebase Realtime Database
        userRef.child("email").setValue(newEmail);

        // Update email and password in Firebase Authentication
        currentUser.updateEmail(newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Email updated successfully
                        Toast.makeText(ProfileActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update email
                        Toast.makeText(ProfileActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                    }
                });

        currentUser.updatePassword(newPassword)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Password updated successfully
                        Toast.makeText(ProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update password
                        Toast.makeText(ProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                });

        if (newProfilePicUri != null) {
            // Update the profile picture in Firebase Storage
            StorageReference profilePicRef = FirebaseStorage.getInstance().getReference("profile_pictures/" + userId + ".jpg");
            profilePicRef.putFile(newProfilePicUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded profile picture
                            profilePicRef.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Update the profilePicUrl in the Firebase Realtime Database
                                            String newProfilePicUrl = uri.toString();
                                            userRef.child("profilePicUrl").setValue(newProfilePicUrl)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ProfileActivity.this, "Profile picture updated successfully", Toast.LENGTH_SHORT)
                                                                    .show();

                                                            // Load the new profile picture into the ImageView
                                                            Glide.with(ProfileActivity.this)
                                                                    .load(newProfilePicUrl)
                                                                    .into(profileImage);
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
        }
    }

    private void storeMedalRankInDatabase(String medalRank) {
        userRef.child("medalRank").setValue(medalRank)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Successfully updated medalRank in the database
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update medalRank in the database
                    }
                });
    }
}


