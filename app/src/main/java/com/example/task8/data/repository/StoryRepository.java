package com.example.task8.data.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.task8.App;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.NewsApi;
import com.example.task8.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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

    public StoryRepository(Application application) {
        App.getAppComponent().injectStoryRepository(this);
        StoryDatabase db = StoryDatabase.getInstance(application);
        storyDao = db.storyDao();
        Log.d(TAG, "Context " + context.getPackageName());
    }

    public Observable<StoryResponse> getData(String key) {
        if (loadFromDbOrLoadFromWEb(key)) {
            Log.d(TAG, "Repo getData: from db");
            loadStoryResponseFromDb();
            return observable;

        } else {
            Log.d(TAG, "Repo getData: from web");
            Call<StoryResponse> call = newsApi.getListResponse(key, Constants.getCurrentDate(),
                    Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
            call.enqueue(new Callback<StoryResponse>() {
                @Override
                public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
                    if (response.isSuccessful()){
                        Log.d(TAG, "onResponse: getArticles().size() = " + response.body().getArticles().size());
                      insertToDb(response.body());
                        List<Story> storyList = response.body().getArticles();
                        Log.d(TAG, "onResponse: storyList =  " + storyList.size());
                    }
                }

                @Override
                public void onFailure(Call<StoryResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure call: " + t.getMessage());

                }
            });
            observable = newsApi.getPostsByDate(key, Constants.getCurrentDate(),
                    Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
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
        new DeleteAllStoriesAsyncTask(storyDao).execute();
    }

    private void insertToDb(StoryResponse storyResponse) {
        new InsertStoryAsyncTask(storyDao).execute(storyResponse);
    }

    private void loadStoryResponseFromDb() {
        new LoadStoryResponseAsyncTask(storyDao);
    }

    private class InsertStoryAsyncTask extends AsyncTask<StoryResponse, Void, Void> {
        private StoryDao storyDao;

        public InsertStoryAsyncTask(StoryDao storyDao) {
            this.storyDao = storyDao;
        }

        @Override
        protected Void doInBackground(StoryResponse... storyResponses) {
            storyDao.insert(storyResponses[0]);
            return null;
        }
    }

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

    private class LoadStoryResponseAsyncTask extends AsyncTask<Void, Void, List<StoryResponse>> {

        private StoryDao storyDao;

        public LoadStoryResponseAsyncTask(StoryDao storyDao) {
            this.storyDao = storyDao;
        }

        @Override
        protected List<StoryResponse> doInBackground(Void... voids) {
            List<StoryResponse> responseList = storyDao.getAll();
            return null;
        }

        @Override
        protected void onPostExecute(List<StoryResponse> storyResponses) {
            super.onPostExecute(storyResponses);
            Log.d(TAG, "doInBackground: LOAD RESPONSE " + storyResponses.size());

        }
    }

}
