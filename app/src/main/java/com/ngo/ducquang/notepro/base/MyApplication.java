package com.ngo.ducquang.notepro.base;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.ngo.ducquang.notepro.R;

/**
 * Created by ducqu on 9/26/2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.idAdmob));
    }
}
