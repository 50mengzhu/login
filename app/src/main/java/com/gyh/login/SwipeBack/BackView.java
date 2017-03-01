package com.gyh.login.SwipeBack;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

class BackView extends View {
    private View mView;

    public BackView(Context context) {
        super(context);
    }

    public void cacheView(View view) {
        mView = view;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mView != null) {
            mView.draw(canvas);
            mView = null;
        }
    }
}
