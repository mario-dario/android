package com.development.dariopal.service;

import android.content.Context;

import com.development.dariopal.database.DBManagerInterface;
import com.development.dariopal.neura_manager.NeuraEventConstants;
import com.development.dariopal.notifications.NotificationWrapper;
import com.neura.standalonesdk.events.NeuraEvent;

/**
 * Created by mario on 29/07/16.
 */
public class businessLogicManager
{
    private final Context mDarioPalService;
    private final DBManagerInterface mDbManagerInterface;
    private final NotificationWrapper mNotificationManager;

    public businessLogicManager(Context darioPalService, DBManagerInterface dbManagerInterface, NotificationWrapper notificationManager)
    {
        mDarioPalService=darioPalService;
        mDbManagerInterface=dbManagerInterface;
        mNotificationManager=notificationManager;
        mNotificationManager.buildNotification();
    }


    public void onNewNeuraEvent(NeuraEvent event)
    {
        if (event.getEventName().equals(NeuraEventConstants.USER_ARRIVED_TO_GYM))
        {
            if(isLastMeasurementAbnormal())
                mNotificationManager.updateNotification("test","test body");
        }

//        }else if(event.getEventName().equals(NeuraEventConstants.USER_LEFT_THE_GYM)
//        {
//        }else if(event.getEventName().equals(NeuraEventConstants.USER_FINISHED_WORKOUT)
//        {
//        }
    }

    private boolean isLastMeasurementAbnormal()
    {
        return true;
    }
}
