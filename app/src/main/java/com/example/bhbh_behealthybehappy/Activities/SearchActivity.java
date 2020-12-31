package com.example.bhbh_behealthybehappy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bhbh_behealthybehappy.Controllers.SearchViewController;
import com.example.bhbh_behealthybehappy.R;

public class SearchActivity extends Activity_Base {

    SearchViewController searchViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        isDoubleBackPressToClose = true;

        searchViewController = new SearchViewController();
    }
}