package com.example.task8.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.example.task8.R;
import com.example.task8.data.model.User;
import com.example.task8.presentation.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userViewModel = new UserViewModel();
        userViewModel.mutableLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "user name is: " + user.getName());
            }
        });
    }
}