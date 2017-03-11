package com.gyh.login.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Route implements Parcelable {

    private int id;
    private int imageId;
    private String title;
    private String intro;
    private int price;
    private int founderId;
    private List<Marker> markers;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(imageId);
        dest.writeString(title);
        dest.writeString(intro);
        dest.writeInt(price);
        dest.writeInt(founderId);
        dest.writeList(markers);
    }

    public static final Parcelable.Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel source) {
            return new Route(source);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    public Route(Parcel in) {
        id = in.readInt();
        imageId = in.readInt();
        title = in.readString();
        intro = in.readString();
        price = in.readInt();
        founderId = in.readInt();
        markers = new ArrayList<>();
        in.writeList(markers);
    }

    public Route(int initImageId, String initTitle, String initIntro, int initPrice, int initFounderId) {
        imageId = initImageId;
        title = initTitle;
        intro = initIntro;
        price = initPrice;
        founderId = initFounderId;
        markers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFounder() {
        return founderId;
    }

    public void setFounder(int founderId) {
        this.founderId = founderId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void addMarker(Marker marker) {
        markers.add(marker);
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    public List<Marker> getMarkers() {
        return this.markers;
    }
}
