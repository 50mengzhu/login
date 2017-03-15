package com.gyh.login.Stepper;

import android.support.v4.app.Fragment;

import java.util.Observable;

public class StepperItem extends Observable {

    private String label;
    private String subLabel;
    private boolean buttonEnable = true;
    private Fragment fragment;

    private boolean displayed = false;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean isPositiveButtonEnable() {
        return buttonEnable;
    }

    public void setPostiveButtonEnable(boolean buttonEnable) {
        synchronized (this) {
            this.buttonEnable = buttonEnable;
        }
        setChanged();
        notifyObservers();
    }

    protected synchronized boolean isDisplayed() {
        return displayed;
    }

    protected void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }
}
