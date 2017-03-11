package com.gyh.login.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Marker implements Parcelable {

    private double longitude;
    private double latitude;
    private String title;
    private String snippet;

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
    }

    // longitude 经度; latitude 纬度
    public Marker(double longitude, double latitude, String title, String snippet) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
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

}
