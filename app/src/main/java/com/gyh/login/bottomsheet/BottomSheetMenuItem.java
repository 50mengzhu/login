package com.gyh.login.bottomsheet;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

class BottomSheetMenuItem implements BottomSheetItem {

    @ColorInt
    private int mTextColor;

    @ColorInt
    private int mTintColor;

    private Drawable mIcon;
    private String mTitle;
    private int mId;
    private MenuItem mMenuItem;

    @DrawableRes
    private int mBackground;

    public BottomSheetMenuItem(MenuItem item,@ColorInt int textColor, @DrawableRes int background,
                               @ColorInt int tintColor) {
        mMenuItem = item;
        mIcon = item.getIcon();
        mId = item.getItemId();
        mTitle = item.getTitle().toString();
        mTextColor = textColor;
        mBackground = background;
        mTintColor = tintColor;

        if (mTintColor != -1) {
            mIcon = DrawableCompat.wrap(mIcon);
            DrawableCompat.setTint(mIcon, mTintColor);
        }
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public MenuItem getMenuItem() {
        return mMenuItem;
    }

    @DrawableRes
    public int getBackground() {
        return mBackground;
    }

    public int getId() {
        return mId;
    }

    @ColorInt
    public int getTextColor() {
        return mTextColor;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
