package com.softserve.tc.diary.webservice;

import java.util.List;

import javax.jws.WebService;

import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.User;

@WebService
public interface DiaryService {

	String sayHello(String name);

	String logIn(String nickName, String password);

	boolean logOut(String nickName);

	boolean addRecord(String nickname, Status status, String record);

	boolean removeRecord(String nickname, String recordId);

	List<Record> getAllRecordsByDate(String nickName, String date);

	List<Record> getAllRecordsByHashTag(String nickName, String hashTag);

	User getUserByNickName(String nickName);

	List<User> getAllUsers();

	// Statistics viewSiteStatistics(String nickNameOfAdmin);

}
