package com.gyh.login.util;

import android.content.Context;

import com.gyh.login.R;
import com.gyh.login.db.Ad;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdLab {
    private static AdLab sAdLab;

    private List<Ad> mAds;

    public static AdLab get(Context context) {
        if (sAdLab == null) {
            sAdLab = new AdLab(context);
        }
        return sAdLab;
    }

    private AdLab(Context context) {
        mAds = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Ad one = new Ad(R.drawable.ad_1);
            mAds.add(one);
            Ad two = new Ad(R.drawable.ad_2);
            mAds.add(two);
            Ad three = new Ad(R.drawable.ad_3);
            mAds.add(three);
        }
    }

    public List<Ad> getAds() {
        return mAds;
    }

    public Ad getAd(UUID id) {
        for (Ad ad : mAds) {
            if (ad.getId().equals(id)) {
                return ad;
            }
        }
        return null;
    }
}
