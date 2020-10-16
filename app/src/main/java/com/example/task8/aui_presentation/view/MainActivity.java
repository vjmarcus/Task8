package com.example.task8.aui_presentation.view;

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

import com.example.task8.App;
import com.example.task8.R;
import com.example.task8.business_domain.StoryInteractor;
import com.example.task8.data.model.Story;
import com.example.task8.data.repository.StoryRepository;
import com.example.task8.aui_presentation.adapter.StoryAdapter;
import com.example.task8.aui_presentation.interfaces.RecyclerViewClickListener;
import com.example.task8.aui_presentation.viewmodel.StoryViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MyApp";
    @Inject
    StoryViewModel storyViewModel;
    @Inject
    StoryInteractor storyInteractor;
    @Inject
    StoryRepository storyRepository;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewClickListener recyclerViewClickListener;
    private RecyclerView recyclerView;
    private String searchKey;
    private List<Story> storyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent().injectMainActivity(this);
        subscribeData();
        refreshData(searchKey);
        init();
        initRecyclerViewClickListener();
        initSwipeRefreshLayout();
            }

    private void refreshData(String searchKey) {
        Log.d(TAG, "Main refreshData:");
        storyInteractor.getDataFromRepo(searchKey);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "Main onItemSelected: " + adapterView.getSelectedItem().toString());
//        if (currentTopic != adapterView.getSelectedItem().toString()) {
//            viewModel.clearDb();
//        }
        searchKey = adapterView.getSelectedItem().toString();
        storyInteractor.getDataFromRepo(searchKey);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //DO NOTHING
    }

    public void showStories(List<Story> storyList) {
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

    private void subscribeData() {
        storyViewModel.getViewModelLiveData().observe(this, new Observer<List<Story>>() {
            @Override
            public void onChanged(List<Story> storyList) {
                Log.d(TAG, "View onChanged: = " + storyList.size());
                showStories(storyList);
            }
        });
//        storyViewModel.getStoryListLiveData(searchKey).observe(this, new Observer<List<Story>>() {
//            @Override
//            public void onChanged(List<Story> stories) {
//                Log.d(TAG, "MA onChanged: " + stories.size());
//                storyList = stories;
//                //Update recyclerView
//                showStories();
//            }
//        });
    }


    private void initRecyclerViewClickListener() {
        recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View sharedView, Story story, int position) {
                Log.d(TAG, "recyclerViewListClicked: ");
                goToSecondActivity(sharedView, story, position);
            }
        };
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(searchKey);
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