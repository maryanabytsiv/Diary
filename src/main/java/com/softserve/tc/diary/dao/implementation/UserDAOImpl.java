package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jasypt.util.password.BasicPasswordEncryptor;
import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class UserDAOImpl implements UserDAO, BaseDAO<User>, IdGenerator {
	private static Connection conn = null;
	private static PreparedStatement ps;

	public void create(User object) {
		try {
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("insert into user_card values(?,?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1, getGeneratedId());
			ps.setString(2, object.getNick_name());
			ps.setString(3, object.getFirst_name());
			ps.setString(4, object.getSecond_name());
			ps.setString(5, object.getAddress());
			ps.setString(6, object.getE_mail());
			ps.setString(7, passwordEncryptor.encryptPassword(object.getPassword()));
			ps.setString(8, object.getSex());
			ps.setNull(9, 0);
			ps.setString(10, object.getAvatar());
			ps.setString(11, object.getRole());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(User object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("update user_card set nick_name=?, first_name=?,"
					+ " second_name=?, address_id=?, e_mail=?, password=?, sex=?,"
					+ " date_of_birth=?, avatar=?,role=? where nick_name=?;");
			// ps.setString(1, getGeneratedId());
			ps.setString(1, object.getNick_name());
			ps.setString(2, object.getFirst_name());
			ps.setString(3, object.getSecond_name());
			ps.setString(4, object.getAddress());
			ps.setString(5, object.getE_mail());
			ps.setString(6, object.getPassword());
			ps.setString(7, object.getSex());
			ps.setNull(8, 0);
			ps.setString(9, object.getAvatar());
			ps.setString(10, object.getRole());
			ps.setString(11, object.getNick_name());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void delete(User object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("delete from user_card where nick_name=?");
			ps.setString(1, object.getNick_name());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<User> getAll() {
		List<User> list = new ArrayList<User>();
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("select * from user_card");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new User(rs.getString("nick_name"), rs.getString("first_name"), rs.getString("second_name"),
						rs.getString("address_id"), rs.getString("e_mail"), rs.getString("password"),
						Sex.valueOf(rs.getString("Sex")), null, rs.getString("avatar"), rs.getString("role")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public User getByNickName(String nickName) {
		User user = new User();
		try {
			ps = conn.prepareStatement("select * from user_card where nick_name =?;");
			ps.setString(1, user.getNick_name());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user.setNick_name(rs.getString("nick_name"));
				user.setFirst_name(rs.getString("first_name"));
				user.setSecond_name(rs.getString("second_name"));
				user.setAddress(rs.getString("address_id"));
				user.setE_mail(rs.getString("e_mail"));
				user.setPassword(rs.getString("password"));
				user.setSex(rs.getString("Sex"));
				user.setDate_of_birth(null);
				user.setAvatar(rs.getString("avatar"));
				user.setRole(rs.getString("role"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public int countAllBySex(Sex sex) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}

	public User readByKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	// public User readByKey(int id) {
	// User user = null;
	// try {
	// conn = ConnectManager.getConnectionToTestDB();
	// ps = conn.prepareStatement("select * from user_card where uid=?");
	// ps.setInt(1, id);
	// ResultSet rs = ps.executeQuery();
	// while (rs.next()) {
	// user = new User(rs.getString(2), rs.getString(3), rs.getString(4),
	// "Lviv", rs.getString(6),
	// rs.getString(7), Sex.FEMALE, rs.getString(9), rs.getString(10), "admin");
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return user;
	// }

	public List<User> getByDateOfBirth(Date dateOfBirth) {
		// TODO Auto-generated method stub
		return null;
	}

	// public List<User> getByDateOfBirth(String dateOfBirth) {
	// List<User> listOfUsers = new ArrayList<User>();
	// try {
	// conn = ConnectManager.getConnectionToTestDB();
	// ps = conn.prepareStatement("select * from user_card where
	// date_of_birth=?");
	// ps.setString(1, dateOfBirth);
	// ResultSet rs = ps.executeQuery();
	// while (rs.next()) {
	// listOfUsers.add(new User(rs.getString(2), rs.getString(3),
	// rs.getString(4), "Lviv", rs.getString(6),
	// rs.getString(7), Sex.FEMALE, rs.getString(9), rs.getString(10),
	// "admin"));
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return listOfUsers;
	// }

}
