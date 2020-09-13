package com.example.task8.data.repository.network;

import com.example.task8.data.model.StoryList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("everything")
    Call<StoryList> getPostsByDate(@Query("q") String key,
                                   @Query("from") String fromDate,
                                   @Query("to") String toDate,
                                   @Query("pageSize") int pageSize,
                                   @Query("language") String language,
                                   @Query("apiKey") String apiKey);
}
