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
}