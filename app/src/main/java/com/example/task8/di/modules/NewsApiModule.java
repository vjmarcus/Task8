package com.example.task8.di.modules;

import android.util.Log;

import com.example.task8.data.repository.network.NewsApi;
import com.example.task8.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NewsApiModule {

    @Provides
    @Singleton
    Retrofit provideRetrofitInstance() {
        Log.d(Constants.TAG, "provideRetrofitInstance: created");
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    NewsApi provideNewsApi(Retrofit retrofit) {
        Log.d(Constants.TAG, "provideNewsApi: created");
        return retrofit.create(NewsApi.class);
    }
}
