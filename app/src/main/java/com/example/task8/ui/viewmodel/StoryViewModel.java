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
    private String searchKey = "software";
    private StoryInteractor storyInteractor;
    private LiveData<List<Story>> storyListLiveData;
    private LiveData<List<Story>> interactorLiveData;

    public StoryViewModel(StoryInteractor storyInteractor) {
        this.storyInteractor = storyInteractor;
    }
//подписываемся на Обсервер Rx на репозиторий
    public LiveData<List<Story>> getStoryListLiveData() {
        storyListLiveData = storyInteractor.getStoryListLiveData();
        return storyListLiveData;
    }

    public void setSearchParam(String key) {
        setSearchKey(key);
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSearchKey() {
        return searchKey;
    }
}


