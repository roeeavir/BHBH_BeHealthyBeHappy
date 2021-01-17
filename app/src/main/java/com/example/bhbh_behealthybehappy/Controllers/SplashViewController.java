package com.example.bhbh_behealthybehappy.Controllers;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bhbh_behealthybehappy.Activities.LoginActivity;
import com.example.bhbh_behealthybehappy.Activities.SplashActivity;
import com.example.bhbh_behealthybehappy.R;
import com.google.android.material.button.MaterialButton;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SplashViewController {

    // Variables
    private final int ANIMATION_DURATION = 2000;
    private final int DELAY = 800;

    private Context context;

    private ImageView splash_IMG_logo;
    private ImageView splash_IMG_background;
    private MaterialButton splash_BTN_start;
    private TextView splash_LBL_name;

    private Timer carousalTimer;


    public SplashViewController(Context context) {
        this.context = context;

        findViews();
        initViews();

    }

    private void findViews() {
        splash_IMG_logo = ((SplashActivity) context).findViewById(R.id.splash_IMG_logo);
        splash_BTN_start = ((SplashActivity) context).findViewById(R.id.splash_BTN_start);
        splash_LBL_name = ((SplashActivity) context).findViewById(R.id.splash_LBL_name);
        splash_IMG_background = ((SplashActivity) context).findViewById(R.id.splash_IMG_background);
    }

    private void initViews() {
        splash_BTN_start.setVisibility(View.INVISIBLE);

        splash_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, LoginActivity.class);
                context.startActivity(myIntent);
            }
        });
    }

    public void startLogoAnimation() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((SplashActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        splash_IMG_logo.setY(-height / 2);


        splash_IMG_logo.animate()
                .rotation(360)
                .translationY(0)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        splash_BTN_start.setVisibility(View.VISIBLE);
                        splash_IMG_background.setAlpha((float) 0.7);
                        startCounting();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
    }

    public void changeColor() {// Changes the color of the exit button and the winner label
        Random r = new Random();
        int color = Color.argb(255, r.nextInt(256),
                r.nextInt(256), r.nextInt(256));
        splash_LBL_name.setTextColor(color);
    }

    public void startCounting() {
        carousalTimer = new Timer();
        Log.d("pttt", "Starting carousal timer");
        carousalTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((SplashActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeColor();
                    }
                });
            }
        }, 0, DELAY);
    }

    public void stopCounting(){
        carousalTimer.cancel();
    }

    public void updateSplash_IMG_background(int id){
        Glide.with(context).load(id).into(splash_IMG_background);
    }
}
