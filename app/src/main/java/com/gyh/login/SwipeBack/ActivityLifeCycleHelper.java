package com.gyh.login.SwipeBack;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Stack;

public class ActivityLifeCycleHelper implements Application.ActivityLifecycleCallbacks {
    private Stack<Activity> mActivities;

    public ActivityLifeCycleHelper() {
        mActivities = new Stack<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
    }

    public Activity getPreActivity() {
        int size = mActivities.size();
        if (size < 2) {
            return null;
        } else {
            return mActivities.get(size - 2);
        }
    }

    public void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }
}
