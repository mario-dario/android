package com.development.dariopal.otto.events;

/**
 * Created by rt_okun on 28/07/2016.
 */
public abstract class BaseEvent{

    private int eventType;

    public BaseEvent(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }
}
