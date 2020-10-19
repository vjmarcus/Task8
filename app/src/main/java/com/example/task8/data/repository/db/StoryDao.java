package com.example.task8.data.repository.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;

import java.util.List;

@Dao
public interface StoryDao {

    @Insert
    void insert(StoryResponse storyResponse);

    @Query("SELECT * FROM story_response_table")
    List<StoryResponse> getAll();

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
