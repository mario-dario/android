package com.development.dariopal.dario;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



/**
 * Created by mario on 27/07/16.
 */

final public class DarioDataReceiver
{
    private static DarioDataReceiver instance;
    private static IDarioDataHandler mOneHandlerOnly;
    private static boolean receiverInOn;

    Context mContext;

    public static DarioDataReceiver getDarioDataReceiver()
    {
        if (instance == null)
            synchronized (DarioDataReceiver.class)
            {
                if (instance == null)
                  instance = new DarioDataReceiver();
            }
        return instance;
    }

    private DarioDataReceiver()
    {

    }


    public void start(Context context, IDarioDataHandler handler)
    {
        if (handler == null || context == null)
            throw new IllegalArgumentException("");

        mOneHandlerOnly = handler;

        synchronized (this)
        {
            if (receiverInOn)
                throw new IllegalStateException("already started");
            receiverInOn = true;
            mContext = context;
            mContext.registerReceiver(darioDataBroadcastReceiver, DarioIntentManager.newIntentFilter());
        }


    }

    ;

    public void stop()
    {
        synchronized (this)
        {
            if(!receiverInOn)
                return;

            receiverInOn =false;
            if(mContext!=null)
                mContext.unregisterReceiver(darioDataBroadcastReceiver);
        }

        mOneHandlerOnly = null;
        mContext =null;
    }

    private BroadcastReceiver darioDataBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            synchronized (this)
            {
                if(!receiverInOn || mOneHandlerOnly==null)
                    return;
            }

            mOneHandlerOnly.onSuccess(DarioIntentManager.getExportDarioLogEntryDataSerializable(intent));

        }
    };

}
