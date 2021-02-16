package com.example.bhbh_behealthybehappy.Activities;


import android.os.Bundle;
import android.util.Log;

import com.example.bhbh_behealthybehappy.Controllers.SearchViewController;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.DRINK;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.SPORTS_ACTIVITY;

public class SearchActivity extends BaseActivity {

    SearchViewController searchViewController;

    public static final String SEARCH_ITEM = "SEARCH_ITEM";
    public static final String USER_DATE = "USER_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        isDoubleBackPressToClose = true;
        Log.d("pttt", "Created SearchActivity");

        // Initializing SearchViewController
        searchViewController = new SearchViewController(this);

        setTheme();

        // Sending SearchViewController the wanted date as String
        searchViewController.setDate(getIntent().getStringExtra(USER_DATE));

    }

    // Sets the ui by the item theme
    private void setTheme() {
        String item = getIntent().getStringExtra(SEARCH_ITEM);
        Log.d("pttt", "Item theme is" + item);
        if (item.equals(DRINK)) {
            searchViewController.updateTheme(R.drawable.drink_background, Enums.ITEM_THEME.DRINK);
            searchViewController.setAddWaterButtonToVisible();
        } else if (item.equals(SPORTS_ACTIVITY))
            searchViewController.updateTheme(R.drawable.activity_back, Enums.ITEM_THEME.ACTIVITY);
        else
            searchViewController.updateTheme(R.drawable.food_background, Enums.ITEM_THEME.FOOD);
    }
}