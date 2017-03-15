package com.gyh.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyh.login.SwipeBack.SwipeBackActivity;
import com.gyh.login.db.Route;
import com.gyh.login.db.User;
import com.gyh.login.util.RoutesAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.gyh.login.Index.allRoutes;
import static com.gyh.login.R.id.user_lv_name;

public class FounderIndex extends SwipeBackActivity {

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleContainerVisible = true;

    private ImageView mIvBg;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private FrameLayout mTitleContainer;
    private LinearLayout mLlTitleContainer;
    private Button mEdit;
    private MenuItem mSearchItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founder_index);

        mIvBg = (ImageView) findViewById(R.id.user_iv_bg);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.user_appbar);
        mToolbar = (Toolbar) findViewById(R.id.user_toolbar);
        mTitleContainer = (FrameLayout) findViewById(R.id.user_lv_title);
        mLlTitleContainer = (LinearLayout) findViewById(R.id.user_ll_title);
        mEdit = (Button) findViewById(R.id.user_edit);

        mToolbar.setTitle("");

        // 设置返回按钮
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // AppBar的监听
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
            }
        });

        int index = getIntent().getIntExtra("user", 0);
        final User user = DataSupport.find(User.class, index);

        List<Route> makeRoutes = new ArrayList<>();
        boolean isEmpty = true;
        String rawId = user.getMakeRoutes();
        String[] ids = rawId.split(",");
        for(String id : ids) {
            if (!id.equals("")) {
                isEmpty = false;
                for (Route route : allRoutes) {
                    if(route.getId() == Integer.parseInt(id)) {
                        makeRoutes.add(route);
                        break;
                    }
                }
            }
        }

        if (!isEmpty) {
            final RoutesAdapter adapter = new RoutesAdapter(makeRoutes);
            adapter.setFlag(2);
            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.star_route_items);

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setNestedScrollingEnabled(false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(FounderIndex.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter.setItemWidth((int) ((recyclerView.getWidth()) / 1.9));
                    recyclerView.setAdapter(adapter);
                }
            });
        }


        final String name = user.getName();
        final String intro = user.getIntro();

        TextView lv_name = (TextView) findViewById(user_lv_name);
        TextView lv_intro = (TextView) findViewById(R.id.user_lv_intro);
        lv_name.setText(name);
        lv_intro.setText(intro);
    }


    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
