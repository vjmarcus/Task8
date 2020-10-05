package com.example.task8.di.components;

import com.example.task8.data.repository.StoryRepository;
import com.example.task8.di.modules.NewsApiModule;
import com.example.task8.ui.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NewsApiModule.class})
@Singleton
public interface AppComponent {
    void injectStoryRepository(StoryRepository storyRepository);
}

