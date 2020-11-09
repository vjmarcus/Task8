package com.example.task8.aui_presentation.viewmodel;

import com.example.task8.business_domain.StoryInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class StoryViewModelTest {

    @Mock
    StoryInteractor mockStoryInteractor;

    StoryViewModel storyViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        storyViewModel = new StoryViewModel(mockStoryInteractor);


    }

    @Test
    public void getDataFromInter() {
        storyViewModel.getDataFromInter("KEY");
    }
}