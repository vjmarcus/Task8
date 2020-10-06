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

    public void update(String currentTopic) {
        if (loadFromDbOrLoadFromWEb(currentTopic)) {
            Log.d(Constants.TAG, "viewModel getAllStoryData: load from db");
            storyRepository.getLiveDataFromDb();
        } else {
            Log.d(Constants.TAG, "viewModel getAllStoryData: load from WEB");
            storyRepository.getLiveDataFromWeb();
        }
    }

    //Load from repository
    public LiveData<List<Story>> getLiveDataFromRepository() {
        liveDataFromRepository = storyRepository.getLiveDataFromWeb();
        return liveDataFromRepository;
    }

    public LiveData<List<Story>> getStoryListFromDb() {
        liveDataFromRepository = storyRepository.getLiveDataFromDb();
        return liveDataFromRepository;
    }

    public LiveData<List<Story>> getStoryListFromWeb() {
        storyRepository.getLiveDataFromWeb();
        return liveDataFromRepository;
    }

    public String getCurrentKey() {
        return "software";
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
