package com.development.dariopal.service;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.development.dariopal.MainActivity;
import com.development.dariopal.R;
import com.development.dariopal.dario.DarioDataReceiver;
import com.development.dariopal.dario.ExportDarioLogEntryDataSerializable;
import com.development.dariopal.dario.IDarioDataHandler;
import com.development.dariopal.database.DBManager;
import com.development.dariopal.notifications.NotificationWrapper;
import com.development.dariopal.otto.BusManager;
import com.development.dariopal.otto.Const;
import com.development.dariopal.otto.events.BaseEvent;
import com.development.dariopal.otto.events.DarioPushEvent;
import com.development.dariopal.otto.events.NeuraPushEvent;
import com.squareup.otto.Subscribe;

import java.util.List;


public class DarioPalService extends Service
{
    private DBManager dbManagerInterface;
    private static String TAG = DarioPalService.class.getName();



    private static DarioPalService instance = null;
    private businessLogicManager mBusinessLogicManager;
    private NotificationWrapper notificationManager;

    public DarioPalService()
    {
    }

    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = 111;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder
    {
        DarioPalService getService()
        {
            return DarioPalService.this;
        }
    }

    @Override
    public void onCreate()
    {
        instance=this;
        BusManager.getInstance().register(this);
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();

        dbManagerInterface = new DBManager(this);
        DarioDataReceiver.getDarioDataReceiver().start(this, new IDarioDataHandler() {
            @Override
            public void onSuccess(ExportDarioLogEntryDataSerializable exportDarioLogEntryDataSerializable) {
                BusManager.getInstance().post(new DarioPushEvent(Const.DARIO_EVENT, exportDarioLogEntryDataSerializable));
                dbManagerInterface.onSaveToDB(exportDarioLogEntryDataSerializable);

            }
        });

        notificationManager = new  NotificationWrapper(this.getApplicationContext());
        mBusinessLogicManager = new businessLogicManager(this,dbManagerInterface,notificationManager);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        instance=null;
        // Cancel the persistent notification.
        BusManager.getInstance().unregister(this);
        mNM.cancel(NOTIFICATION);

        DarioDataReceiver.getDarioDataReceiver().stop();

        // Tell the user we stopped.
        Toast.makeText(this, "local_service_stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification()
    {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "local_service_started";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.neura_sdk_notification_status_icon)  // the status icon
                .setTicker(text)  // the status text
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("DarioPal")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    @Subscribe
    public void pushArrived(BaseEvent baseEvent)
    {
        switch ( baseEvent.getEventType() )
        {
            case Const.DB_EVENT:
                Toast.makeText(this, "DB_EVENT arrived", Toast.LENGTH_SHORT).show();
                break;
            case Const.NEURA_PUSH_EVENT:
                dbManagerInterface.onSaveToDB(((NeuraPushEvent)baseEvent).neuraEvent.getEventName());
                mBusinessLogicManager.onNewNeuraEvent(((NeuraPushEvent)baseEvent).neuraEvent);
                Toast.makeText(this, "NEURA_PUSH_EVENT arrived", Toast.LENGTH_SHORT).show();
                break;
            case Const.DARIO_EVENT:
                dbManagerInterface.onSaveToDB(((DarioPushEvent)baseEvent).darioEvent);

                Toast.makeText(this, "DARIO_EVENT arrived", Toast.LENGTH_SHORT).show();
                break;
        }


    }


}


