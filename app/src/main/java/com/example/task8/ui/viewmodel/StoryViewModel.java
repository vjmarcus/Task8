package com.example.task8.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.task8.business.StoryInteractor;
import com.example.task8.data.model.Story;
import com.example.task8.data.repository.StoryRepository;

import java.util.List;

public class StoryViewModel extends ViewModel {

    private static final String TAG = "MyApp";
    private StoryInteractor storyInteractor;
    private LiveData<List<Story>> storyListLiveData;

    public StoryViewModel(StoryInteractor storyInteractor) {
        this.storyInteractor = storyInteractor;
    }

    public LiveData<List<Story>> getStoryListLiveData() {
        storyListLiveData = storyInteractor.getStoryListLiveData();
        return storyListLiveData;
    }
}


