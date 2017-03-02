package com.gyh.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyh.login.SwipeBack.SwipeBackActivity;
import com.gyh.login.banner.BannerViewPager;
import com.gyh.login.banner.OnPageClickListener;
import com.gyh.login.banner.ViewPagerAdapter;
import com.gyh.login.bottomsheet.BottomSheetBuilder;
import com.gyh.login.bottomsheet.BottomSheetItemClickListener;
import com.gyh.login.db.Ad;
import com.gyh.login.db.Route;
import com.gyh.login.db.User;
import com.gyh.login.lab.AdLab;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.gyh.login.Index.user;

public class RouteIndex extends SwipeBackActivity {

    private List<ImageView> mViews = new ArrayList<>();
    private Route route;
    private boolean isStar = false;

    private ViewPagerAdapter mAdapter;
    private BannerViewPager mBannerViewPager;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ScrollView mScrollView;
    private View mSep;

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.1f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleContainerVisible = true;
    int height = 0;

    // 底部状态栏
    public static final String STATE_GRID = "state_grid";
    private BottomSheetDialog mBottomSheetDialog;
    private BottomSheetBehavior mBehavior;
    private boolean mShowingGridDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_index);

        WindowManager wm = this.getWindowManager();
        height = wm.getDefaultDisplay().getHeight() - 80;

        createViews();

        // 读取路线信息
        route = getIntent().getParcelableExtra("route");
        isStar();

        TextView intro = (TextView) findViewById(R.id.route_intro);
        intro.setText(route.getIntro());
        TextView title = (TextView) findViewById(R.id.route_title);
        title.setText(route.getTitle());
        TextView subIntro = (TextView) findViewById(R.id.route_sub_title);
        subIntro.setText(route.getIntro());

        User founder = DataSupport.find(User.class, route.getFounder());
        TextView authorName = (TextView) findViewById(R.id.route_author_name);
        authorName.setText(founder.getName());
        TextView authorIntro = (TextView) findViewById(R.id.route_author_intro);
        authorIntro.setText(founder.getIntro());

        mBannerViewPager = (BannerViewPager) findViewById(R.id.banner);
        mAdapter = new ViewPagerAdapter(mViews, new OnPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
            }
        });
        mBannerViewPager.setAdapter(mAdapter);

        // AppBar的监听
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.article_main_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteIndex.this.getTouchHelper().startSlide();
                RouteIndex.this.getTouchHelper().startAnimating(true, 0);
            }
        });

        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                if (isStar) {
                    mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_star_white);
                } else {
                    mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_unstar_white);
                }
            }
        });

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mSep = findViewById(R.id.route_sep);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_menu, menu);

        return true;
    }

    // 记录修改后结果
    private String res = "";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        res = "";
        switch (item.getItemId()) {
            case R.id.route_share:
                // 分享逻辑
                onShowDialogGridClick();
                break;
            case R.id.route_like:
                // 收藏逻辑
                if (!mIsTheTitleContainerVisible) {
                    if (isStar) {
                        // 删除已收藏
                        mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_unstar_black);
                        isStar = false;
                        String routeIds = user.getStarRoutes();
                        String[] ids = routeIds.split(",");
                        for(String id : ids) {
                            // 如果ID不相等，加入id，忽略相等
                            if(!id.equals(String.valueOf(route.getId()))) {
                                res = res + "," + id;
                            }
                        }
                        Snackbar.make(mAppBarLayout, "已取消收藏", Snackbar.LENGTH_LONG).setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_star_black);
                                isStar = true;
                                // 不变
                                res = user.getStarRoutes();
                            }
                        }).show();
                        user.setStarRoutes(res);
                    } else {
                        // 收藏
                        mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_star_black);
                        isStar = true;
                        res = user.getStarRoutes() + "," + String.valueOf(route.getId());
                        Snackbar.make(mAppBarLayout, "已收藏", Snackbar.LENGTH_LONG).setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_unstar_black);
                                isStar = false;
                                res = user.getStarRoutes();
                            }
                        }).show();
                    }
                } else {
                    if (isStar) {
                        mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_unstar_white);
                        isStar = false;
                        String routeIds = user.getStarRoutes();
                        String[] ids = routeIds.split(",");
                        for(String id : ids) {
                            // 如果ID不相等，加入id，忽略相等
                            if(!id.equals(String.valueOf(route.getId()))) {
                                res = res + "," + id;
                            }
                        }
                        Snackbar.make(mAppBarLayout, "已取消收藏", Snackbar.LENGTH_LONG).setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_star_white);
                                isStar = true;
                                res = user.getStarRoutes();
                            }
                        }).show();
                        user.setStarRoutes(res);
                    } else {
                        mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_star_white);
                        isStar = true;
                        res = user.getStarRoutes() + "," + String.valueOf(route.getId());
                        Snackbar.make(mAppBarLayout, "已收藏", Snackbar.LENGTH_LONG).setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_unstar_white);
                                isStar = false;
                                res = user.getStarRoutes();
                            }
                        }).show();
                    }
                }
                break;
        }

        User tmp = new User();
        tmp.setStarRoutes(res);
        tmp.update(user.getId());
        return true;
    }

    private void createViews() {
        AdLab adLab = AdLab.get(RouteIndex.this);
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
        ImageView iv = (ImageView) LayoutInflater.from(RouteIndex.this).inflate(R.layout.ad_item, mBannerViewPager, false);
        Glide.with(RouteIndex.this).load(ads.get(index).getImageId()).into(iv);
        mViews.add(iv);
    }

    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mAppBarLayout, height, View.GONE);
                mToolbar.setNavigationIcon(R.drawable.ic_back_black);
                mToolbar.getMenu().findItem(R.id.route_share).setIcon(R.drawable.ic_share_black);

                if (isStar) {
                    mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_star_black);
                } else {
                    mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_unstar_black);
                }

                mScrollView.setVisibility(View.VISIBLE);
                mSep.setVisibility(View.VISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mAppBarLayout, height, View.VISIBLE);
                mToolbar.setNavigationIcon(R.drawable.ic_back_white);
                mToolbar.getMenu().findItem(R.id.route_share).setIcon(R.drawable.ic_share_white);

                if (isStar) {
                    mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_star_white);
                } else {
                    mToolbar.getMenu().findItem(R.id.route_like).setIcon(R.drawable.ic_unstar_white);
                }

                mScrollView.setVisibility(View.GONE);
                mSep.setVisibility(View.GONE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置运动的动画
    public static void startAlphaAnimation(View v, int height, int visibility) {
        TranslateAnimation translateAnimation = (visibility == View.GONE)
                ? new TranslateAnimation(0, 0, 0, -height)
                : new TranslateAnimation(0, 0, 0, 0);

        translateAnimation.setDuration(ALPHA_ANIMATIONS_DURATION);
        translateAnimation.setFillAfter(true);
        v.startAnimation(translateAnimation);
    }

    // 展示底部分享框
    public void onShowDialogGridClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingGridDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setMenu(this.getResources().getBoolean(R.bool.tablet_landscape)
                        ? R.menu.menu_bottom_grid_tablet_sheet : R.menu.menu_bottom_grid_sheet)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        mShowingGridDialog = false;
                    }
                })
                .createDialog();

        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingGridDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }

    private void isStar() {
        String routeIds = user.getStarRoutes();
        String[] ids = routeIds.split(",");
        for(String id : ids) {
            if(id.equals(String.valueOf(route.getId()))) {
                isStar = true;
                return;
            }
        }

        isStar = false;
    }


}
