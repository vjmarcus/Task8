package com.example.task8.ui.interfaces;


import com.example.task8.data.model.Story;

import java.util.List;

public interface LoadStoryCallback {
    void onCompleteCallback(List<Story> storyList);
}
