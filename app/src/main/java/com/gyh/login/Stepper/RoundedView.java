package com.gyh.login.Stepper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.gyh.login.R;


public class RoundedView extends View {

    public RoundedView(Context context) {
        super(context);
    }

    public RoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // 拥有孩子的派生类应当重载这个方法来为每个孩子布局
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    Paint mPaint = new Paint(Paint.DITHER_FLAG);

    @Override
    protected void onDraw(Canvas canvas) {
        // ANTI_ALIAS_FLAG 抗锯齿使边缘更加平滑
        mPaint.setAntiAlias(true);
        // FILTER_BITMAP_FLAG 影响位图运动时的采样
        mPaint.setFilterBitmap(true);
        // DITHER_FLAG 抖动，颜色更精确
        mPaint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        mPaint.setColor(color);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);

        if (text != null && !checked) {
            drawText(canvas);
        }

        if (checked && text == null) {
            drawChecked(canvas);
        }
    }

    private int color = ContextCompat.getColor(getContext(), R.color.circle_color_default_gray);
    private String text = null;
    private boolean checked = false;

    public void setCircleColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setCircleAccentColor(){
        final TypedValue value = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        if(value != null) color = value.data;
        else ContextCompat.getColor(getContext(), R.color.circle_color_default_blue);
        invalidate();
    }

    public void setCircleGrayColor(){
        color = ContextCompat.getColor(getContext(), R.color.circle_color_default_gray);
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        this.checked = false;
        invalidate();
    }

    public void setChecked(boolean checked){
        this.checked = checked;
        text = null;
        invalidate();
    }

    private void drawText(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(getResources().getDimension(R.dimen.item_circle_text_size));

        // 四边为整数
        Rect areaRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        // 四边为浮点数
        RectF bounds = new RectF(areaRect);
        bounds.right = paint.measureText(text, 0, text.length());
        // 基准线以下的距离（正数） - 基准线以上的距离（负数）
        bounds.bottom = paint.descent() - paint.ascent();
        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;

        paint.setColor(Color.WHITE);
        canvas.drawText(text, bounds.left, bounds.top - paint.ascent(), paint);
    }

    private void drawChecked(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check);

        if (bitmap != null) {
            float posX = (canvas.getWidth() - bitmap.getWidth()) / 2;
            float posY = (canvas.getHeight() - bitmap.getHeight()) / 2;

            canvas.drawBitmap(bitmap, posX, posY, paint);
        }
    }
}
