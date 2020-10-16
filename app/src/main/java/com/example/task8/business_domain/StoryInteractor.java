package com.example.task8.business_domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.StoryRepository;
import com.example.task8.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class StoryInteractor {

    private StoryRepository storyRepository;
    private MutableLiveData<List<Story>> interLiveData = new MutableLiveData<>();
    private List<Story> storyList = new ArrayList<>();


    public StoryInteractor(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public LiveData<List<Story>> getInterLiveData() {
        return interLiveData;
    }

    public void getDataFromRepo(String searchKey) {
        storyRepository.getData(searchKey)
                .subscribeOn(Schedulers.io())
                .flatMapIterable((Function<StoryResponse, Iterable<Story>>) StoryResponse::getArticles)
                .filter(story -> story.getTitle().length() > 20)
                .map(story -> {
                    story.setTitle(story.getTitle() + " filtered");
                    return story;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Story>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //DO NOTHING
                    }

                    @Override
                    public void onNext(Story story) {
                        Log.d(Constants.TAG, "onNext: " + story.getTitle() + " " + story.getTitle().length());
                        storyList.add(story);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //DO NOTHING
                    }

                    @Override
                    public void onComplete() {
                        interLiveData.setValue(storyList);
                    }
                });
    }


//    public void update(String searchKey) {
//        if (loadFromDbOrLoadFromWEb(searchKey)) {
//            Log.d(Constants.TAG, "viewModel getAllStoryData: load from db");
//            storyRepository.getLiveDataFromDb();
//        } else {
//            Log.d(Constants.TAG, "viewModel getAllStoryData: load from WEB");
//            storyRepository.getLiveDataFromWeb(searchKey);
//        }
//    }
//
//    //Load from repository
//    public LiveData<List<Story>> getLiveDataFromRepository(String searchKey) {
//        liveDataFromRepository = storyRepository.getLiveDataFromWeb(searchKey);
//        return liveDataFromRepository;
//    }
//
//    private Boolean loadFromDbOrLoadFromWEb(String currentTopic) {
//        long currentTime = System.currentTimeMillis();
//        if (((currentTime - updatedTime) < 60000) && currentTopic.equals(updatedTopic)) {
//            updatedTime = System.currentTimeMillis();
//            return true;
//        } else {
//            updatedTime = System.currentTimeMillis();
//            updatedTopic = currentTopic;
//            return false;
//        }
//    }
}
