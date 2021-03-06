package com.gyh.login.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import com.gyh.login.IndexPager;

public class IndexPagerAdapter extends FragmentPagerAdapter{
    public final int COUNT = 3;
    private String[] titles = new String[]{"路线", "广场", "其他"};
    private Context mContext;

    public IndexPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return IndexPager.newInstance(position + 1);
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
