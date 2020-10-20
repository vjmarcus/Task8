package com.example.task8.data.repository.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;

import java.util.List;
import java.util.Observable;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface StoryDao {

    @Insert
    Completable insert(StoryResponse storyResponse);

    @Query("SELECT * FROM story_response_table")
    Single<List<StoryResponse>> getResponseList();

    @Query("DELETE FROM story_response_table")
    Single<Integer> deleteAll();

    @Query("SELECT * FROM story_response_table WHERE id = (SELECT MAX(id) FROM story_response_table)")
    Single<StoryResponse> getSingleResponse();


//    @Insert
//    void insert(Story story);
//
//    @Query("SELECT * FROM story_table")
//    LiveData<List<Story>> getAllStories();
//
//    @Query("SELECT * FROM story_table")
//    List<Story> getAllStoriesList();
//
    @Query("DELETE FROM story_response_table")
    void deleteAllStories();
}
