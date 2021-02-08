package com.krys.exoplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.krys.exoplayer.R;
import com.krys.exoplayer.base.BaseActivity;

public class Splash extends BaseActivity {

    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById();
        init();
    }

    private void findViewById() {
        animationView = findViewById(R.id.animationView);
    }

    private void init() {
        startApplication();
    }

    private void startApplication() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}