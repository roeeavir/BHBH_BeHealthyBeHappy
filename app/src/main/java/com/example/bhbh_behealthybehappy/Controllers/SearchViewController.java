package com.example.bhbh_behealthybehappy.Controllers;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.ScreenUtils;

public class SearchViewController {// Search Activity Controller Class

    // Variables
    Context context;

    private SearchView search_SRC_searchView;

    private ImageButton search_IMB_back;
    private ImageView search_IMG_background;

    public SearchViewController(Context context) {
        this.context = context;

        findViews();
        initViews();
    }

    private void initViews() {
        search_IMB_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchActivity) context).finish();
            }
        });
//        search_SRC_searchView.
    }



    private void findViews() {
        search_SRC_searchView = ((SearchActivity) context).findViewById(R.id.search_SRC_searchView);
        search_IMB_back = ((SearchActivity) context).findViewById(R.id.search_IMB_back);
        search_IMG_background = ((SearchActivity) context).findViewById(R.id.search_IMG_background);
    }

    public void updateSearch_IMG_background(int id){
        Glide.with(context).load(id).into(search_IMG_background);
    }
}
