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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class StoryInteractor {

    private StoryRepository storyRepository;


    public StoryInteractor(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Observable<List<Story>> getDataFromRepo(String searchKey) {
        storyRepository.initDb();
        return storyRepository.getData(searchKey)
                .map(new Function<StoryResponse, List<Story>>() {
                    @Override
                    public List<Story> apply(@NonNull StoryResponse storyResponse) throws Exception {
                        return storyResponse.getArticles();
                    }
                });
    }
}
