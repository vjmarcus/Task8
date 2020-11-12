package com.example.task8.data.repository;

import android.app.Application;
import android.content.Context;

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

@RunWith(JUnit4.class)
public class StoryRepositoryTest {

    StoryRepository storyRepository;
    StoryDatabase db;
    StoryDao storyDao;

    @Mock
    Application mockApplication;
    @Mock
    NewsApi mockNewsApi;
    @Mock
    Context mockContext;

    //Web test
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        storyRepository = new StoryRepository(mockApplication, mockNewsApi, mockContext);
        Mockito.when(mockNewsApi.getPostsByDate("NULL", "NULL", "NULL",
                0, "NULL", "NULL")).thenReturn(getFakeResponse());
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        StoryResponse fakeStoryResponse = getFakeStoryResponse();
        storyDao.insert(fakeStoryResponse);
        Assert.assertThat(fakeStoryResponse, equalTo(getStoryResponseFromFlowable()));
    }

    @Test
    public void getDataFromWeb() {
        mockNewsApi.getPostsByDate("NULL", "NULL", "NULL",
                0, "NULL", "NULL");
        verify(mockNewsApi).getPostsByDate("NULL", "NULL", "NULL",
                0, "NULL", "NULL");
    }

    private Observable<StoryResponse> getFakeResponse() {
        return Observable.just(getFakeStoryResponse());
    }

    private StoryResponse getStoryResponseFromFlowable() {
        List<StoryResponse> responseList = storyDao.getAll().blockingFirst();
        return responseList.get(0);
    }

    private StoryResponse getFakeStoryResponse() {
        List<Story> storyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            storyList.add(new Story(new Source("sourceName"), "NaN", "NaN",
                    "NaN", "NaN", "NaN"));
        }
        return new StoryResponse(storyList);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}