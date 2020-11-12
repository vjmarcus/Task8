package com.example.task8.data.repository;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.task8.data.model.Source;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.NewsApi;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.junit.Assert;
import org.junit.Assert.*;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.task8.data.model.Source;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.db.StoryDatabase;
import com.example.task8.data.repository.network.NewsApi;
import com.example.task8.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class StoryRepositoryInstrumTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    StoryDatabase db;
    StoryDao storyDao;
    StoryResponse fakeStoryResponse;

    //Db test
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, StoryDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();
        storyDao = db.storyDao();
        fakeStoryResponse = getFakeStoryResponse(111);
        Log.d(Constants.TAG, "StoryRepositoryInstrumTest createDb: " + fakeStoryResponse.getId());
    }

    @Test
    public void insertAndGetAllStory() {
        storyDao.insert(fakeStoryResponse).blockingAwait();
        storyDao.getResponseById(111)
                .test()
                .assertValue(new Predicate<StoryResponse>() {
                    @Override
                    public boolean test(@NonNull StoryResponse storyResponse) throws Exception {
                        return fakeStoryResponse.getId() == storyResponse.getId();
                    }
                });

    }


    private StoryResponse getFakeStoryResponse(int id) {
        List<Story> storyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            storyList.add(new Story(new Source("sourceName"), "NaN", "NaN",
                    "NaN", "NaN", "NaN"));
        }
        return new StoryResponse(id, storyList);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}