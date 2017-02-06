package com.gyh.login.db;

import java.util.UUID;

public class Ad {
    private UUID id;
    private int imageId;

    public Ad(int imageId) {
        this.imageId = imageId;
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public int getImageId() {
        return imageId;
    }
}
