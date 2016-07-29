package com.development.dariopal.service;

import android.content.Context;

import com.development.dariopal.database.DBManagerInterface;
import com.development.dariopal.database.EventRecord;
import com.development.dariopal.neura_manager.NeuraEventConstants;
import com.development.dariopal.notifications.NotificationWrapper;
import com.neura.standalonesdk.events.NeuraEvent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by mario on 29/07/16.
 */
public class businessLogicManager
{
    private final Context mDarioPalService;
    private final DBManagerInterface mDbManagerInterface;
    private final NotificationWrapper mNotificationManager;
    public final  String TAG;

    public businessLogicManager(Context darioPalService, DBManagerInterface dbManagerInterface, NotificationWrapper notificationManager)
    {
       TAG = this.getClass().getName();

        mDarioPalService=darioPalService;
        mDbManagerInterface=dbManagerInterface;
        mNotificationManager=notificationManager;
        mNotificationManager.buildNotification();
    }


    public void onNewNeuraEvent(NeuraEvent event)
    {
        if (event.getEventName().equals(NeuraEventConstants.USER_ARRIVED_TO_GYM))
        {
            isLastMeasurementAbnormal(event);
            mNotificationManager.updateNotification("test","test body");
        }

//        }else if(event.getEventName().equals(NeuraEventConstants.USER_LEFT_THE_GYM)
//        {
//        }else if(event.getEventName().equals(NeuraEventConstants.USER_FINISHED_WORKOUT)
//        {
//        }
    }

    private boolean isLastMeasurementAbnormal(NeuraEvent event)
    {
        Calendar inHourBack = Calendar.getInstance();
        inHourBack.setTimeInMillis(event.getEventTimestamp());
        inHourBack.add(Calendar.HOUR, -1);

        List<EventRecord> eventRecordList = mDbManagerInterface.onGetFromDB(inHourBack.getTimeInMillis(),event.getEventTimestamp());
        if(eventRecordList.size()>0)
            return true;
//
//        Log.e(TAG,"getTimeInMillis start" + inHourBack.getTimeInMillis());
//        Log.e(TAG,"getTimeInMillis end" + event.getEventTimestamp());



//        //List<EventRecord> eventAllRecordList = mDbManagerInterface.getAllRecordFromDB();
//        for (EventRecord REvent:eventRecordList)
//        {
////            Log.e(TAG,"REvent.getType()" + REvent.getType());
////            Log.e(TAG,"REvent.getValue()" + REvent.getValue());
////            Log.e(TAG,"REvent.getTimeStamp()" + REvent.getTimeStamp());
//        }
//        return true;
        return false;
    }
}
