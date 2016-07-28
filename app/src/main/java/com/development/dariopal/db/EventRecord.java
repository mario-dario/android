package com.development.dariopal.db;


import com.orm.SugarRecord;

import java.sql.Time;

public class EventRecord extends SugarRecord{
  String type; // type of activity
  float value; // value of measurement etc
  String meta; // for later
  Time timeStamp;

  public EventRecord() {
    this.type = "none";
  }

  public EventRecord(String type, float value, String meta, Time timeStamp) {
    this.type = type;
    this.value = value;
    this.meta = meta;
    this.timeStamp = timeStamp;
  }


  public EventRecord(String type, float value, Time timeStamp) {
    this.type = type;
    this.value = value;
    this.timeStamp = timeStamp;
  }
}
