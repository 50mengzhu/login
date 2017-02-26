package com.gyh.login.banner;

public interface DataSetSubject {
    void registerSubscriber(DataSetSubscriber subscriber);
    void removeSubscriber(DataSetSubscriber subscriber);
    void notifySubscriber();
}
