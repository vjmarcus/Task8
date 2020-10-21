package com.example.task8.data.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.task8.App;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.NewsApi;
import com.example.task8.utils.Constants;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryRepository {
    private static final String TAG = "MyApp";
    private StoryDao storyDao;
    private long updatedTime;
    private String updatedTopic;
    private Observable<StoryResponse> observable;
    @Inject
    NewsApi newsApi;
    @Inject
    Context context;
    private StoryResponse storyResponse;

    public StoryRepository(Application application) {
        App.getAppComponent().injectStoryRepository(this);
        StoryDatabase db = StoryDatabase.getInstance(application);
        storyDao = db.storyDao();
        Log.d(TAG, "Context " + context.getPackageName());
    }

    public StoryResponse getData(String key) {
        if (loadFromDbOrLoadFromWEb(key)) {
            Log.d(TAG, "Repo getData: from db");
            // return from db
            return null;
        } else {
            Log.d(TAG, "Repo getData: from web");
                StoryResponse storyResponse = null;
            Log.d(TAG, "getData: response" + newsApi.getCallStoryResponse(key, Constants.getCurrentDate(),
                        Constants.getCurrentDate(), 20, "en", Constants.API_KEY).request());

            return storyResponse;
        }
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
