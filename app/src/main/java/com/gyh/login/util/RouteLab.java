package com.gyh.login.util;

import android.content.Context;

import com.gyh.login.R;
import com.gyh.login.db.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RouteLab {
    private static RouteLab sRouteLab;

    private List<Route> mRoutes;

    public static RouteLab get(Context context) {
        if (sRouteLab == null) {
            sRouteLab = new RouteLab(context);
        }
        return sRouteLab;
    }

    private RouteLab(Context context) {
        mRoutes = new ArrayList<>();
        Route one = new Route(R.drawable.route_1, "Biker", 300, null);
        Route two = new Route(R.drawable.route_2, "Adventure", 500, null);
        Route three = new Route(R.drawable.route_3, "Peaker", 400, null);
        mRoutes.add(one);
        mRoutes.add(two);
        mRoutes.add(three);
    }

    public List<Route> getRoutes() {
        return mRoutes;
    }

    public Route getRoute(UUID id) {
        for (Route route : mRoutes) {
            if (route.getId().equals(id)) {
                return route;
            }
        }
        return  null;
    }
}
