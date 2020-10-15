package com.example.task8.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.Source;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.network.NewsApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.*;

public class StoryRepositoryTest {

    @Mock
    NewsApi newsApi;
    @Mock
    Application application;
    private static final String KEY = "key";
    private StoryRepository storyRepository;
    private MutableLiveData<List<Story>> liveData = new MutableLiveData<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        storyRepository = new StoryRepository(application);
    }

    @Test
    public void getLiveDataFromWeb() {
        storyRepository.getLiveDataFromWeb(KEY)
    }

    @Test
    public void getLiveDataFromDb() {
    }

    @Test
    public void deleteAllStoriesInDb() {
    }

        private List<Story> getFakeStoryList() {
        List<Story> storyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            storyList.add(new Story(new Source("sourceName"), "author", "title",
                    "desc", "urlToImage", "publishedAt"));
        }
        return storyList;

    }

    public Observable<List<Story>> getFakeObservable() {
        return Observable.just(getFakeStoryList());
    }

    public LiveData<List<Story>> getLiveData() {
        return liveData;
    }
}