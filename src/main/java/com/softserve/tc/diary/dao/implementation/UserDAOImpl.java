package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class UserDAOImpl extends BaseDAOImpl<User> implements UserDAO{
	public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	private static Connection conn;
	private static PreparedStatement ps;
	
	private static void getConnection(){
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void create(User object) {
		getConnection();
		try {
			ps = conn.prepareStatement("insert into user_card(nick_name, first_name, second_name, address_id, e_mail, password, sex, date_of_birth, avatar, role) values(?,?,?,?,?,?,?,?,?,?);");
			//ps.setInt(1, object.getU_id());
			ps.setString(1, object.getNick_name());
			ps.setString(2, object.getFirst_name());
			ps.setString(3, object.getSecond_name());
			ps.setInt(4, 1);  
			ps.setString(5, object.getE_mail());
			ps.setString(6, object.getPassword());
			ps.setString(7, "F");
			ps.setNull(8, 0);
			ps.setNull(9, 0);
			ps.setInt(10,2);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public List<User> getByDateOfBirth(String dateOfBirth) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
