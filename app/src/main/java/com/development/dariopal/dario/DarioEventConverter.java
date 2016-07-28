package com.labstyle.darioandroid.hackathon;

import com.labstyle.darioandroid.dariosharedclasses.ExportDarioLogEntryDataSerializable;
import com.labstyle.darioandroid.database.event.DarioTransactionEventEntity;

/**
 * Created by mario on 27/07/16.
 */

final class DarioEventConverter
{

    public static ExportDarioLogEntryDataSerializable darioTransactionToDarioExportedLogEntry(DarioTransactionEventEntity entity)
    {
        if(entity==null)
            throw new NullPointerException("DarioEventGlucoseMeasurement has mandatory fields and can't be null");

        Float insulinUnits=null;
        String measurementState=null;

        Long timeOfEvent=entity.getGlucoseMeasurementEvent().getDts();

        if(null!=entity.getGlucoseMeasurementEvent().getStatus())
            measurementState=entity.getGlucoseMeasurementEvent().getStatus().toString();

        if(null!=entity.getInsulinIntakeEventList() && entity.getInsulinIntakeEventList().size()>0)
            insulinUnits= entity.getInsulinIntakeEventList().get(0).getInsulineUnits();


        return new ExportDarioLogEntryDataSerializable(insulinUnits,measurementState,timeOfEvent);

    }
}
