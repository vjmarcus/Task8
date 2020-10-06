package com.example.task8.aui_presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task8.R;
import com.example.task8.data.model.Story;
import com.squareup.picasso.Picasso;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";
    private TextView titleSecondTextView;
    private TextView sourceNameTextView;
    private TextView descriptionTextView;
    private ImageView picassoImageView;
    private Story story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        init();
        loadFromIntent();
        setTextToTextView();
        loadImageToImageViews();
    }

    private void init() {
        titleSecondTextView = findViewById(R.id.titleTextView);
        sourceNameTextView = findViewById(R.id.sourceNameSecondTextView);
        descriptionTextView = findViewById(R.id.descriptionSecondTextView);
        picassoImageView = findViewById(R.id.picassoImageView);
    }

    private void loadFromIntent() {
        Intent intent = getIntent();
        story = (Story) intent.getSerializableExtra("obj");
        if (story != null) {
            Log.d(TAG, "loadFromIntent: " + story.getAuthor());
        }
    }

    private void loadImageToImageViews() {
        Picasso.get().load(story.getUrlToImage())
                .into(picassoImageView);
    }

    private void setTextToTextView() {
        titleSecondTextView.setText(story.getTitle());
        sourceNameTextView.setText(story.getSource().getName());
        descriptionTextView.setText(story.getDescription());
    }
}