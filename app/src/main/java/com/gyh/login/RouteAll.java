package com.gyh.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.gyh.login.SwipeBack.SwipeBackActivity;
import com.gyh.login.util.RoutePagerAdapter;

public class RouteAll extends SwipeBackActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_all);

        // 设置ViewPager的高度填充
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport (true);

        mViewPager = (ViewPager) findViewById(R.id.main_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mSearchView = (FloatingSearchView) findViewById(R.id.search_view);

        RoutePagerAdapter adapter = new RoutePagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(adapter);
        // 跳转到指定界面
        mViewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        mTabLayout.setupWithViewPager(mViewPager);

        //搜索逻辑
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

                //pass them on to the search view
                //mSearchView.swapSuggestions(newSuggestions);
            }
        });
        // 返回按钮
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                RouteAll.this.getTouchHelper().startSlide();
                RouteAll.this.getTouchHelper().startAnimating(true, 0);
            }
        });
    }
}
