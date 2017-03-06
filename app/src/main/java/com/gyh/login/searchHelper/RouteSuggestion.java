package com.gyh.login.searchHelper;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class RouteSuggestion implements SearchSuggestion {

    private String mRouteName;
    private String mRouteIntro;
    private int mRouteId;
    private boolean mIsRoute = true;
    private boolean mIsHistory = false;

    public RouteSuggestion(String suggestion, String intro, int routeId) {
        this.mRouteName = suggestion.toLowerCase();
        this.mRouteIntro = intro;
        this.mRouteId = routeId;
    }

    public RouteSuggestion(Parcel source) {
        this.mRouteName = source.readString();
        this.mRouteIntro = source.readString();
        this.mRouteId = source.readInt();
        this.mIsRoute = source.readInt() != 0;
        this.mIsHistory = source.readInt() != 0;
    }

    public int getId() {
        return this.mRouteId;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    public void setIsRoute(boolean isRoute) {
        this.mIsRoute = isRoute;
    }

    public boolean getIsRoute() {
        return this.mIsRoute;
    }

    public String getRouteIntro() {
        return mRouteIntro;
    }

    public boolean isEqual(RouteSuggestion routeSuggestion) {
        return (this.mRouteName.equals(routeSuggestion.getBody()) && this.mRouteIntro.equals(routeSuggestion.getRouteIntro()));
    }

    @Override
    public String getBody() {
        return mRouteName;
    }

    public static final Creator<RouteSuggestion> CREATOR = new Creator<RouteSuggestion>() {
        @Override
        public RouteSuggestion createFromParcel(Parcel source) {
            return new RouteSuggestion(source);
        }

        @Override
        public RouteSuggestion[] newArray(int size) {
            return new RouteSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRouteName);
        dest.writeString(mRouteIntro);
        dest.writeInt(mRouteId);
        dest.writeInt(mIsRoute ? 1 : 0);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}
