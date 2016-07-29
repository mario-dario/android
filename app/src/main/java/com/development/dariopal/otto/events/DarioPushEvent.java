package com.development.dariopal.otto.events;

import com.development.dariopal.dario.ExportDarioLogEntryDataSerializable;

/**
 * Created by mario on 29/07/16.
 */

public class DarioPushEvent extends BaseEvent
{
    ExportDarioLogEntryDataSerializable darioEvent;

    public DarioPushEvent(int eventType, ExportDarioLogEntryDataSerializable neuraEvent) {
        super(eventType);
        this.darioEvent = neuraEvent;
    }
}
