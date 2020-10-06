package com.example.task8.aui_presentation.interfaces;


import android.view.View;

import com.example.task8.data.model.Story;


public interface RecyclerViewClickListener {
    void recyclerViewListClicked(View sharedView, Story story, int position);
}
