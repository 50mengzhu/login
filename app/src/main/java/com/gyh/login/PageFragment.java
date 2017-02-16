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
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        if (mPage == 1) {
            RouteLab routeLab = RouteLab.get(getContext());
            List<Route> routes = routeLab.getRoutes();

            RecyclerView routeItems = (RecyclerView) view.findViewById(R.id.route_items);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            routeItems.setLayoutManager(layoutManager);
            RoutesAdapter adapter = new RoutesAdapter(routes);
            routeItems.setAdapter(adapter);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.team_items);
            layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new RoutesAdapter(routes);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}
