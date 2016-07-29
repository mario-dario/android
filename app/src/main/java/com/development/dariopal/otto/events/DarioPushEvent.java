package com.development.dariopal.otto.events;

import com.development.dariopal.dario.ExportDarioLogEntryDataSerializable;

/**
 * Created by mario on 29/07/16.
 */

public class DarioPushEvent extends BaseEvent
{
    public ExportDarioLogEntryDataSerializable darioEvent;

    public DarioPushEvent(int eventType, ExportDarioLogEntryDataSerializable darioEvent) {
        super(eventType);
        this.darioEvent = darioEvent;
    }
}
