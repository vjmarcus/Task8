package com.example.task8.business_domain;
import com.example.task8.data.model.Source;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.data.repository.StoryRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.junit.MockitoTestListener;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class StoryInteractorTest {

    StoryInteractor storyInteractor;
    @Mock
    StoryRepository mockStoryRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        storyInteractor = new StoryInteractor(mockStoryRepository);
        when(mockStoryRepository.getData("KEY")).thenReturn(getFakeResponse());
    }

    @Test
    public void getDataFromRepo() {
        storyInteractor.getDataFromRepo("KEY");
        verify(mockStoryRepository).getData("KEY");
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
    
    @After
    public void tearDown() throws Exception {
    }
}