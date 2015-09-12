package com.softserve.tc.diary.dao;

import java.sql.Date;
import java.util.List;

import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public interface UserDAO extends BaseDAO<User> {

	int countAllBySex(Sex sex);

	List<User> getByDateOfBirth(Date dateOfBirth);

}