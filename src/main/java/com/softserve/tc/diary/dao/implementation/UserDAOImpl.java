package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
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
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class UserDAOImpl implements UserDAO, BaseDAO<User>, IdGenerator {
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs;

	public static void close() {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(User object) {
		try {
			if ((object.getRole() == null) || (object.getE_mail() == null) || (object.getNick_name() == null)) {
				System.err.println("!!!!!!!!You not enter nickname, e-mail or role!!!!!!!");
				throw new NullPointerException();
			} else {
				conn = ConnectManager.getConnectionToTestDB();
				ps = conn.prepareStatement("insert into user_card values(?,?,?,?,?,?,?,?,CAST(? AS DATE),?,?);");
				ps.setString(1, getGeneratedId());
				ps.setString(2, object.getNick_name());
				ps.setString(3, object.getFirst_name());
				ps.setString(4, object.getSecond_name());
				ps.setString(5, object.getAddress());
				ps.setString(6, object.getE_mail());
				ps.setString(7, PasswordHelper.encrypt(object.getPassword()));
				ps.setString(8, object.getSex().toUpperCase());
				ps.setString(9, object.getDate_of_birth());
				ps.setString(10, object.getAvatar());
				ps.setString(11, object.getRole());
				ps.execute();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(User object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("update user_card set nick_name=?, first_name=?,"
					+ " second_name=?, address_id=?, e_mail=?, password=?, sex=?,"
					+ " date_of_birth=CAST(? AS DATE), avatar=?,role=? where nick_name=?;");
			ps.setString(1, object.getNick_name());
			ps.setString(2, object.getFirst_name());
			ps.setString(3, object.getSecond_name());
			ps.setString(4, object.getAddress());
			ps.setString(5, object.getE_mail());
			ps.setString(6, object.getPassword());
			ps.setString(7, object.getSex().toUpperCase());
			ps.setString(8, object.getDate_of_birth());
			ps.setString(9, object.getAvatar());
			ps.setString(10, object.getRole());
			ps.setString(11, object.getNick_name());
			ps.execute();
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
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new User(rs.getString("nick_name"), rs.getString("first_name"), rs.getString("second_name"),
						rs.getString("address_id"), rs.getString("e_mail"), rs.getString("password"),
						Sex.valueOf(rs.getString("Sex")), rs.getString("date_of_birth"), rs.getString("avatar"),
						rs.getString("role")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public User readByKey(String id) { // id as NICK_NAME
		User user = new User();
		try {
			ps = conn.prepareStatement("select * from user_card where nick_name =?;");
			ps.setString(1, user.getNick_name());
			rs = ps.executeQuery();
			if (rs.next()) {
				user=resultSet(rs);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int countAllBySex(String sex) {
		int users = 0;
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("select * from user_card where sex =?;");
			ps.setString(1, sex.toUpperCase());
			rs = ps.executeQuery();
			while (rs.next()) {
				users++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return users;
	}

	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}

	public List<User> getByYearOfBirth(String yearOfBirth) {
		List<User> usersByYear = new ArrayList<User>();
		String rangeFrom = yearOfBirth + "-01-01";
		String rangeTo = yearOfBirth + "-12-31";
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement(
					"select * from user_card where date_of_birth>CAST(? AS DATE) AND date_of_birth<CAST(? AS DATE); ");
			ps.setString(1, rangeFrom);
			ps.setString(2, rangeTo);
			rs = ps.executeQuery();
			while (rs.next()) {
				usersByYear.add(resultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usersByYear;
	}
	
	private User resultSet(ResultSet rs){
		User user=new User();
		try {
			while (rs.next()) {
				user.setNick_name(rs.getString("nick_name"));
				user.setFirst_name(rs.getString("first_name"));
				user.setSecond_name(rs.getString("second_name"));
				user.setAddress(rs.getString("address_id"));
				user.setE_mail(rs.getString("e_mail"));
				user.setPassword(rs.getString("password"));
				user.setSex(rs.getString("Sex"));
				user.setDate_of_birth(rs.getString("date_of_birth"));
				user.setAvatar(rs.getString("avatar"));
				user.setRole(rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
}
