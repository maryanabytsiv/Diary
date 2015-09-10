package com.softserve.tc.diary.dao;

import java.util.List;
import java.util.Map;

import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public interface UserDAO extends BaseDAO<User>{

	//Teodor
	//by nick_name, all female, all male (count)
	//from what city there is much users
	
	User getByNickName(String nickName);
	int countAllBySex(Sex sex);
	List<String> getCityByMaxNumberOfUsers();
	
	
	
	//Maryana
	//by date of birth
	//AVG(date of birth)
	//get emails 
	List<User> getByDateOfBirth(String dateOfBirth);
	int getAvarageAgeOfUsers();
	Map<String, String> getAllEmailsByNickNames(List<String> nickNames);
}