package com.gyh.login.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ViewPagerIndicator extends LinearLayout {

    private Context mContext;
    private Paint mPaint;
    private View mMoveView;

    private int mCurrentPosition = 0;
    private float mPositionOffset;

    private int mPadding = 10;
    private int mRadius = 10;
    private int mMoveRadius = 15;
    private int mDistanceBtwItem = mRadius * 2 + mPadding;

    private int mItemCount = 5;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);

        mMoveView = new MoveView(mContext);
        addView(mMoveView);
    }

    public void setItemCount(int count) {
        this.mItemCount = count;
        requestLayout();
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        this.mMoveRadius = radius * 3 / 2;
        this.mDistanceBtwItem = mRadius * 2 + mPadding;
        requestLayout();
    }

    public void setPadding(int padding) {
        this.mPadding = padding;
        this.mDistanceBtwItem = mRadius * 2 + mPadding;
        requestLayout();
    }

    public void setPositionAndOffset(int position, float offset) {
        this.mCurrentPosition = position;
        this.mPositionOffset = offset;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mPadding + (mRadius * 2 + mPadding) * (mItemCount - 2),
                2*mRadius + 2*mPadding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mItemCount - 2; i++) {
            canvas.drawCircle(mRadius + mPadding + mRadius * i * 2 + mPadding * i,
                    mRadius + mPadding, mRadius, mPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mMoveView.layout(
                (int) (mPadding + mDistanceBtwItem * (mCurrentPosition + mPositionOffset)),
                mPadding,
                (int) (mDistanceBtwItem * ( 1 + mCurrentPosition + mPositionOffset)),
                mPadding + mRadius * 2);
    }

    private class MoveView extends View {
        private Paint mPaint;

        public MoveView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.WHITE);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mMoveRadius * 2, mMoveRadius * 2);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(mRadius, mRadius, mMoveRadius, mPaint);
        }
    }
}
