package com.gyh.login;

import android.content.Intent;
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
import android.widget.Toast;

import com.gyh.login.SwipeBack.SwipeBackActivity;
import com.gyh.login.db.Route;
import com.gyh.login.db.User;
import com.gyh.login.util.RoutesAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.gyh.login.R.id.user_lv_name;

public class UserIndex extends SwipeBackActivity {

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleContainerVisible = true;

    private ImageView mIvBg;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextView mUsername;
    private TextView mUserintro;
    private FrameLayout mTitleContainer;
    private LinearLayout mLlTitleContainer;
    private Button mEdit;
    private MenuItem mSearchItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);

        mIvBg = (ImageView) findViewById(R.id.user_iv_bg);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.user_appbar);
        mToolbar = (Toolbar) findViewById(R.id.user_toolbar);
        mUsername = (TextView) findViewById(user_lv_name);
        mUserintro = (TextView) findViewById(R.id.user_lv_intro);
        mTitleContainer = (FrameLayout) findViewById(R.id.user_lv_title);
        mLlTitleContainer = (LinearLayout) findViewById(R.id.user_ll_title);
        mEdit = (Button) findViewById(R.id.user_edit);

        // 如果是他人的主页不可以修改，默认是自己的主页
        int flag = getIntent().getIntExtra("flag", 0);
        if (flag == 1) {
            mEdit.setVisibility(View.GONE);
        }

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

        List<Route> starRoutes = new ArrayList<>();
        boolean isEmpty = true;
        String rawId = user.getStarRoutes();
        String[] ids = rawId.split(",");
        for(String id : ids) {
            if (!id.equals("")) {
                isEmpty = false;
                starRoutes.add(DataSupport.find(Route.class, Integer.parseInt(id)));
            }
        }

        if (!isEmpty) {
            TextView noRoute = (TextView) findViewById(R.id.no_star_route);
            noRoute.setVisibility(View.GONE);
            final RoutesAdapter adapter = new RoutesAdapter(starRoutes);
            adapter.setFlag(1);
            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.star_route_items);

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setNestedScrollingEnabled(false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(UserIndex.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter.setItemWidth((int) ((recyclerView.getWidth()) / 1.9));
                    recyclerView.setAdapter(adapter);
                }
            });
        }


        final String name = user.getName();
        final String intro = user.getIntro();

        TextView lv_name = (TextView) findViewById(R.id.user_lv_name);
        TextView lv_intro = (TextView) findViewById(R.id.user_lv_intro);
        lv_name.setText(name);
        lv_intro.setText(intro);

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserIndex.this, UserSet.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
            }
        });
    }


    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mEdit, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mEdit, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
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

    // 处理修改后的返回消息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    Toast.makeText(UserIndex.this, "Welcome back", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
