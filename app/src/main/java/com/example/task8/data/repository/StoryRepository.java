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

    public Observable<StoryResponse> getData(String key) {
        if (loadFromDbOrLoadFromWEb(key)) {
            Log.d(TAG, "Repo getData: from db");
            // return from db
            return loadSingleResponseFromDb();
        } else {
            Log.d(TAG, "Repo getData: from web");
            deleteAllStoriesInDb();

            return newsApi.getPostsByDate(key, Constants.getCurrentDate(),
                    Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
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
//    //Load data to LiveData from Web
//    public LiveData<List<Story>> getLiveDataFromWeb(String searchKey) {
//        deleteAllStoriesInDb();
//        List<Story> storyList = new ArrayList<>();
//        Observable<StoryResponse> observable = newsApi.getPostsByDate(searchKey, Constants.getCurrentDate(),
//                Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
//        observable
//                .subscribeOn(Schedulers.io())
//                .flatMapIterable(new Function<StoryResponse, Iterable<Story>>() {
//                    @Override
//                    public Iterable<Story> apply(StoryResponse storyResponse) throws Exception {
//                        return storyResponse.getArticles();
//                    }
//                })
//                .filter(new Predicate<Story>() {
//                    @Override
//                    public boolean test(Story story) throws Exception {
//                        return story.getTitle().length() > 20;
//                    }
//                })
//                .map(new Function<Story, Story>() {
//                    @Override
//                    public Story apply(Story story) throws Exception {
//                        story.setTitle(story.getTitle() + " filtered");
//                        return story;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Story>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        //DO NOTHING
//                    }
//
//                    @Override
//                    public void onNext(Story story) {
////                        Log.d(TAG, "onNext: " + story.getTitle() + " " + story.getTitle().length());
//                        storyList.add(story);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //DO NOTHING
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        //DO NOTHING
//                        Log.d(TAG, "onComplete:");
//                        liveDataFromWeb.setValue(storyList);
//                        addStoriesToDatabase(storyList);
//                        liveDataFromDb.setValue(storyList);
//
//                    }
//                });
//        return liveDataFromWeb;
//    }
//
//    //Load data to LiveData from Db
//    public LiveData<List<Story>> getLiveDataFromDb() {
//        Log.d(TAG, "getLiveDataFromDb: after loadAllStories, storyList = " + liveDataFromDb.getValue().size());
//        return liveDataFromDb;
//    }
//
//    private void addStoriesToDatabase(List<Story> storyList) {
//        Log.d(TAG, "addStoriesToDatabase: lines is = " + storyList.size());
//        for (int i = 0; i < storyList.size(); i++) {
//            insert(storyList.get(i));
//        }
//    }


    public void deleteAllStoriesInDb() {
        storyDao.deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: db cleared");
                    }
                });

    }

    private void insertToDb(StoryResponse storyResponse) {
        storyDao.insert(storyResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //DO NOTHING
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: response added to db" );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: added to db" + e.getMessage());
                    }
                });
    }

    private Observable<StoryResponse> loadSingleResponseFromDb() {
        observable = storyDao.getSingleResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable();
        return observable;
    }
}
