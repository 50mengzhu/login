package com.gyh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.gyh.login.banner.BannerViewPager;
import com.gyh.login.banner.OnPageClickListener;
import com.gyh.login.banner.ViewPagerAdapter;
import com.gyh.login.db.Ad;
import com.gyh.login.db.Article;
import com.gyh.login.db.Route;
import com.gyh.login.db.User;
import com.gyh.login.lab.AdLab;
import com.gyh.login.util.IndexPagerAdapter;
import com.gyh.login.util.MD5Util;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Index extends AppCompatActivity {

    public static User user;
    private List<Route> routes = DataSupport.findAll(Route.class);
    private List<Article> articles = DataSupport.findAll(Article.class);

    private List<ImageView> mViews = new ArrayList<>();

    private ViewPagerAdapter mAdapter;
    private BannerViewPager mBannerViewPager;
    private DrawerLayout mDrawerLayout;
    private FloatingSearchView mSearchView;

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

        //搜索逻辑
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

                //pass them on to the search view
                //mSearchView.swapSuggestions(newSuggestions);
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
                intent.putExtra("user", user);
                startActivity(intent);
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
