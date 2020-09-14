package com.example.task8.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task8.R;
import com.example.task8.data.model.Story;
import com.example.task8.ui.interfaces.RecyclerViewClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private static final String TAG = "MyApp";
    private List<Story> storyList;
    private RecyclerViewClickListener recyclerViewClickListener;

    public StoryAdapter(List<Story> storyList, RecyclerViewClickListener recyclerViewClickListener) {
        this.storyList = storyList;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item,
                parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storyList.get(position);
        holder.titleTextView.setText(story.getTitle());
        holder.authorTextView.setText(story.getAuthor());
        holder.publishedAtTextView.setText(story.getPublishedAt());
        ViewCompat.setTransitionName(holder.urlImageView, "transition");
        holder.titleTextView.setTransitionName("transition");
        if (story.getUrlToImage() != null) {
            if (story.getUrlToImage().isEmpty()) {
                holder.urlImageView.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Picasso.get().load(story.getUrlToImage())
                        .resize(100, 100)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.urlImageView);
            }
        } else {
            holder.urlImageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView authorTextView;
        private final TextView titleTextView;
        private final TextView publishedAtTextView;
        private final ImageView urlImageView;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            publishedAtTextView = itemView.findViewById(R.id.publishedAtTextView);
            urlImageView = itemView.findViewById(R.id.urlImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.recyclerViewListClicked(urlImageView,
                    storyList.get(getAdapterPosition()), this.getLayoutPosition());
            Log.d(TAG, "onClick: ");
        }
    }
}
