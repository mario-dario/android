package com.development.dariopal.otto;

import android.content.Context;

import com.development.dariopal.MainActivity;
import com.development.dariopal.otto.events.BaseEvent;
import com.squareup.otto.Bus;

/**
 * Created by rt_okun on 28/07/2016.
 */
public class BusManager {

    private Bus bus;

    private static BusManager ourInstance = new BusManager();

    public static BusManager getInstance() {
        return ourInstance;
    }

    private BusManager() {
        bus = new Bus();
    }

    public void post(BaseEvent event) {
        bus.post(event);
    }


    public void register(Context context) {
        bus.register(context);
    }

    public void unregister(Context context) {
        bus.register(context);
    }
}
