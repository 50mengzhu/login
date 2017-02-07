package com.gyh.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gyh.login.db.Ad;
import com.gyh.login.util.AdLab;
import com.gyh.login.util.OnPageClickListener;
import com.gyh.login.util.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Index extends AppCompatActivity {

    private List<ImageView> mViews = new ArrayList<>();
    private ViewPagerAdapter mAdapter;
    private BannerViewPager mBannerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        createViews();

        mBannerViewPager = (BannerViewPager) findViewById(R.id.banner);
        mAdapter = new ViewPagerAdapter(mViews, new OnPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {

            }
        });
        mBannerViewPager.setAdapter(mAdapter);
    }

    private void createViews() {
        AdLab adLab = AdLab.get(Index.this);
        List<Ad> ads = adLab.getAds();

        // 添加最后一个到第一位
        addView(ads, ads.size() - 1);

        for (int i = 0; i < ads.size(); i++) {
            addView(ads, i);
        }

        // 添加第一个到最后一位
        addView(ads, 0);
    }

    private void addView(List<Ad> ads, int index) {
        ImageView iv = (ImageView) LayoutInflater.from(Index.this).inflate(R.layout.ad_item, mBannerViewPager, false);
        iv.setImageResource(ads.get(index).getImageId());
        mViews.add(iv);
    }
}
