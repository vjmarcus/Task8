package com.example.task8.data.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Observable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.ApiFactory;
import com.example.task8.data.repository.network.NewsApi;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class StoryRepository {
    private static final String TAG = "MyApp";
    private List<Story> storyList;
    private ApiFactory apiFactory;
    private StoryDao storyDao;
    private NewsApi newsApi;
    private MutableLiveData<List<Story>> allStoriesLiveData = new MutableLiveData<>();
    private Application application;
    private Observable<List<Story>> storyListObservable;

    public StoryRepository(Application application) {
        this.application = application;
        StoryDatabase db = StoryDatabase.getInstance(application);
        storyDao = db.storyDao();
        apiFactory = ApiFactory.getInstance();
        newsApi = ApiFactory.getNewsApi();
    }

    //Load data to LiveData from Web
    @SuppressLint("CheckResult")
    public LiveData<List<Story>> getLiveDataFromWeb(String key) {
        newsApi.getPostsByDate(key, ApiFactory.getCurrentDate(),
                ApiFactory.getCurrentDate(), 20, "en", ApiFactory.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoryResponse>() {
                    @Override
                    public void accept(StoryResponse storyResponse) throws Exception {
                        allStoriesLiveData.setValue(storyResponse.getArticles());
                        //Observable.just = storyResponse.getArticles()
                        // Observable.just(Callable) = BlockingGet  = ответ из Observable минуя подписчик
                        // вернет чистый респонс, ретурн Observable.just.storyResponse.getArticles()
                    }
                });
        return Observable ;// хранит данные
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
