package com.development.dariopal;


import com.orm.SugarApp;
import android.content.Context;


public class DarioPalApplication  extends SugarApp{




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
