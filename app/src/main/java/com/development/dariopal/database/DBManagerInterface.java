package com.development.dariopal.database;


import com.labstyle.darioandroid.dariosharedclasses.ExportDarioLogEntryDataSerializable;

import java.util.List;

public interface DBManagerInterface {
  void onSaveToDB(ExportDarioLogEntryDataSerializable data);
  void onSaveToDB(String data);
  List<EventRecord> onGetFromDB(Long startTime, Long endTime);
}
