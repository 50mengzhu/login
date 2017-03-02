package com.gyh.login.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

public class Article extends DataSupport implements Parcelable {

    private int id;
    private int imageId;
    private String title;
    private String info;
    private String date;
    private int writerId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(imageId);
        dest.writeString(title);
        dest.writeString(info);
        dest.writeString(date);
        dest.writeInt(writerId);
    }

    public static final Parcelable.Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public Article(Parcel in) {
        id = in.readInt();
        imageId = in.readInt();
        title = in.readString();
        info = in.readString();
        date = in.readString();
        writerId = in.readInt();
    }

    public Article(int initImageId, String initTitle, String initInfo, String initDate, int initWriterId) {
        imageId = initImageId;
        title = initTitle;
        info = initInfo;
        date = initDate;
        writerId = initWriterId;
    }

    public int getId() {
        return id;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWriter() {
        return writerId;
    }

    public void setWriter(int writerId) {
        this.writerId = writerId;
    }
}
