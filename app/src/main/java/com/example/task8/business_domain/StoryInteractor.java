package com.example.task8.business_domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.Story;
import com.example.task8.data.repository.StoryRepository;
import com.example.task8.utils.Constants;

import java.util.List;

public class StoryInteractor {

    private StoryRepository storyRepository;
    private LiveData<List<Story>> liveDataFromRepository;
    private long updatedTime;
    private String updatedTopic;


    public StoryInteractor(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
        liveDataFromRepository = new MutableLiveData<>();
    }

    public void update(String searchKey) {
        if (loadFromDbOrLoadFromWEb(searchKey)) {
            Log.d(Constants.TAG, "viewModel getAllStoryData: load from db");
            storyRepository.getLiveDataFromDb();
        } else {
            Log.d(Constants.TAG, "viewModel getAllStoryData: load from WEB");
            storyRepository.getLiveDataFromWeb(searchKey);
        }
    }

    //Load from repository
    public LiveData<List<Story>> getLiveDataFromRepository(String searchKey) {
        liveDataFromRepository = storyRepository.getLiveDataFromWeb(searchKey);
        return liveDataFromRepository;
    }

    public LiveData<List<Story>> getStoryListFromDb() {
        liveDataFromRepository = storyRepository.getLiveDataFromDb();
        return liveDataFromRepository;
    }

    private Boolean loadFromDbOrLoadFromWEb(String currentTopic) {
        long currentTime = System.currentTimeMillis();
        if (((currentTime - updatedTime) < 60000) && currentTopic.equals(updatedTopic)) {
            updatedTime = System.currentTimeMillis();
            return true;
        } else {
            updatedTime = System.currentTimeMillis();
            updatedTopic = currentTopic;
            return false;
        }
    }
}
