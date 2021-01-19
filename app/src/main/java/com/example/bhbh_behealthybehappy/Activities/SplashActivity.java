package com.example.bhbh_behealthybehappy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bhbh_behealthybehappy.Controllers.SplashViewController;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.google.android.material.button.MaterialButton;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {


    private SplashViewController splashViewController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isDoubleBackPressToClose = true;
        Log.d("pttt", "Created SplashActivity");

        splashViewController = new SplashViewController(this);

        splashViewController.updateSplash_IMG_background(R.drawable.img_healthy_girl_s);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("pttt", "Stopping carousal timer");
        splashViewController.stopCounting();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("pttt", "Stopping carousal timer");
        splashViewController.stopCounting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("pttt", "Stopping carousal timer");
        splashViewController.startLogoAnimation();
    }

}