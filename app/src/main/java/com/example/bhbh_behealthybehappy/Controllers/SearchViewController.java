package com.example.bhbh_behealthybehappy.Controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.example.bhbh_behealthybehappy.Activities.CustomeItemActivity;
import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;

public class SearchViewController {// Search Activity Controller Class


    // Variables
    Context context;

    private SearchView search_SRC_searchView;

    private ImageButton search_IMB_back;
    private ImageView search_IMG_background;
    private Button search_BTN_customItem;

    private Enums.ITEM_THEME theme;

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
        search_BTN_customItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomActivity();
//                ((SearchActivity) context).finish();
            }
        });
    }

    private void openCustomActivity() {
        search_BTN_customItem.setEnabled(false);
        Intent myIntent = new Intent(context, CustomeItemActivity.class);
        myIntent.putExtra(CustomeItemActivity.ADD_CUSTOM_ITEM, theme);
        context.startActivity(myIntent);// Opens winner activity
        search_BTN_customItem.setEnabled(true);
    }


    private void findViews() {
        search_SRC_searchView = ((SearchActivity) context).findViewById(R.id.search_SRC_searchView);
        search_IMB_back = ((SearchActivity) context).findViewById(R.id.search_IMB_back);
        search_IMG_background = ((SearchActivity) context).findViewById(R.id.search_IMG_background);
        search_BTN_customItem = ((SearchActivity) context).findViewById(R.id.search_BTN_customItem);
    }

    public void updateTheme(int id, Enums.ITEM_THEME th) {
        theme = th;
        updateSearch_IMG_background(id);
        updateSearch_BTN_customItem();
        updateHintSearch_SRC_searchView();
    }

    private void updateSearch_IMG_background(int id) {
        Glide.with(context).load(id).into(search_IMG_background);
    }

    private void updateSearch_BTN_customItem() {
        String s = search_BTN_customItem.getText().toString() + " " + theme.toString();
        search_BTN_customItem.setText(s);
    }

    private void updateHintSearch_SRC_searchView() {
        String s = search_SRC_searchView.getQueryHint() + " " + theme.toString();
        search_SRC_searchView.setQueryHint(s);
    }
}
