package com.gyh.login.db;

import org.litepal.crud.DataSupport;

import java.util.UUID;

public class Article extends DataSupport {

    private UUID id;
    private int imageId;
    private String title;
    private String info;
    private String date;

    public Article(int initImageId, String initTitle, String initInfo, String initDate) {
        imageId = initImageId;
        title = initTitle;
        info = initInfo;
        date = initDate;
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
}
