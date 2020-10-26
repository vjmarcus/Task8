package com.example.task8.data.repository.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;

import java.util.List;
import java.util.Observable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface StoryDao {

    @Insert
    Completable insert(StoryResponse storyResponse);


    @Query("DELETE FROM story_response_table")
    Completable deleteAll();

    @Query("SELECT * FROM story_response_table WHERE id = (SELECT MAX(id) FROM story_response_table)")
    Single<StoryResponse> getLastAddedResponse();


    @Query("SELECT * FROM story_response_table")
    Flowable<List<StoryResponse>> getAll();
}
