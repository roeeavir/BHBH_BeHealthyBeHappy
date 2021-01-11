package com.example.bhbh_behealthybehappy.Activities;


import android.os.Bundle;

import com.example.bhbh_behealthybehappy.Controllers.SearchViewController;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.DRINK;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.SPORTS_ACTIVITY;

public class SearchActivity extends Activity_Base {

    SearchViewController searchViewController;

    public static final String SEARCH_ITEM = "SEARCH_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        isDoubleBackPressToClose = true;

        searchViewController = new SearchViewController(this);

        setTheme();
    }

    private void setTheme() {
        String item = getIntent().getStringExtra(SEARCH_ITEM);
        if (item.equals(DRINK)){
            searchViewController.updateTheme(R.drawable.drink_back, Enums.ITEM_THEME.DRINK);
            searchViewController.setAddWaterButtonToVisible();
        }
        else if (item.equals(SPORTS_ACTIVITY))
            searchViewController.updateTheme(R.drawable.activity_back, Enums.ITEM_THEME.ACTIVITY);
        else
            searchViewController.updateTheme(R.drawable.food_back, Enums.ITEM_THEME.FOOD);
    }
}