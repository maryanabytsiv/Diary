package com.softserve.tc.diary.webservice;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import com.softserve.tc.diary.Statistics;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;

@WebService
public class DiaryServiceImpl implements DiaryService{

	@Override
	public boolean logIn(String nickName, String password) {
		return false;
	}

	@Override
	public boolean logOut(String nickName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addRecord(String nickname, Status status, String record) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRecord(String nickname, String record) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Record> getAllRecords(String nickName, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> getAllRecords(String nickName, String hashTag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statistics viewSiteStatistics(String nickNameOfAdmin) {
		// TODO Auto-generated method stub
		return null;
	}

}
