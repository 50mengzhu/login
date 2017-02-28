package com.gyh.login.db;

import org.litepal.crud.DataSupport;

import java.util.UUID;

public class Route extends DataSupport {

    private UUID id;
    private int imageId;
    private String title;
    private int price;
    private User founder;

    public Route(int initImageId, String initTitle, int initPrice, User initFounder) {
        imageId = initImageId;
        title = initTitle;
        price = initPrice;
        founder = initFounder;
    }

    public Route(int initImageId, String initTitle, int initPrice) {
        imageId = initImageId;
        title = initTitle;
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
}
