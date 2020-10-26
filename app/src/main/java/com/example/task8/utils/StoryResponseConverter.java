package com.example.task8.utils;

import androidx.room.TypeConverter;

import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StoryResponseConverter {

    @TypeConverter
    public String fromStoryResponse(List<Story> storyList) {
        Gson gson = new Gson();
        return gson.toJson(storyList);
    }

    @TypeConverter
    public List<Story> toStoryResponse(String jsonString) {
        Gson gson = new Gson();
        TypeToken<List<Story>> token = new TypeToken<List<Story>>(){};
        return gson.fromJson(jsonString, token.getType());
    }
}
