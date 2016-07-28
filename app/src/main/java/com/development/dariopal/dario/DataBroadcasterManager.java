package com.labstyle.darioandroid.hackathon;

import android.content.Context;
import android.content.Intent;

import com.labstyle.darioandroid.crosslibtools.eventBus.EventBus;
import com.labstyle.darioandroid.dariosharedclasses.DarioIntentManager;

import com.labstyle.darioandroid.dariosharedclasses.ExportDarioLogEntryDataSerializable;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by mario on 27/07/16.
 */


public class DataBroadcasterManager
{
    private final Context mApplicationContext;


    @Inject
    public DataBroadcasterManager(Context mApplicationContext)
    {
        this.mApplicationContext = mApplicationContext;
        EventBus.getInstanceForMainThread().register(this);
   }

    @Subscribe
    public void newDarioTransaction(DarioTransactionBusEvent event)
    {
        broadcastMe(DarioEventConverter.darioTransactionToDarioExportedLogEntry(event.entity));

    }

    private void broadcastMe(ExportDarioLogEntryDataSerializable darioLogEntryData)
    {
        Intent intent = DarioIntentManager.newIntentWithLogEntryData(darioLogEntryData);
        mApplicationContext.sendBroadcast(intent);
    }
}


