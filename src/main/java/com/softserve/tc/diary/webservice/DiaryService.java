package com.softserve.tc.diary.webservice;

import java.util.List;

import javax.jws.WebService;

import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.User;

@WebService
public interface DiaryService {

	String sayHello(String name);

	String logIn(String nickName, String password);

	boolean logOut(String nickName);

	boolean addRecord(String nickname, String title, String text, String status);

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

	void updateUser(User user);

	void deleteUser(User user);
	
	Tag getMostPopularTag();
	
	User getMostActiveUser();
	
	int[] getSexStatistic();

    void createUser(User user);

	// Statistics viewSiteStatistics(String nickNameOfAdmin);

}
