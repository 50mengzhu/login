package com.gyh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.bumptech.glide.Glide;
import com.gyh.login.banner.BannerViewPager;
import com.gyh.login.banner.OnPageClickListener;
import com.gyh.login.banner.ViewPagerAdapter;
import com.gyh.login.db.Ad;
import com.gyh.login.db.Article;
import com.gyh.login.db.Route;
import com.gyh.login.db.User;
import com.gyh.login.lab.AdLab;
import com.gyh.login.searchHelper.DataHelper;
import com.gyh.login.searchHelper.RouteSuggestion;
import com.gyh.login.util.IndexPagerAdapter;
import com.gyh.login.util.MD5Util;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Index extends AppCompatActivity {

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    public static User user;
    private List<Route> routes = DataSupport.findAll(Route.class);
    private List<Article> articles = DataSupport.findAll(Article.class);

    private List<ImageView> mViews = new ArrayList<>();

    private ViewPagerAdapter mAdapter;
    private BannerViewPager mBannerViewPager;
    private DrawerLayout mDrawerLayout;
    private FloatingSearchView mSearchView;

    private String mLastQuery = "";

    @Override
    protected void onResume() {
        super.onResume();

        List<User> users = DataSupport.findAll(User.class);
        String starRoutes = users.get(user.getId() - 1).getStarRoutes();

        // 去除前缀 ,
        boolean key = false;
        String ID = "";
        for (int i = 0; i < starRoutes.length(); i++) {
            if(starRoutes.charAt(i) >= '0' && starRoutes.charAt(i) <= '9') {
                key = true;
            }

            if (key) {
                ID += starRoutes.charAt(i);
            }
        }

        // 更新用户
        User tmp = new User();
        tmp.setStarRoutes(ID);
        tmp.update(user.getId());
        user = DataSupport.find(User.class, user.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        createViews();
        user = getIntent().getParcelableExtra("user");

        // 初始化路线或路线
        if (routes.size() == 0 || articles.size() == 0) {
            initRoutes();
            routes = DataSupport.findAll(Route.class);
            articles = DataSupport.findAll(Article.class);
        }

        // 设置ViewPager的高度填充
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport (true);

        mBannerViewPager = (BannerViewPager) findViewById(R.id.banner);
        mAdapter = new ViewPagerAdapter(mViews, new OnPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
            }
        });
        mBannerViewPager.setAdapter(mAdapter);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSearchView= (FloatingSearchView) findViewById(R.id.search_view);

        // 搜索推荐项显示
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();

                    DataHelper.findSuggestions(Index.this, newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {
                                @Override
                                public void onResults(List<RouteSuggestion> results) {
                                    mSearchView.swapSuggestions(results);
                                    mSearchView.hideProgress();
                                }
                            });
                }
            }
        });

        // 搜素监听事件
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            // 推荐项点击事件
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                final RouteSuggestion routeSuggestion = (RouteSuggestion) searchSuggestion;
                // 设置为是历史
                DataHelper.addSuggestions(routeSuggestion);
                routeSuggestion.setIsHistory(true);
                mLastQuery = searchSuggestion.getBody();

                // 如果是直接点击推荐项，则启动相应的路线界面
                if (routeSuggestion.getId() != -1) {
                    Route route = DataSupport.find(Route.class, routeSuggestion.getId());
                    Intent intent = new Intent(Index.this, RouteIndex.class);
                    intent.putExtra("route", route);
                    startActivity(intent);
                } else {
                    DataHelper.findRoutes(Index.this, routeSuggestion.getBody(),
                            new DataHelper.OnFindRoutesListener() {
                                @Override
                                public void onResults(List<Route> results) {
                                    Intent intent = new Intent(Index.this, SearchResult.class);
                                    intent.putExtra("string", routeSuggestion.getBody());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.in_right, R.anim.out_left);
                                }
                            });
                }
            }

            // 搜索键点击
            @Override
            public void onSearchAction(final String currentQuery) {
                mLastQuery = currentQuery;

                // 若是点击搜索键的，则加入不是路线的历史
                RouteSuggestion routeSuggestion = new RouteSuggestion(currentQuery, "", -1);
                routeSuggestion.setIsRoute(false);
                routeSuggestion.setIsHistory(true);
                DataHelper.addSuggestions(routeSuggestion);

                // 如果是点击搜索键，则显示符合条件的列表
                DataHelper.findRoutes(Index.this, currentQuery,
                        new DataHelper.OnFindRoutesListener() {
                            @Override
                            public void onResults(List<Route> results) {
                                Intent intent = new Intent(Index.this, SearchResult.class);
                                intent.putExtra("string", currentQuery);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_right, R.anim.out_left);
                            }
                        });
            }
        });

        // 获得输入焦点与失去输入焦点
        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.swapSuggestions(DataHelper.getHistory(Index.this, 3));
            }

            @Override
            public void onFocusCleared() {
                mSearchView.setSearchBarTitle(mLastQuery);
            }
        });

        // 重载推荐项的样式
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                RouteSuggestion routeSuggestion = (RouteSuggestion) item;
                String textColor = "#000000";
                String textLight = "#787878";

                // 如果是历史项
                if(routeSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history, null));
                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                }
                // 如果是正常搜索的推荐项
                else {
                    leftIcon.setAlpha(.72f);
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_search_commit, null));
                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                }

                textView.setTextColor(Color.parseColor(textColor));
                // 替换已输入文字颜色
                String text = routeSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                text = text + "\t" + routeSuggestion.getRouteIntro();
                textView.setText(Html.fromHtml(text));
            }
        });

        // 添加侧边栏
        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        // 侧边栏点击逻辑
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                switch (item.getItemId()) {
                    case R.id.nav_information:
                        break;
                    case R.id.nav_logout:
                        // 退出登录逻辑，清空记录
                        SharedPreferences sharedPreferences = getSharedPreferences("config", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", "");
                        editor.putString("password", "");
                        editor.apply();
                        user = null;
                        Intent intent = new Intent(Index.this, Login.class);
                        startActivity(intent);
                        finish();
                }
                return true;
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_pager);
        IndexPagerAdapter adapter = new IndexPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // 获得登录用户信息
        final String name = user.getName();
        final String intro = user.getIntro();

        // 处理登录信息
        View headerView = navView.getHeaderView(0);
        // 头像点击进入主页
        CircleImageView mIcon = (CircleImageView) headerView.findViewById(R.id.icon_image);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(Index.this, UserIndex.class);
                intent.putExtra("user", user.getId());
                startActivity(intent);
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
            }
        });
        TextView user_name = (TextView) headerView.findViewById(R.id.name);
        user_name.setText(name);
        TextView user_intro = (TextView) headerView.findViewById(R.id.intro);
        user_intro.setText(intro);
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
        Glide.with(Index.this).load(ads.get(index).getImageId()).into(iv);
        mViews.add(iv);
    }

    /**
     * 初始化路线
     */
    private void initRoutes() {
        User founder = new User();
        founder.setUsername("cj");
        founder.setPassword(MD5Util.getMD5("123456"));
        founder.setStarRoutes(",");
        founder.setName("CJ");
        founder.save();

        Route one = new Route(R.drawable.route_1, "Biker", "西安两天两夜骑行路线", 300, founder.getId());
        Route two = new Route(R.drawable.route_2, "Adventure", "台湾浪呀嘛浪打狼", 500, founder.getId());
        Route three = new Route(R.drawable.route_3, "Peaker", "华山徒手攀爬历险记", 400, founder.getId());
        one.save();
        two.save();
        three.save();

        Article beach = new Article(R.drawable.article_1, "最美海滩", "世界上最美的十大海滩 \n再不去就老了，带上心爱的他她，说走就走", "二月 21", founder.getId());
        Article modern = new Article(R.drawable.article_2, "现代建筑", "堪比鬼斧神工 \n看看建筑师眼中的几何与我们都有哪些不同", "二月 11", founder.getId());
        beach.save();
        modern.save();
    }
}
