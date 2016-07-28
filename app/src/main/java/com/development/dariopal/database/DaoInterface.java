package com.development.dariopal.database;

import java.sql.Time;
import java.util.List;


public interface DaoInterface {

  List<EventRecord> getAllRecords();

  List<EventRecord> getRecordsInTimeRange(Long tStart, Long tEnd);

  List<EventRecord> getAllWithType (String type);

  List<EventRecord> getAllWithTypeInRange(Long tStart, Long tEnd, String type);

}
