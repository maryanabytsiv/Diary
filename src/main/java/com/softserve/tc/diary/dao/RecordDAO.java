package com.softserve.tc.diary.dao;

import java.util.List;

import com.softserve.tc.diary.entity.Record;

public interface RecordDAO extends BaseDAO<Record> {
    List<Record> getRecordByUserId(String user_name);
            
    List<Record> getRecordByDate(String date);
    
    List<Record> getRecordByVisibility(String visibility);
    
    List<Record> getRecordTypeOfSupplement(String typeOfSupplement);
    
}
