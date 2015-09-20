package com.softserve.tc.diary.webservice;

import javax.jws.WebService;

import com.softserve.tc.diary.entity.Status;

@WebService
public interface DiaryService {
    
    String logIn(String nickName, String password);
    
    boolean logOut(String nickName);
    
    boolean addRecord(String nickname, Status status, String record);
    
    boolean removeRecord(String nickname, String record);
    
    // List<Record> getAllRecords(String nickName, Date date);
    //
    // List<Record> getAllRecords(String nickName, String hashTag);
    //
    // Statistics viewSiteStatistics(String nickNameOfAdmin);
    
}
