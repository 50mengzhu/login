package com.gyh.login.searchHelper;

import android.content.Context;
import android.widget.Filter;

import com.gyh.login.Index;
import com.gyh.login.db.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {

    private static List<Route> sRoutes = Index.allRoutes;

    private static List<RouteSuggestion> sRouteSuggestions = new ArrayList<>();
    private static List<RouteSuggestion> sHistorySuggestions = new ArrayList<>();


    public interface OnFindRoutesListener {
        void onResults(List<Route> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<RouteSuggestion> results);
    }

    public static List<RouteSuggestion> getHistory(Context context, int count) {
        List<RouteSuggestion> suggestionList = new ArrayList<>();
        RouteSuggestion routeSuggestion;
        for (int i = 0; i < sHistorySuggestions.size(); i++) {
            routeSuggestion = sHistorySuggestions.get(i);
            suggestionList.add(routeSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        sHistorySuggestions.clear();
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        initSuggestions(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<RouteSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {
                    for (RouteSuggestion suggestion : sRouteSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase()) && suggestion.getIsRoute()) {
                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<RouteSuggestion>() {
                    @Override
                    public int compare(RouteSuggestion lhs, RouteSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<RouteSuggestion>) results.values);
                }
            }
        }.filter(query);
    }

    public static void findRoutes(Context context, String query, final OnFindRoutesListener listener) {
        new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Route> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {
                    for (Route route : sRoutes) {
                        if (route.getTitle().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())){
                            suggestionList.add(route);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<Route>) results.values);
                }
            }
        }.filter(query);
    }

    private static void initSuggestions(Context context) {
        if (sRouteSuggestions.isEmpty()) {
            for (Route route : sRoutes) {
                sRouteSuggestions.add(new RouteSuggestion(route.getTitle(), route.getIntro(), route.getId()));
            }
        }
    }

    // 增加历史内容
    public static void addSuggestions(RouteSuggestion suggestion) {
        List<RouteSuggestion> temp = new ArrayList<>();
        temp.add(suggestion);
        for(int i = 0; i < sHistorySuggestions.size(); i++) {
            RouteSuggestion route = sHistorySuggestions.get(i);
            if (!route.isEqual(suggestion)) {
                temp.add(route);
            }

            if (temp.size() == 3) {
                break;
            }
        }
        sHistorySuggestions = temp;
    }

}
