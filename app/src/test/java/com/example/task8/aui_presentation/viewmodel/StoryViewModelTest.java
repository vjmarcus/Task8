package com.example.task8.aui_presentation.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.example.task8.business_domain.StoryInteractor;
import com.example.task8.data.model.Source;
import com.example.task8.data.model.Story;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoryViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    StoryInteractor mockStoryInteractor;
    @Mock
    Observer<List<Story>> mockObserver;
    @Mock
    LifecycleOwner mockLifecycleOwner;
    Lifecycle lifecycle;
    StoryViewModel storyViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(mockLifecycleOwner);
        storyViewModel = new StoryViewModel(mockStoryInteractor);
        storyViewModel.getViewModelLiveData().observeForever(mockObserver);
    }

    @Test
    public void testNull() {
        when(mockStoryInteractor.getDataFromRepo("KEY")).thenReturn(null);
        Assert.assertNotNull(storyViewModel.getViewModelLiveData());
        assertTrue(storyViewModel.getViewModelLiveData().hasObservers());
    }

    @Test
    public void testApi() {
        Observable<List<Story>> fakeObservable = getFakeObservable();
        when(mockStoryInteractor.getDataFromRepo("KEY")).thenReturn(fakeObservable);
        storyViewModel.getDataFromInter("KEY");
        Assert.assertNotNull(storyViewModel.getViewModelLiveData());
    }

    private Observable<List<Story>> getFakeObservable() {
        List<Story> storyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            storyList.add(new Story(new Source("sourceName"), "NaN", "NaN",
                    "NaN", "NaN", "NaN"));
        }
        return Observable.just(storyList);
    }
}