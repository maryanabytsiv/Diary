package com.softserve.tc.diary.dao;

import java.sql.Timestamp;
import java.util.List;

import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;

public interface RecordDAO extends BaseDAO<Record> {
    
    List<Record> getRecordByUserId(String user_name);
    
    List<Record> getRecordByDate(Timestamp date);
    
    List<Record> getRecordByNickNameAndDate(String userID, Timestamp date);
    
    int getUserAmountOfRecord(String user_id);
    
    List<Record> getRecordByVisibility(String user_id, Status visibility);
    
    List<Record> getRecordTypeOfSupplement(String typeOfSupplement);
    
}
