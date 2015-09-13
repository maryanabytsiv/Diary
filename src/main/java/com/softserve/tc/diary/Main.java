package com.softserve.tc.diary;

import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class Main {

	public static void main(String[] args) {
		UserDAOImpl user=new UserDAOImpl();
		User newUser=new User("Nikko", "Thomas", "Shelby", "1", "mtungieugn@gmail", "sefhwufh", Sex.FEMALE, "1991-12-12", "frwnfef", "1");
		user.create(newUser);

	}

}
