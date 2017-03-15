package com.gyh.login.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Marker implements Parcelable {

    private double longitude;
    private double latitude;
    private String title;
    private String snippet;
    private String method;
    private String time;
    private String tag;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(title);
        dest.writeString(snippet);
        dest.writeString(method);
        dest.writeString(time);
        dest.writeString(tag);
    }

    public static final Parcelable.Creator<Marker> CREATOR = new Creator<Marker>() {
        @Override
        public Marker createFromParcel(Parcel source) {
            return new Marker(source);
        }

        @Override
        public Marker[] newArray(int size) {
            return new Marker[size];
        }
    };

    public Marker(Parcel in) {
        longitude = in.readInt();
        latitude = in.readInt();
        title = in.readString();
        snippet = in.readString();
        method = in.readString();
        time = in.readString();
        tag = in.readString();
    }

    // longitude 经度; latitude 纬度
    public Marker(double longitude, double latitude, String title, String snippet, String method, String time, String tag) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
        this.method = method;
        this.time = time;
        this.tag = tag;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
