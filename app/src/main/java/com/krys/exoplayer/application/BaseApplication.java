package com.krys.exoplayer.application;

import android.app.Application;

import com.krys.exoplayer.utils.CommonUtils;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //CommonUtils.disableDarkMode();
    }
}
