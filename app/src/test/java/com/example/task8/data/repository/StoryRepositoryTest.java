package com.example.task8.data.repository;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StoryRepositoryTest {


    StoryRepository storyRepository;
    @Mock
    Application mockApplication;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        storyRepository = new StoryRepository(mockApplication);

    }

    @Test
    public void getData() {

    }
}