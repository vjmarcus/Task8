package com.example.task8.ui.view;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.task8.R;
import com.example.task8.business.StoryInteractor;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.User;
import com.example.task8.data.repository.StoryRepository;
import com.example.task8.ui.adapter.StoryAdapter;
import com.example.task8.ui.interfaces.RecyclerViewClickListener;
import com.example.task8.ui.viewmodel.StoryViewModel;
import com.example.task8.ui.viewmodel.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private static final String TAG = "MyApp";
    private UserViewModel userViewModel;
    private StoryViewModel storyViewModel;
    private StoryInteractor storyInteractor;
    private StoryRepository storyRepository;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewClickListener recyclerViewClickListener;
    private RecyclerView recyclerView;
    private String currentTopic;
    private List<Story> storyList;

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
        storyRepository = new StoryRepository(getApplication());
        storyInteractor = new StoryInteractor(storyRepository);
        storyInteractor.getStoryListFromWeb();
        storyViewModel = new StoryViewModel(storyInteractor);
        storyViewModel.getStoryListLiveData().observe(this, new Observer<List<Story>>() {
            @Override
            public void onChanged(List<Story> stories) {
                Log.d(TAG, "onChanged: " + stories.get(0).getAuthor());
                if (stories != null)
                    Log.d(TAG, "onChanged: " + stories.size());
                storyList = stories;
                //Update recyclerView
                showStories();
            }
        });

        init();
        initRecyclerViewClickListener();
        initSwipeRefreshLayout();
        currentTopic = "android";
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Log.d(TAG, "Main onItemSelected: " + adapterView.getSelectedItem().toString());
//        if (currentTopic != adapterView.getSelectedItem().toString()) {
//            viewModel.clearDb();
//        }
        currentTopic = adapterView.getSelectedItem().toString();
//        viewModel.setCurrentRequestParam(currentTopic);
//        viewModel.update(currentTopic);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //DO NOTHING
    }

    public void showStories() {
        Log.d(TAG, "showStories: create adapter " + storyList.size());
        StoryAdapter storyAdapter = new StoryAdapter(storyList, recyclerViewClickListener);
        recyclerView.setAdapter(storyAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void init() {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<?> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.topics,
                android.R.layout.simple_list_item_1);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        recyclerView = findViewById(R.id.story_recycler);
    }


    private void initRecyclerViewClickListener() {
//        recyclerViewClickListener = new RecyclerViewClickListener() {
//            @Override
//            public void recyclerViewListClicked(View sharedView, Story story, int position) {
//                Log.d(TAG, "recyclerViewListClicked: ");
//                goToSecondActivity(sharedView, story, position);
//            }
//        };
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                viewModel.update(currentTopic);
                Log.d(TAG, "onRefresh: swipe");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void goToSecondActivity(View sharedView, Story story, int position) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("obj", story);
        ActivityOptionsCompat compat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(MainActivity.this, sharedView, "transition");
        startActivity(intent, compat.toBundle());
    }
}