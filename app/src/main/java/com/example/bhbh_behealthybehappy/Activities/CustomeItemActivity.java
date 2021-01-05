package com.example.bhbh_behealthybehappy.Activities;

import android.os.Bundle;

import com.example.bhbh_behealthybehappy.Controllers.CustomItemViewController;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class CustomeItemActivity extends Activity_Base {

    public static final String ADD_CUSTOM_ITEM = "ADD_CUSTOM_ITEM";

    private CustomItemViewController customItemViewController;

    private Enums.ITEM_THEME theme;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_item);
        isDoubleBackPressToClose = true;

        customItemViewController = new CustomItemViewController(this);

        theme = (Enums.ITEM_THEME) getIntent().getSerializableExtra(ADD_CUSTOM_ITEM);

        customItemViewController.updateTheme(theme);
    }
}