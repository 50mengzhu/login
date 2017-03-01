package com.gyh.login.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.UUID;

public class Route extends DataSupport implements Parcelable {

    private UUID id;
    private int imageId;
    private String title;
    private String intro;
    private int price;
    private User founder;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(title);
        dest.writeString(intro);
        dest.writeInt(price);
        dest.writeParcelable(founder, flags);
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
        imageId = in.readInt();
        title = in.readString();
        intro = in.readString();
        price = in.readInt();
        founder = in.readParcelable(User.class.getClassLoader());
    }

    public Route(int initImageId, String initTitle, String initIntro, int initPrice, User initFounder) {
        imageId = initImageId;
        title = initTitle;
        intro = initIntro;
        price = initPrice;
        founder = initFounder;
    }

    public Route(int initImageId, String initTitle,String initIntro, int initPrice) {
        imageId = initImageId;
        title = initTitle;
        intro = initIntro;
        price = initPrice;
        founder = new User();
        founder.setName("CJJJJJ");
        founder.setIntro("酷");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public User getFounder() {
        return founder;
    }

    public void setFounder(User founder) {
        this.founder = founder;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
