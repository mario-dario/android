package com.development.dariopal.otto.events;

import com.neura.standalonesdk.events.NeuraEvent;

/**
 * Created by rt_okun on 28/07/2016.
 */
public class NeuraPushEvent extends BaseEvent {

    NeuraEvent neuraEvent;

    public NeuraPushEvent(int eventType, NeuraEvent neuraEvent) {
        super(eventType);
        this.neuraEvent = neuraEvent;
    }

}
