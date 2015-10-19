package com.softserve.tc.diary.webservice;

import java.util.List;

import javax.jws.WebService;

import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.User;

@WebService
public interface DiaryService {
    
    String sayHello(String name);
    
    String logIn(String nickName, String password);
    
    boolean logOut(String nickName);
    
    Record addRecord(String nickname, String title, String text, String status,
            String fileName);
            
    boolean removeRecord(String nickname, String recordId);
    
    List<Record> getAllRecordsByDate(String nickName, String date);
    
    List<Record> getAllRecordsByHashTag(String nickName, String hashTag);
    
    String getRoleByNickName(String nickName);
    
    User getUserByNickName(String nickName);
    
    List<User> getAllUsers();
    
    List<Record> getAllPublicRecords();
    
    List<Record> getAllPublicRecordsByHashTag(String hashTag);
    
    Record readByKey(String id);
    
    int getUserAmountOfRecords(String nickName);
    
    void updateUserWithoutImage(User user);
    
    void updateUser(User user, byte[] file, String fileName);
    
    void deleteUser(User user);
    
    Tag getMostPopularTag();
    
    User getMostActiveUser();
    
    int[] getSexStatistic();
    
    void createUser(User user);
    
    String updateSession(String nickName);
    
    List<String> getDatesWithRecordsPerMonth(String nickName, String date);
    
    List<Tag> getListTagsByPrefix(String prefix);
    
    User getUserByKey(String userId);
    
    List<User> getActiveUsers();
}
