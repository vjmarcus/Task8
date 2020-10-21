package com.example.task8.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.task8.utils.Constants;
import com.example.task8.utils.StoryResponseConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "story_response_table")
public class StoryResponse implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("articles")
    @Expose
    @TypeConverters({StoryResponseConverter.class})
    private List<Story> articles;

    @Ignore
    public StoryResponse(List<Story> articles) {
        this.articles = articles;
    }

    public StoryResponse(int id, List<Story> articles) {
        this.id = id;
        this.articles = articles;
    }

    public List<Story> getArticles() {
        return articles;
    }

    public int getId() {
        return id;
    }

}
