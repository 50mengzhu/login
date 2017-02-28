package com.gyh.login.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gyh.login.RoutePager;

public class RoutePagerAdapter extends FragmentPagerAdapter {
    public final int COUNT = 2;
    private String[] titles = new String[]{"路线", "组团"};
    private Context mContext;

    public RoutePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return RoutePager.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
