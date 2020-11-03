package com.example.task8.data.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.task8.App;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.NewsApi;
import com.example.task8.utils.Constants;
import com.example.task8.utils.StoryResponseConverter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


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

    public StoryRepository(Application application) {
        App.getAppComponent().injectStoryRepository(this);
        StoryDatabase db = StoryDatabase.getInstance(application);
        storyDao = db.storyDao();
        Log.d(TAG, "Context " + context.getPackageName());
    }

    public Observable<StoryResponse> getData(String key) {
        if (loadFromDbOrLoadFromWEb(key)) {
            Log.d(TAG, "Repo getData: from db");
            observable = getAll()
                    .map(new Function<List<StoryResponse>, StoryResponse>() {
                        @Override
                        public StoryResponse apply(@NonNull List<StoryResponse> storyResponses) throws Exception {
                            return storyResponses.get(storyResponses.size() - 1);
                        }
                    }).toObservable();
            return observable;

        } else {
            Log.d(TAG, "Repo getData: from web");
            deleteAllFromDb();
            Observable<StoryResponse> observable = newsApi.getPostsByDate(key, Constants.getCurrentDate(),
                    Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
            getStoryResponseFromObservable(observable);
            return observable;
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

    private void getStoryResponseFromObservable(Observable<StoryResponse> observable) {
        Log.d(TAG, "StoryRepository getStoryResponseFromObservable: ");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StoryResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "StoryRepository onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull StoryResponse storyResponse) {
                        insertDataToDb(storyResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //DO NOTHING

                    }

                    @Override
                    public void onComplete() {
                        //DO NOTHING
                    }
                });

    }

    private void insertDataToDb(StoryResponse storyResponse) {
        Log.d(TAG, "StoryRepository insertDataToDb: ");
        storyDao.insert(storyResponse).subscribeOn(Schedulers.io()).subscribe();
    }

    private void deleteAllFromDb() {
        Log.d(Constants.TAG, "deleteAll: ");
        storyDao.deleteAll().subscribeOn(Schedulers.io()).subscribe();
    }

    private Single<StoryResponse> getLastStoryResponseFromDb() {
        Log.d(Constants.TAG, "getLastStoryResponse: ");
        return storyDao.getLastAddedResponse();
    }

    private Flowable<List<StoryResponse>> getAll() {
        return storyDao.getAll();
    }
}
