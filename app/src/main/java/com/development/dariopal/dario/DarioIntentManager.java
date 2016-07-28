package com.labstyle.darioandroid.dariosharedclasses;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Created by mario on 27/07/16.
 */
public class DarioIntentManager
{
    private static final String DarioDataReceivedAction = "com.labstyle.darioandroid.newdata";
    //private static final String DarioDataReceivedKey = "darioLogEntryData";
    private static final String DarioDataReceivedBundleName = "darioLogEntryDataBundle";
    private static String DarioDataReceivedKeySerializable = "DarioDataReceivedKeySerializable";

    public static IntentFilter newIntentFilter()
    {
        IntentFilter filter = new IntentFilter(DarioDataReceivedAction);
        return filter;
    }


    public static ExportDarioLogEntryDataSerializable getExportDarioLogEntryDataSerializable(Intent intent)
    {
        Bundle extras = intent.getBundleExtra(DarioDataReceivedBundleName);
        ExportDarioLogEntryDataSerializable darioLogEntryData=null;

        if (extras != null)
            darioLogEntryData = (ExportDarioLogEntryDataSerializable)extras.getSerializable(DarioDataReceivedKeySerializable);

        return darioLogEntryData;
    }

    public static Intent newIntentWithLogEntryData(ExportDarioLogEntryDataSerializable darioLogEntryData)
    {
        Intent intent= new Intent(DarioDataReceivedAction);
        Bundle extras=new Bundle();
        extras.putSerializable(DarioDataReceivedKeySerializable,darioLogEntryData);
        intent.putExtra(DarioDataReceivedBundleName,extras);
        return intent;
    }





}
