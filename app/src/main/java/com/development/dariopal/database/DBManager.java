package com.development.dariopal.database;


/*
    Adapter class to connect Dario data object with our PalDao database

 */

import android.content.Context;

import com.labstyle.darioandroid.dariosharedclasses.ExportDarioLogEntryDataSerializable;

import java.util.List;

public class DBManager implements DBManagerInterface {
  private Context context;

  public DBManager(Context context) {
    this.context = context;
  }


  private void saveToDb(ExportDarioLogEntryDataSerializable darioDataObj) {

    Long timeStamp = darioDataObj.getTimeOfEvent();

    if (null != darioDataObj.getMeasurementState()) {
      EventRecord newRecord = new EventRecord("Type_Measurement_State", darioDataObj.getMeasurementState(), timeStamp);
      newRecord.save();
    }

    if (null != darioDataObj.getInsulinUnits()) {
      EventRecord newRecord = new EventRecord("Type_Insulin_State", String.valueOf(darioDataObj.getInsulinUnits()), timeStamp);
      newRecord.save();
    }

  }

  @Override
  public void onSaveToDB(ExportDarioLogEntryDataSerializable data) {
    saveToDb(data);
  }

  @Override
  public void onSaveToDB(String data) {

  }

  @Override
  public List<EventRecord> onGetFromDB(Long startTime, Long endTime) {
    List<EventRecord> resultRecords = EventRecord.find(EventRecord.class,
        "timeStamp <= ? and timeStamp >= ?", endTime.toString(), startTime.toString());
    return resultRecords;
  }

}
