package com.example.task8.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryList;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.ApiFactory;
import com.example.task8.data.repository.network.NewsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryRepository {
    private static final String TAG = "MyApp";
    private List<Story> storyList;
    private ApiFactory apiFactory;
    private StoryDao storyDao;
    private NewsApi newsApi;
    private MutableLiveData<List<Story>> allStoriesLiveData = new MutableLiveData<>();
    private Application application;

    public StoryRepository(Application application) {
        this.application = application;
        StoryDatabase db = StoryDatabase.getInstance(application);
        storyDao = db.storyDao();
        apiFactory = ApiFactory.getInstance();
        newsApi = ApiFactory.getNewsApi();
    }

    //Load data to LiveData from Web
    public LiveData<List<Story>> getLiveDataFromWeb(String key) {
        deleteAllStoriesInDb();
        Call<StoryList> call = newsApi.getPostsByDate(key, ApiFactory.getCurrentDate(),
                ApiFactory.getCurrentDate(), 20, "en", ApiFactory.API_KEY);
        call.enqueue(new Callback<StoryList>() {
            @Override
            public void onResponse(@NonNull Call<StoryList> call, @NonNull Response<StoryList> response) {
                Log.d(TAG, "onResponse: " + response);
                StoryList articlesList = response.body();
                if (articlesList != null) {
                    storyList = articlesList.getArticles();
                    Log.d(TAG, "Good onResponse: " + storyList.size());
                } else {
                    Log.d(TAG, "bad onResponse:");
                }
                allStoriesLiveData.setValue(storyList);
//                addStoriesToDatabase();
            }

            @Override
            public void onFailure(Call<StoryList> call, Throwable t) {
                Log.d(TAG, "onFailure: error= " + t.getMessage());
            }
        });
        return allStoriesLiveData;
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
