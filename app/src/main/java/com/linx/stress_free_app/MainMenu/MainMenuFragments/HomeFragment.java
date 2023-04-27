package com.linx.stress_free_app.MainMenu.MainMenuFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.NewsSystem.NewsAdapter;
import com.linx.stress_free_app.NewsSystem.NewsArticle;
import com.linx.stress_free_app.NewsSystem.NewsResponse;
import com.linx.stress_free_app.R;
import com.linx.stress_free_app.AnimationController.RestAPI.NewsAPI;
import com.linx.stress_free_app.AnimationController.RestAPI.RetrofitInstance;
import com.linx.stress_free_app.StressSystem.Item;
import com.linx.stress_free_app.StressSystem.RecommendAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private static final String API_KEY = "4d64cb5b80c74e84860efa95a88e68b3";
    private RecyclerView recyclerView;
    private RecyclerView RecommendrecyclerView;
    private TextView RecoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        fetchUserStressLevel();
        RecoView = rootView.findViewById(R.id.whyReco);

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
                items.add(new Item(R.drawable.icon1, "Item 1 for stress level 1"));
                break;
            case 2:
                // Add items for stress level 2
                items.add(new Item(R.drawable.icon2, "Item 1 for stress level 2"));
                break;
            case 3:
                // Add items for stress level 3
                items.add(new Item(R.drawable.icon3, "Item 1 for stress level 3"));
                break;
            default:
                break;
        }

        // Update the RecommendrecyclerView
        RecommendrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecommendrecyclerView.setHasFixedSize(true);
        RecommendrecyclerView.setAdapter(new RecommendAdapter(items, getActivity()));
    }


}