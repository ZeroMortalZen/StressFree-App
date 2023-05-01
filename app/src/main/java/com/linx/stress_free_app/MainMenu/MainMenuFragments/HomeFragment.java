package com.linx.stress_free_app.MainMenu.MainMenuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.GoalSystem.ProgressAdapter;
import com.linx.stress_free_app.NewsSystem.NewsAdapter;
import com.linx.stress_free_app.NewsSystem.NewsArticle;
import com.linx.stress_free_app.NewsSystem.NewsResponse;
import com.linx.stress_free_app.R;
import com.linx.stress_free_app.RestAPI.NewsAPI;
import com.linx.stress_free_app.RestAPI.RetrofitInstance;
import com.linx.stress_free_app.Settings.SettingsActivity;
import com.linx.stress_free_app.StressSystem.Item;
import com.linx.stress_free_app.StressSystem.RecommendAdapter;
import com.linx.stress_free_app.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private static final String API_KEY = "4d64cb5b80c74e84860efa95a88e68b3";
    private RecyclerView recyclerView;
    private RecyclerView RecommendrecyclerView;
    private TextView RecoView;
    private ImageButton settingsbutton;
    private RecyclerView GoalrecyclerView;
    private ProgressAdapter progressAdapter;
    private SharedViewModel sharedViewModel;
    private Timer resetTimer;
    private ImageButton notfiBtn;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users").child(userId);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        fetchUserStressLevel();
        RecoView = rootView.findViewById(R.id.goalText);

        //Database


        settingsbutton = rootView.findViewById(R.id.settingbutton);
        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });





        //Goal View
        GoalrecyclerView = rootView.findViewById(R.id.GoalrecyclerView);
        GoalrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with the initial progress values (0, 0)
        progressAdapter = new ProgressAdapter(0, 0);
        GoalrecyclerView.setAdapter(progressAdapter);

        // Instantiate the shared ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe yoga progress value changes in the shared ViewModel
        sharedViewModel.getYogaProgress().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer yogaProgress) {
                // Update the adapter with the new yoga progress value
                progressAdapter.setYogaProgress(yogaProgress);

                // Update the progress value in the Firebase Realtime Database
                userRef.child("yogaProgress").setValue(yogaProgress);
            }
        });


        // Observe meditation progress value changes in the shared ViewModel
        sharedViewModel.getMeditationProgress().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer meditationProgress) {
                // Update the adapter with the new meditation progress value
                progressAdapter.setMeditationProgress(meditationProgress);

                // Update the progress value in the Firebase Realtime Database
                userRef.child("meditationProgress").setValue(meditationProgress);
            }
        });


        // Fetch yoga progress from the Firebase Realtime Database
        userRef.child("yogaProgress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer yogaProgress = dataSnapshot.getValue(Integer.class);
                if (yogaProgress != null) {
                    // Update the shared ViewModel with the fetched yoga progress value
                    sharedViewModel.setYogaProgress(yogaProgress);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

       // Fetch meditation progress from the Firebase Realtime Database
        userRef.child("meditationProgress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer meditationProgress = dataSnapshot.getValue(Integer.class);
                if (meditationProgress != null) {
                    // Update the shared ViewModel with the fetched meditation progress value
                    sharedViewModel.setMeditationProgress(meditationProgress);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        // Start the Timer
        //startTimer();

        RecommendrecyclerView = rootView.findViewById(R.id.RecoRecyclerView);
        recyclerView = rootView.findViewById(R.id.NewsRecyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);




        NewsAPI newsAPI = RetrofitInstance.getRetrofitInstance().create(NewsAPI.class);
        Call<NewsResponse> call = newsAPI.getNews("stress+AND+meditation", API_KEY);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    List<NewsArticle> articles = response.body().getArticles();
                    recyclerView.setAdapter(new NewsAdapter(getActivity(),articles));
                } else {
                    Log.e("NewsFragment", "Error fetching news articles");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("NewsFragment", "Error: " + t.getMessage());
            }
        });



        return rootView;
    }


    private void fetchUserStressLevel() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer stressLevel = dataSnapshot.child("stressLevelScore").getValue(Integer.class);
                if (stressLevel != null) {
                    updateRecommendations(stressLevel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private void updateRecommendations(int stressLevel) {
        // Update RecoView text based on the stress level
        RecoView.setText(getString(R.string.your_stress_level_text, stressLevel));

        // Update the RecommendrecyclerView with appropriate data based on the stress level
        List<Item> items = new ArrayList<>();
        switch (stressLevel) {
            case 1:
                // Add items for stress level 1
                items.add(new Item(R.drawable.tutorial, "Started Stress Survery"));
                items.add(new Item(R.drawable.icon1, "You should listen to so light music "));
                break;
            case 2:
                // Add items for stress level 2
                items.add(new Item(R.drawable.tutorial, "Started Stress Survery"));
                items.add(new Item(R.drawable.icon2, "Bit of Stress huh do some light yoga sessions"));
                break;
            case 3:
                // Add items for stress level 3
                items.add(new Item(R.drawable.tutorial, "Started Stress Survery"));
                items.add(new Item(R.drawable.icon3, "WOW you have Some serious Stress have u done your breathing exercises"));
                break;
            default:
                break;
        }

        // Update the RecommendrecyclerView
        RecommendrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecommendrecyclerView.setHasFixedSize(true);
        RecommendrecyclerView.setAdapter(new RecommendAdapter(items, getActivity()));
    }


    private void startTimer() {
        // Start the reset timer
        resetTimer = new Timer();
        resetTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Reset the progress values and update the database
                sharedViewModel.setYogaProgress(0);
                sharedViewModel.setMeditationProgress(0);
                userRef.child("yogaProgress").setValue(0);
                userRef.child("meditationProgress").setValue(0);
            }
        }, 24 * 60 * 60 * 1000); // 24 hours in milliseconds
    }




}
