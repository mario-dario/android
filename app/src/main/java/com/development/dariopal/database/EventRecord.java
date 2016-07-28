package com.development.dariopal.database;


import com.orm.SugarRecord;

import java.sql.Time;

public class EventRecord extends SugarRecord{
  String type; // type of activity
  String value; // value of measurement etc
  String meta; // for later
//  Time timeStamp;
  Long timeStamp;

  public EventRecord() {
    this.type = "none";
  }



  public EventRecord(String type, String value, String meta, Long timeStamp) {
    this.type = type;
    this.value = value;
    this.meta = meta;
    this.timeStamp = timeStamp;
  }


  public EventRecord(String type, String value, Long timeStamp) {
    this.type = type;
    this.value = value;
    this.timeStamp = timeStamp;
  }


  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public String getMeta() {
    return meta;
  }

  public Long getTimeStamp() {
    return timeStamp;
  }
}
