package com.example.bhbh_behealthybehappy.Controllers;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bhbh_behealthybehappy.Activities.MainActivity;
import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.CallBack;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainViewController { // Main Activity Controller Class

    // Variables
    private Context context;


    public MainViewController(Context context) {
        this.context = context;


        BottomNavigationView navView = ((MainActivity) context).findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(((MainActivity) context),
                R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(((MainActivity) context), navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        findViews();
        initViews();

    }


    private void initViews() {

    }


    private void findViews() {// Initializes views

    }


    private CallBack callBack = new CallBack() {

        @Override
        public void updateInfo(String name, int age, int weight, int height) {
//            ((MainActivity) context).getSupportFragmentManager().getFragment();
        }
    };

}


