package com.gyh.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.gyh.login.SwipeBack.SwipeBackActivity;
import com.gyh.login.db.Route;
import com.gyh.login.searchHelper.DataHelper;
import com.gyh.login.searchHelper.RouteSuggestion;
import com.gyh.login.searchHelper.SearchResultsListAdapter;
import com.gyh.login.util.RoutesAdapter;

import java.util.List;

import static com.gyh.login.Index.routes;

public class SearchResult extends SwipeBackActivity {

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private FloatingSearchView mSearchView;
    private RecyclerView mSearchResultList;
    private SearchResultsListAdapter mSearchResultsAdapter;

    private LinearLayout mNoResultPart;

    private String mLastQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mSearchView= (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchResultList = (RecyclerView) findViewById(R.id.search_results_list);
        mNoResultPart = (LinearLayout) findViewById(R.id.no_search_part);

        // 搜索推荐项显示
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();

                    DataHelper.findSuggestions(SearchResult.this, newQuery, 5,
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
                RouteSuggestion routeSuggestion = (RouteSuggestion) searchSuggestion;
                // 设置为是历史
                DataHelper.addSuggestions(routeSuggestion);
                routeSuggestion.setIsHistory(true);
                mLastQuery = searchSuggestion.getBody();

                // 如果是直接点击推荐项，则启动相应的路线界面
                if (routeSuggestion.getId() != -1) {
                    Route route = null;
                    for (Route r : routes) {
                        if(r.getId() == routeSuggestion.getId()) {
                            route = r;
                            break;
                        }
                    }
                    Intent intent = new Intent(SearchResult.this, RouteIndex.class);
                    intent.putExtra("route", route.getId());
                    startActivity(intent);
                } else {
                    DataHelper.findRoutes(SearchResult.this, routeSuggestion.getBody(),
                            new DataHelper.OnFindRoutesListener() {
                                @Override
                                public void onResults(List<Route> results) {
                                    mSearchResultsAdapter.swapData(results);
                                }
                            });
                }
            }

            // 搜索键点击
            @Override
            public void onSearchAction(String currentQuery) {
                mLastQuery = currentQuery;

                // 若是点击搜索键的，则加入不是路线的历史
                RouteSuggestion routeSuggestion = new RouteSuggestion(currentQuery, "", -1);
                routeSuggestion.setIsRoute(false);
                routeSuggestion.setIsHistory(true);
                DataHelper.addSuggestions(routeSuggestion);

                // 如果是点击搜索键，则显示符合条件的列表
                DataHelper.findRoutes(SearchResult.this, currentQuery,
                        new DataHelper.OnFindRoutesListener() {
                            @Override
                            public void onResults(List<Route> results) {
                                mSearchResultsAdapter.swapData(results);
                                if(results.size() != 0) {
                                    mNoResultPart.setVisibility(View.GONE);
                                } else {
                                    mNoResultPart.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });

        // 获得输入焦点与失去输入焦点
        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.swapSuggestions(DataHelper.getHistory(SearchResult.this, 3));
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

        // 高度改变
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultList.setTranslationY(newHeight);
            }
        });

        // 返回按钮
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                SearchResult.this.getTouchHelper().startSlide();
                SearchResult.this.getTouchHelper().startAnimating(true, 0);
            }
        });

        mSearchResultsAdapter = new SearchResultsListAdapter();

        // 设置搜索结果
        mSearchResultList.post(new Runnable() {
            @Override
            public void run() {
                GridLayoutManager layoutManager = new GridLayoutManager(SearchResult.this, 2);
                mSearchResultList.setLayoutManager(layoutManager);
                mSearchResultList.setAdapter(mSearchResultsAdapter);
                mSearchResultsAdapter.setItemWidth((int) ((mSearchResultList.getWidth()) / 2));
            }
        });

        // 传入搜索字符，直接开始搜索
        DataHelper.findRoutes(SearchResult.this, getIntent().getStringExtra("string"),
                new DataHelper.OnFindRoutesListener() {
                    @Override
                    public void onResults(List<Route> results) {
                        mSearchResultsAdapter.swapData(results);
                        if (results.size() != 0) {
                            mNoResultPart.setVisibility(View.GONE);
                        } else {
                            mNoResultPart.setVisibility(View.VISIBLE);
                        }
                    }
                });

        final RoutesAdapter adapter = new RoutesAdapter(routes);
        adapter.setFlag(1);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fav_routes);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResult.this);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(layoutManager);
                adapter.setItemWidth((int) ((recyclerView.getWidth()) / 2.2));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
