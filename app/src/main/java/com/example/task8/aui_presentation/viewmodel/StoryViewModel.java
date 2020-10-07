package com.example.task8.aui_presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.task8.business_domain.StoryInteractor;
import com.example.task8.data.model.Story;

import java.util.List;

public class StoryViewModel extends ViewModel {

    private static final String TAG = "MyApp";
    private StoryInteractor storyInteractor;
    private LiveData<List<Story>> storyListLiveData;

    public StoryViewModel(StoryInteractor storyInteractor) {
        this.storyInteractor = storyInteractor;
    }

    public LiveData<List<Story>> getStoryListLiveData(String searchKey) {
        storyListLiveData = storyInteractor.getLiveDataFromRepository(searchKey);
        return storyListLiveData;
    }
}


