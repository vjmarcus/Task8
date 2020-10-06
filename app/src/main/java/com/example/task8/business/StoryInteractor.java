package com.example.task8.business;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.Story;
import com.example.task8.data.repository.StoryRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class StoryInteractor {

    private StoryRepository storyRepository;
    private LiveData<List<Story>> storyListLiveData;

    private LiveData<List<Story>> testLiveData;

    public StoryInteractor(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
        storyListLiveData = new MutableLiveData<>();
    }


    //External method
    public LiveData<List<Story>> getStoryListLiveData() {
        storyListLiveData = storyRepository.getLiveDataFromWeb("software");
        return storyListLiveData;
    }

    public LiveData<List<Story>> getStoryListFromDb() {
        storyListLiveData = storyRepository.getLiveDataFromDb();
        return storyListLiveData;
    }

    public LiveData<List<Story>> getStoryListFromWeb() {
        storyRepository.getLiveDataFromWeb(getCurrentKey());
        return storyListLiveData;
    }

    public String getCurrentKey() {

        return "software";
    }
}
