package com.softserve.tc.diary.webservice;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.softserve.tc.diary.Statistics;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;

@WebService
public interface DiaryService {
	
	 boolean logIn(String nickName, String password);
    
//    boolean logIn(String nickName, String password);
//    
//    boolean logOut(String nickName);
//    
//    boolean addRecord(String nickname, Status status, String record);
//    
//    boolean removeRecord(String nickname, String record);
//    
//    List<Record> getAllRecords(String nickName, Date date);
//    
//    List<Record> getAllRecords(String nickName, String hashTag);
//    
//    Statistics viewSiteStatistics(String nickNameOfAdmin);
    
}
