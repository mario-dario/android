package com.development.dariopal.database;


import java.sql.Time;
import java.util.List;

public class DarioPalDaoApi implements DaoInterface {

  @Override
  public List<EventRecord> getAllRecords() {
    List<EventRecord> resultRecords = EventRecord.listAll(EventRecord.class);
    return resultRecords;
  }

  @Override
  public List<EventRecord> getRecordsInTimeRange(Long tStart, Long tEnd) {
      List<EventRecord> resultRecords = EventRecord.find(EventRecord.class,
          "timeStamp <= ? and timeStamp >= ?", tEnd.toString(), tStart.toString());
    return resultRecords;
  }

  @Override
  public List<EventRecord> getAllWithType(String type) {
    List<EventRecord> resultRecords = EventRecord.find(EventRecord.class, "type = ?" , type);
    return resultRecords;

  }

  @Override
  public List<EventRecord> getAllWithTypeInRange(Long tStart, Long tEnd, String type) {
    List<EventRecord> resultRecords = EventRecord.find(EventRecord.class,
        "timeStamp <= ? and timeStamp >= ? and type = ?", tEnd.toString(), tStart.toString(), type);
    return resultRecords;
  }


}
