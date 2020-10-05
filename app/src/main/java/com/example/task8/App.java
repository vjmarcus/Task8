package com.example.task8;


import android.app.Application;
import android.util.Log;

import com.example.task8.di.components.AppComponent;
import com.example.task8.di.components.DaggerAppComponent;
import com.example.task8.di.modules.AppModule;
import com.example.task8.di.modules.NewsApiModule;
import com.example.task8.di.modules.StoryViewModelModule;
import com.example.task8.utils.Constants;

public class App extends Application {

    static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "onCreate: App");
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .newsApiModule(new NewsApiModule()).build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
