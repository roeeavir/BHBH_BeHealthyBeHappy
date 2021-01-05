package com.example.bhbh_behealthybehappy.Activities;


import android.app.Application;

import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.example.bhbh_behealthybehappy.Utils.MySP;

// An activity that runs when the app is active and provides itself to MyHelper class
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MyHelper.init(this);
        MySP.init(this);

    }
}
