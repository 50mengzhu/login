package com.gyh.login.SwipeBack;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class TouchHelper {

    private boolean isIdler = true;
    private boolean isSliding = false;
    private boolean isAnimating = false;

    private Window mWindow;
    private ViewGroup preContentView;
    private ViewGroup curContentView;
    private ViewGroup preView;
    private ViewGroup curView;
    private Activity preActivity;

    private int triggerWidth = 60;
    private int SHADOW_WIDTH = 30;

    private ShadowView mShadowView;

    public TouchHelper(Window window) {
        mWindow = window;
    }

    private Context getContext() {
        return mWindow.getContext();
    }

    public boolean processTouchEvent(MotionEvent event) {
        if (isAnimating) {
            return true;
        }

        float x = event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x <= triggerWidth) {
                    isIdler = false;
                    isSliding = true;
                    startSlide();
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (isSliding) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isSliding) {
                    if (event.getActionIndex() != 0) {
                        return true;
                    }
                    sliding(x);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!isSliding) {
                    return false;
                }

                int width = getContext().getResources().getDisplayMetrics().widthPixels;
                isAnimating = true;
                isSliding = false;
                startAnimating(width / x <= 3, x);
                return true;
            default:
                break;
        }
        return false;
    }

    public void startSlide() {
        preActivity = ((MyApplication) getContext().getApplicationContext()).getHelper().getPreActivity();
        if (preActivity == null) {
            return;
        }

        preContentView = (ViewGroup) preActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        preView = (ViewGroup) preContentView.getChildAt(0);
        preContentView.removeView(preView);
        curContentView=(ViewGroup) mWindow.findViewById(Window.ID_ANDROID_CONTENT);
        curView= (ViewGroup) curContentView.getChildAt(0);
        preView.setX(-preView.getWidth() / 3);
        curContentView.addView(preView, 0);

        if (mShadowView == null) {
            mShadowView = new ShadowView(getContext());
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(SHADOW_WIDTH, FrameLayout.LayoutParams.MATCH_PARENT);
        curContentView.addView(mShadowView, 1, params);
        mShadowView.setX(-SHADOW_WIDTH);
    }

    private void sliding(float rawX) {
        if (preActivity == null) {
            return;
        }

        curView.setX(rawX);
        preView.setX(-preView.getWidth() / 3 + rawX / 3);
        mShadowView.setX(-SHADOW_WIDTH+rawX);
    }

    public void startAnimating(final boolean isFinishing, float x) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ValueAnimator animator = ValueAnimator.ofFloat(x, isFinishing ? width : 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sliding((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                doEndWorks(isFinishing);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void doEndWorks(boolean isFinishing) {
        if (preActivity == null) {
            return;
        }

        if (isFinishing) {
            BackView view = new BackView(getContext());
            view.cacheView(preView);
            curContentView.addView(view, 0);
        } else {
            preView.setX(0);
        }

        curContentView.removeView(mShadowView);
        if (curContentView == null || preContentView == null) {
            return;
        }

        curContentView.removeView(preView);
        preContentView.addView(preView);
        if (isFinishing) {
            ((Activity) getContext()).finish();
            ((Activity) getContext()).overridePendingTransition(0, 0);
        }

        isAnimating=false;
        isSliding=false;
        isIdler=true;
        preView=null;
        curView=null;
    }

    class ShadowView extends View {

        private Drawable mDrawable;

        public ShadowView(Context context) {
            super(context);
            int[] colors=new int[]{0x00000000, 0x17000000, 0x43000000};
            mDrawable=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mDrawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
            mDrawable.draw(canvas);
        }
    }
}
