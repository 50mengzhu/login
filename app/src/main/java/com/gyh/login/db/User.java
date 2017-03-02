package com.gyh.login.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;


public class User extends DataSupport implements Parcelable{

    private int id;
    private String username;
    private String password;
    private String name;
    private String intro;
    // 记录收藏的路线
    private String starRoutes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public  void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(username);
        out.writeString(password);
        out.writeString(name);
        out.writeString(intro);
        out.writeString(starRoutes);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        name = in.readString();
        intro = in.readString();
        starRoutes = in.readString();
    }

    public User() {
        username = "username";
        password = "password";
        name = "route";
        intro = "一句话介绍一下你自己";
        starRoutes = ",";
    }

    public String getStarRoutes() {
        return starRoutes;
    }

    public void setStarRoutes(String starRoutes) {
        this.starRoutes = starRoutes;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
