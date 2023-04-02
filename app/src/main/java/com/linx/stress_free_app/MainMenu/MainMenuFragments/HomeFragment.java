package com.linx.stress_free_app.MainMenu.MainMenuFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linx.stress_free_app.NewsSystem.NewsAdapter;
import com.linx.stress_free_app.NewsSystem.NewsArticle;
import com.linx.stress_free_app.NewsSystem.NewsResponse;
import com.linx.stress_free_app.R;
import com.linx.stress_free_app.RestAPI.NewsAPI;
import com.linx.stress_free_app.RestAPI.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private static final String API_KEY = "4d64cb5b80c74e84860efa95a88e68b3";
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

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
                    recyclerView.setAdapter(new NewsAdapter(articles));
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

}