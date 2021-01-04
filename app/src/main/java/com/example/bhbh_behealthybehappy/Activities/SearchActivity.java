package com.example.bhbh_behealthybehappy.Activities;


import android.os.Bundle;

import com.example.bhbh_behealthybehappy.Controllers.SearchViewController;
import com.example.bhbh_behealthybehappy.R;

import static com.example.bhbh_behealthybehappy.Utils.Constants.DRINK;
import static com.example.bhbh_behealthybehappy.Utils.Constants.FOOD;
import static com.example.bhbh_behealthybehappy.Utils.Constants.SPORTS_ACTIVITY;

public class SearchActivity extends Activity_Base {

    SearchViewController searchViewController;

    public static final String SEARCH_ITEM = "SEARCH_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        isDoubleBackPressToClose = true;

        searchViewController = new SearchViewController(this);

        setBackground();
    }

    private void setBackground() {
        String item = getIntent().getStringExtra(SEARCH_ITEM);
        if (item.equals(DRINK))
            searchViewController.updateSearch_IMG_background(R.drawable.drink_back);
        else if (item.equals(SPORTS_ACTIVITY))
            searchViewController.updateSearch_IMG_background(R.drawable.activity_back);
        else
            searchViewController.updateSearch_IMG_background(R.drawable.food_back);
    }
}