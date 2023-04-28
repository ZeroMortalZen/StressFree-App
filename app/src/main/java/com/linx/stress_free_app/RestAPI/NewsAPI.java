package com.linx.stress_free_app.RestAPI;

import com.linx.stress_free_app.NewsSystem.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {
    @GET("v2/everything")
    Call<NewsResponse> getNews(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );
}
