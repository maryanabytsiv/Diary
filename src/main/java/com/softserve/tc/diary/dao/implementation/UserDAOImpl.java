package com.softserve.tc.diary.dao.implementation;

import java.util.List;
import java.util.Map;

import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class UserDAOImpl extends BaseDAOImpl<User> implements UserDAO{

	public void create(User object) {

	}

	public User readByKey(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(User object) {
		// TODO Auto-generated method stub
		
	}

	public void delete(User object) {
		// TODO Auto-generated method stub
		
	}

	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public User getByNickName(String nickName) {
		// TODO Auto-generated method stub
		return null;
	}

	public int countAllBySex(Sex sex) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<String> getCityByMaxNumberOfUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getByDAteOfBirth(String dateOfBirth) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getAvarageAgeOfUsers() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Map<String, String> getAllEmailsByNickNames(List<String> nickNames) {
		// TODO Auto-generated method stub
		return null;
	}

}
