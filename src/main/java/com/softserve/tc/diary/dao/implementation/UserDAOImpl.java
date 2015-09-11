package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class UserDAOImpl implements UserDAO, BaseDAO<User>, IdGenerator {
	public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	private static Connection conn;
	private static PreparedStatement ps;

	private static void getConnection() {
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create(User object) {
		
		getConnection();
		try {
			ps = conn.prepareStatement(
					"insert into user_card values(?,?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1, getGeneratedId());
			ps.setString(2, object.getNick_name());
			ps.setString(3, object.getFirst_name());
			ps.setString(4, object.getSecond_name());
			ps.setString(5, object.getAddress());
			ps.setString(6, object.getE_mail());
			ps.setString(7, object.getPassword());
			ps.setString(8, "F");
			ps.setNull(9, 0);
			ps.setNull(10, 0);
			ps.setString(11, object.getRole());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User readByKey(int id) {
		getConnection();
		User user = null;
		try {
			ps = conn.prepareStatement("select * from user_card where uid=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new User(rs.getString(2), rs.getString(3), rs.getString(4), "Lviv", rs.getString(6),
						rs.getString(7), Sex.FEMALE, rs.getString(9), rs.getString(10), "admin");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public void update(User object) {
		// TODO Auto-generated method stub

	}

	public void delete(User object) {
		getConnection();
		try {
			ps = conn.prepareStatement("delete from user_card where u_id=?");
			ps.setString(1, object.getUuid());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

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

	public List<User> getByDateOfBirth(String dateOfBirth) {
		getConnection();
		List<User> listOfUsers = new ArrayList<User>();
		try {
			ps = conn.prepareStatement("select * from user_card where date_of_birth=?");
			ps.setString(1, dateOfBirth);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				listOfUsers.add(new User(rs.getString(2), rs.getString(3), rs.getString(4), "Lviv", rs.getString(6),
						rs.getString(7), Sex.FEMALE, rs.getString(9), rs.getString(10), "admin"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listOfUsers;
	}

	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}

	public User readByKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getByDateOfBirth(Date dateOfBirth) {
		// TODO Auto-generated method stub
		return null;
	}

}
