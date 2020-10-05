package com.example.task8.data.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.task8.App;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.ApiFactory;
import com.example.task8.data.repository.network.NewsApi;
import com.example.task8.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class StoryRepository {
    private static final String TAG = "MyApp";
    private List<Story> storyList;
    private ApiFactory apiFactory;
    private StoryDao storyDao;
    private MutableLiveData<List<Story>> allStoriesLiveData = new MutableLiveData<>();
    private Application application;
    @Inject
    NewsApi newsApi;

    public StoryRepository(Application application) {
        this.application = application;
        StoryDatabase db = StoryDatabase.getInstance(application);
        storyDao = db.storyDao();
        App.getAppComponent().injectStoryRepository(this);
    }

    //Load data to LiveData from Web
    @SuppressLint("CheckResult")
    public void getLiveDataFromWeb(String key) {
        Observable<StoryResponse> observable = newsApi.getPostsByDate(Constants.KEY, Constants.getCurrentDate(),
                Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
        observable
                .subscribeOn(Schedulers.io())
                .flatMapIterable(new Function<StoryResponse, Iterable<Story>>() {
                    @Override
                    public Iterable<Story> apply(StoryResponse storyResponse) throws Exception {
                        return storyResponse.getArticles();
                    }
                })
                .filter(new Predicate<Story>() {
                    @Override
                    public boolean test(Story story) throws Exception {
                        return story.getTitle().length() > 20;
                    }
                })
                .map(new Function<Story, Story>() {
                    @Override
                    public Story apply(Story story) throws Exception {
                        story.setTitle(story.getTitle() + " filtered");
                        return story;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Story>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //DO NOTHING
                    }

                    @Override
                    public void onNext(Story story) {
                        Log.d(TAG, "onNext: " + story.getTitle() + " " + story.getTitle().length());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //DO NOTHING
                    }

                    @Override
                    public void onComplete() {
                        //DO NOTHING
                    }
                });
    }

    //Load data to LiveData from Db
    public LiveData<List<Story>> getLiveDataFromDb() {
        return allStoriesLiveData;
    }

//    private void addStoriesToDatabase() {
//        Log.d(TAG, "addStoriesToDatabase: ");
//        for (int i = 0; i < storyList.size(); i++) {
//            insert(storyList.get(i));
//        }
//    }

    public void deleteAllStoriesInDb() {
        new DeleteAllStoriesAsyncTask(storyDao).execute();
    }

//    private void insert(Story story) {
//        new InsertStoryAsyncTask(storyDao).execute(story);
//    }

//    private class InsertStoryAsyncTask extends AsyncTask<Story, Void, Void> {
//        private StoryDao storyDao;
//
//        public InsertStoryAsyncTask(StoryDao storyDao) {
//            this.storyDao = storyDao;
//        }
//
//        @Override
//        protected Void doInBackground(Story... stories) {
//            storyDao.insert(stories[0]);
//            return null;
//        }
//    }

    private class DeleteAllStoriesAsyncTask extends AsyncTask<Void, Void, Void> {

        private StoryDao storyDao;

        public DeleteAllStoriesAsyncTask(StoryDao storyDao) {
            this.storyDao = storyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            storyDao.deleteAllStories();
            return null;
        }
    }

}
