package com.example.bhbh_behealthybehappy.Activities;

import android.os.Bundle;
import android.util.Log;

import com.example.bhbh_behealthybehappy.Controllers.CustomItemViewController;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class CustomeItemActivity extends BaseActivity {

    public static final String ADD_CUSTOM_ITEM = "ADD_CUSTOM_ITEM";

    private CustomItemViewController customItemViewController;

    private Enums.ITEM_THEME theme;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_item);
        isDoubleBackPressToClose = true;
        Log.d("pttt", "Created CustomItemActivity");

        // Initializing customItemViewController
        customItemViewController = new CustomItemViewController(this);

        // Getting items theme from previous intent
        theme = (Enums.ITEM_THEME) getIntent().getSerializableExtra(ADD_CUSTOM_ITEM);

        // Passing items theme to customItemViewController
        customItemViewController.updateTheme(theme);
    }
}