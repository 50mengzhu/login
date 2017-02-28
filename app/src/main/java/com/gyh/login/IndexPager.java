package com.gyh.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gyh.login.db.Article;
import com.gyh.login.db.Route;
import com.gyh.login.lab.ArticleLab;
import com.gyh.login.lab.RouteLab;
import com.gyh.login.util.ArticlesAdapter;
import com.gyh.login.util.RoutesAdapter;

import java.util.List;

public class IndexPager extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    private Context mContext;

    public static IndexPager newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        IndexPager fragment = new IndexPager();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;

        mContext = getContext();

        if (mPage == 1) {
            view = inflater.inflate(R.layout.fragment_index, container, false);

            RouteLab routeLab = RouteLab.get(mContext);
            List<Route> routes = routeLab.getRoutes();

            RecyclerView routeItems = (RecyclerView) view.findViewById(R.id.route_items);
            routeItems.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            routeItems.setLayoutManager(layoutManager);
            RoutesAdapter adapter = new RoutesAdapter(routes);
            routeItems.setAdapter(adapter);
            // 查看更多
            LinearLayout routeMore = (LinearLayout) view.findViewById(R.id.route_more);
            routeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RouteAll.class);
                    intent.putExtra("page", 0);
                    mContext.startActivity(intent);
                }
            });

            RecyclerView teamItems = (RecyclerView) view.findViewById(R.id.team_items);
            teamItems.setNestedScrollingEnabled(false);
            layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            teamItems.setLayoutManager(layoutManager);
            adapter = new RoutesAdapter(routes);
            teamItems.setAdapter(adapter);
            // 查看更多
            LinearLayout teamMore = (LinearLayout) view.findViewById(R.id.team_more);
            teamMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RouteAll.class);
                    intent.putExtra("page", 1);
                    mContext.startActivity(intent);
                }
            });
        } else if (mPage == 2) {
            view = inflater.inflate(R.layout.fragment_public, container, false);

            ArticleLab articleLab = ArticleLab.get(mContext);
            List<Article> articles = articleLab.getArticles();

            RecyclerView articleItems = (RecyclerView) view.findViewById(R.id.public_articles);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            articleItems.setLayoutManager(layoutManager);
            ArticlesAdapter adapter = new ArticlesAdapter(articles);
            articleItems.setAdapter(adapter);

            // 下拉刷新逻辑
            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.public_refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshArticles(swipeRefreshLayout);
                }
            });

        } else if (mPage == 3) {
            view = inflater.inflate(R.layout.fragment_other, container, false);
        }
        return view;
    }

    // 刷新接口
    private void refreshArticles(final SwipeRefreshLayout swipeRefreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
