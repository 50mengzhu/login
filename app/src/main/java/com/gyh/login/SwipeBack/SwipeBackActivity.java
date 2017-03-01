package com.gyh.login.SwipeBack;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class SwipeBackActivity extends AppCompatActivity {
    private TouchHelper mTouchHelper;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTouchHelper == null) {
            mTouchHelper = new TouchHelper(getWindow());
        }

        boolean consume = mTouchHelper.processTouchEvent(ev);
        if (!consume) {
            return super.dispatchTouchEvent(ev);
        }
        return false;
    }

    public TouchHelper getTouchHelper() {
        return mTouchHelper;
    }
}
