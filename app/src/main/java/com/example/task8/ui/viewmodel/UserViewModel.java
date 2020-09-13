package com.example.task8.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task8.data.model.User;
import com.example.task8.data.repository.UserRepository;

public class UserViewModel extends ViewModel {

    public MutableLiveData<User> mutableLiveData;
    private UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
        mutableLiveData = userRepository.getUserMutableLiveData();
    }
}
