package com.example.task8.business_domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.Story;
import com.example.task8.data.repository.StoryRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoryInteractorTest {

    private final String KEY = "key";
    private LiveData<List<Story>> fakeLiveDataFromDb;
    private LiveData<List<Story>> fakeLiveDataFromWeb;
    @Mock
    private StoryRepository storyRepository;
    private StoryInteractor storyInteractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        storyInteractor =  new StoryInteractor(storyRepository);
        fakeLiveDataFromDb = new MutableLiveData<>();
        fakeLiveDataFromWeb = new MutableLiveData<>();
    }
}