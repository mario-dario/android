package com.development.dariopal.database;

import java.sql.Time;
import java.util.List;


public interface DaoInterface {

  List<EventRecord> getAllRecords();

  List<EventRecord> getRecordsInTimeRange(Time start, Time end);

  List<EventRecord> getAllWithType (String type);

  List<EventRecord> getAllWithTypeInRange(Time start, Time end, String type);

}
