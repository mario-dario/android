package com.labstyle.darioandroid.hackathon;

import com.labstyle.darioandroid.database.event.DarioTransactionEventEntity;

/**
 * Created by mario on 27/07/16.
 */

public class DarioTransactionBusEvent
{
    final public DarioTransactionEventEntity entity;
    public DarioTransactionBusEvent(DarioTransactionEventEntity event)
    {
        this.entity=event;
    }
}
