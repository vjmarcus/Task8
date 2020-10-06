package com.example.task8.di.modules;

import android.app.Application;

import com.example.task8.business_domain.StoryInteractor;
import com.example.task8.data.repository.StoryRepository;
import com.example.task8.aui_presentation.viewmodel.StoryViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StoryViewModelModule {

    Application application;

    public StoryViewModelModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    StoryViewModel provideStoryViewModel(StoryInteractor storyInteractor) {
        return new StoryViewModel(storyInteractor);
    }

    @Provides
    @Singleton
    StoryInteractor provideStoryInteractor(StoryRepository storyRepository) {
        return new StoryInteractor(storyRepository);
    }

    @Provides
    @Singleton
    StoryRepository provideStoryRepository(Application application) {
        return new StoryRepository(application);
    }

    @Provides
    @Singleton
    Application provideApplication(){
        return application;
    }
}
