package com.gyh.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyh.login.db.Route;
import com.gyh.login.util.RouteLab;
import com.gyh.login.util.RoutesAdapter;

import java.util.List;

public class PageFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        PageFragment fragment = new PageFragment();
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

        if (mPage == 1) {
            view = inflater.inflate(R.layout.fragment_index, container, false);

            RouteLab routeLab = RouteLab.get(getContext());
            List<Route> routes = routeLab.getRoutes();

            RecyclerView routeItems = (RecyclerView) view.findViewById(R.id.route_items);
            routeItems.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            routeItems.setLayoutManager(layoutManager);
            RoutesAdapter adapter = new RoutesAdapter(routes);
            routeItems.setAdapter(adapter);

            RecyclerView teamItems = (RecyclerView) view.findViewById(R.id.team_items);
            teamItems.setNestedScrollingEnabled(false);
            layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            teamItems.setLayoutManager(layoutManager);
            adapter = new RoutesAdapter(routes);
            teamItems.setAdapter(adapter);
        } else if (mPage == 2) {
            view = inflater.inflate(R.layout.fragment_public, container, false);
        } else if (mPage == 3) {
            view = inflater.inflate(R.layout.fragment_other, container, false);
        }

        return view;
    }
}
