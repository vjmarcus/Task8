package com.example.task8.utils;

import androidx.room.TypeConverter;

import com.example.task8.data.model.StoryResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Constants {
    public static final String API_KEY = "92632cb5e9be43ef9dfc1545d021180a";
    public static final String BASE_URL = "https://newsapi.org/v2/";
    public static final String TEST_URL = "http://jsonplaceholder.typicode.com/";
    public static final String TAG = "MyApp";
    public static final String KEY = "software";

    public static String getCurrentDate() {
        long date = System.currentTimeMillis();
        String pattern = "YYYY-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(date));
    }
}
