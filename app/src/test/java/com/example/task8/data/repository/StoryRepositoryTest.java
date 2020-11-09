package com.example.task8.data.repository;

import android.app.Application;
import android.content.Context;

import com.example.task8.data.model.Source;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.db.StoryDao;
import com.example.task8.data.repository.network.NewsApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StoryRepositoryTest {


    StoryRepository storyRepository;
    @Mock
    Application mockApplication;
    @Mock
    StoryDao storyDao;
    @Inject
    NewsApi newsApi;
    @Inject
    Context context;

    @Before
    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//        storyRepository = new StoryRepository(mockApplication);
        Mockito.when(newsApi.getPostsByDate("NULL", "NULL", "NULL",
                0, "NULL", "NULL")).thenReturn(getFakeResponse());

    }

    @Test
    public void getDataFromWeb() {
        newsApi.getPostsByDate("NULL", "NULL", "NULL",
                0, "NULL", "NULL");
        verify(newsApi).getPostsByDate("NULL", "NULL", "NULL",
                0, "NULL", "NULL");
    }

    @Test
    public void getDataFromDb() {

    }

    private Observable<StoryResponse> getFakeResponse () {
        List<Story> storyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            storyList.add(new Story(new Source("sourceName"), "NaN", "NaN",
                    "NaN", "NaN", "NaN"));
        }
        StoryResponse storyResponse = new StoryResponse(storyList);
        return Observable.just(storyResponse);
    }
}