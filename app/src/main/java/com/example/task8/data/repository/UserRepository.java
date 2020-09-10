package com.example.task8.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.task8.data.model.User;

public class UserRepository {

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private User user;

    public UserRepository() {
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        user = new User("Bob");
        userMutableLiveData.setValue(user);
        return userMutableLiveData;
    }
}
