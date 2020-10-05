package com.example.task8.data.repository.db;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.task8.data.model.Story;

import javax.inject.Inject;


@Database(entities = {Story.class}, version = 1)
public abstract class StoryDatabase extends RoomDatabase {

    private static StoryDatabase instance;
    public abstract StoryDao storyDao();
    public static synchronized StoryDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), StoryDatabase.class,
                    "story_database").build();
        }
        return instance;
    }
}
