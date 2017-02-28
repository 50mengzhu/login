package com.gyh.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyh.login.db.Route;
import com.gyh.login.lab.RouteLab;
import com.gyh.login.util.RoutesAdapter;

import java.util.List;

public class RoutePager extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    private Context mContext;

    public static RoutePager newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        RoutePager fragment = new RoutePager();
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
            view = inflater.inflate(R.layout.fragment_routes, container, false);

            final RecyclerView routeItems = (RecyclerView) view.findViewById(R.id.route_items);
            routeItems.setHasFixedSize(true);

            routeItems.post(new Runnable() {
                @Override
                public void run() {
                    RouteLab routeLab = RouteLab.get(mContext);
                    List<Route> routes = routeLab.getRoutes();

                    GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
                    routeItems.setLayoutManager(layoutManager);
                    RoutesAdapter adapter = new RoutesAdapter(routes);
                    adapter.setFlag(1);
                    adapter.setItemWidth((int) ((routeItems.getWidth()) / 2));
                    routeItems.setAdapter(adapter);
                }
            });

        } else if (mPage == 2) {
            view = inflater.inflate(R.layout.fragment_teams, container, false);
        }

        return view;
    }

}
