package com.development.dariopal;

import android.app.Application;
import android.content.Context;

/**
 * Created by rt_okun on 28/07/2016.
 */
public class DarioPalApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

}
