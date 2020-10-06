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
    private String searchKey = "software";
    private StoryInteractor storyInteractor;
    private LiveData<List<Story>> storyListLiveData;

    public StoryViewModel(StoryInteractor storyInteractor) {
        this.storyInteractor = storyInteractor;
    }
//подписываемся на Обсервер Rx на репозиторий
    public LiveData<List<Story>> getStoryListLiveData() {
        storyListLiveData = storyInteractor.getLiveDataFromRepository();
        return storyListLiveData;
    }

    public void subscribeToRepository() {
        storyInteractor.getLiveDataFromRepository().observeForever(new Observer<List<Story>>() {
            @Override
            public void onChanged(List<Story> stories) {

            }
        });
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        Log.d(TAG, "StoryViewModel search key is = " + "\"" +  searchKey + "\"");

    }

    public String getSearchKey() {
        return searchKey;
    }
}


