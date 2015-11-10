package com.softserve.tc.diary.webservice;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import javax.jws.WebService;

import com.softserve.tc.diary.entity.Follower;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.User;

@WebService
public interface DiaryService {
    
    String sayHello(String name);
    
    String logIn(String nickName, String password);
    
    boolean logOut(String nickName);
    
	Record addRecord(Record record, byte[] file);
	
	Record updateRecord (Record record, byte[] file);
	  
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
    
    String updateSession(String nickName, String session);
    
    void invalidateSession(String nickName, String session);
    
    List<String> getDatesWithRecordsPerMonth(String nickName, String date);
    
    List<Tag> getListTagsByPrefix(String prefix);
    
    User getUserByKey(String userId);
    
    Set<String> getAllHashes();
    
    List<User> getActiveUsers();
    
    User getUserByEmail(String email);
    
    String[][] getRecDate();
	
	String attachFollower(Follower follower);
    
    void detachFollower (Follower follower);
    
    List<User> getAllUserFollowers(String userUuid);
    
    List<User> getAllFollowedUsers(String followerUuid);
    
    void markUserWithNewRecord(String userUuid);
    
    void markAsViwed(String followerUuid);
    
    List<User> getAllReviewedUsers (String followerUuid);
    
    List<User> getAllNotReviewedUsers (String followerUuid);
    
    boolean isThereAvalableNewRecords (String followerUuid);
    
    List<Record> getAllPublicRecordsByNickName(String nickName);
    
    String getDataForGeoChactGraphic(String country);
    
    void updateUserPassword(User user, String password);
    
    String getHashOfPassword(String password);
    
}
