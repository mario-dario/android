package com.development.dariopal.db;


import java.sql.Time;
import java.util.List;

public class DarioPalDaoApi implements DaoInterface {

  @Override
  public List<EventRecord> getAllRecords() {
    List<EventRecord> resultRecords = EventRecord.listAll(EventRecord.class);
    return resultRecords;
  }

  @Override
  public List<EventRecord> getRecordsInTimeRange(Time start, Time end) {
      List<EventRecord> resultRecords = EventRecord.find(EventRecord.class,
          "timeStamp <= ? and timeStamp >= ?", end.toString(), start.toString());
    return resultRecords;
  }

  @Override
  public List<EventRecord> getAllWithType(String type) {
    List<EventRecord> resultRecords = EventRecord.find(EventRecord.class, "type = ?" , type);
    return resultRecords;

  }

  @Override
  public List<EventRecord> getAllWithTypeInRange(Time start, Time end, String type) {
    List<EventRecord> resultRecords = EventRecord.find(EventRecord.class,
        "timeStamp <= ? and timeStamp >= ? and type = ?", end.toString(), start.toString(), type);
    return resultRecords;
  }


}
