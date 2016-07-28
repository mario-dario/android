package com.development.dariopal.otto;

import android.content.Context;

import com.development.dariopal.MainActivity;
import com.development.dariopal.otto.events.BaseEvent;
import com.squareup.otto.Bus;

/**
 * Created by rt_okun on 28/07/2016.
 */
public class BusManager {


    private static Bus instance = null;

    private BusManager() {
    }

    public static Bus getInstance() {
        if (instance == null) {
            instance = new Bus();
        }

        return instance;
    }
}

