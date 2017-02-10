package com.gyh.login.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import com.gyh.login.PageFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    public final int COUNT = 3;
    private String[] titles = new String[]{"Tab1", "Tab2", "Tab3"};
    private Context mContext;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
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
