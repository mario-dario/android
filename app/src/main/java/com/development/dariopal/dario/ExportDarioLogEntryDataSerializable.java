package com.development.dariopal.dario;

import java.io.Serializable;

/**
 * Created by mario on 29/07/16.
 */

public class ExportDarioLogEntryDataSerializable implements Serializable
{

    private final Float insulinUnits;
    private final String measurementState;
    private final Long timeOfEvent;

    public ExportDarioLogEntryDataSerializable(Float insulinUnits, String measurementState, Long timeOfEvent)
    {
        this.insulinUnits = insulinUnits;
        this.measurementState = measurementState;
        this.timeOfEvent = timeOfEvent;
    }

    public Float getInsulinUnits()
    {
        return insulinUnits;
    }

    public String getMeasurementState()
    {
        return measurementState;
    }

    public Long getTimeOfEvent()
    {
        return timeOfEvent;
    }
}
