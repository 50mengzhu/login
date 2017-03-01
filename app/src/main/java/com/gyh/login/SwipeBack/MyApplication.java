package com.gyh.login.SwipeBack;

import android.app.Application;

import org.litepal.LitePal;

public class MyApplication extends Application {
    public ActivityLifeCycleHelper getHelper() {
        return mHelper;
    }

    private ActivityLifeCycleHelper mHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mHelper = new ActivityLifeCycleHelper();
        // store all the activities
        registerActivityLifecycleCallbacks(mHelper);
        LitePal.initialize(this);
    }
}
