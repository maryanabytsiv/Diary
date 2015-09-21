package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.connectmanager.TestDBConnection;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.dao.util.PasswordHelper;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.log.Log;

public class UserDAOImpl implements UserDAO, BaseDAO<User> {
	// private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs;
	private static Logger logger = Log.init("UserDAOImpl");

	public void create(User object) {
		String[] splitAddress = object.getAddress().split(", ");
		Address newAdress = new Address(splitAddress[0], splitAddress[1], splitAddress[2], splitAddress[3]);
		AddressDAOImpl adressDAO = new AddressDAOImpl();
		adressDAO.create(newAdress);

		Address getAddress = null;
		try (Connection conn = TestDBConnection.getConnection()) {
			ps = conn.prepareStatement(
					"SELECT * FROM address WHERE country = ? AND city = ? AND street = ? AND build_number = ?;");
			ps.setString(1, newAdress.getCountry());
			ps.setString(2, newAdress.getCity());
			ps.setString(3, newAdress.getStreet());
			ps.setString(4, newAdress.getBuild_number());
			rs = ps.executeQuery();
			if (rs.next()) {
				getAddress = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				getAddress.setId(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.error("create address failed", e);
		}

		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				if ((object.getRole() == null) || (object.getE_mail() == null) || (object.getNick_name() == null)) {
					logger.error("You not enter nickname, e-mail or role");
					throw new IllegalArgumentException();
				} else {
					logger.debug("Creating user");
					ps = conn.prepareStatement("insert into user_card values(?,?,?,?,?,?,?,?,CAST(? AS DATE),?,?,?);");
					ps.setString(1, UUID.randomUUID().toString());
					ps.setString(2, object.getNick_name());
					ps.setString(3, object.getFirst_name());
					ps.setString(4, object.getSecond_name());
					ps.setString(5, getAddress.getId());
					ps.setString(6, object.getE_mail());
					ps.setString(7, PasswordHelper.encrypt(object.getPassword()));
					ps.setString(8, object.getSex().toUpperCase());
					ps.setString(9, object.getDate_of_birth());
					ps.setString(10, object.getAvatar());
					ps.setString(11, object.getRole().toUpperCase());
					ps.setString(12, null);
					ps.execute();
					ps.close();
					conn.commit();
				}
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("create user failed", e);
		}
	}

	public void update(User object) {
		String[] splitAddress = object.getAddress().split(", ");
		Address newAdress = new Address(splitAddress[0], splitAddress[1], splitAddress[2], splitAddress[3]);
		AddressDAOImpl adressDAO = new AddressDAOImpl();

		User userToUpdate = readByNickName(object.getNick_name());
		String addressUUID = "";
		try (Connection conn = TestDBConnection.getConnection()) {
			ps = conn.prepareStatement("select address_id from user_card where nick_name=?;");
			ps.setString(1, userToUpdate.getNick_name());
			ps.execute();
			rs = ps.executeQuery();
			if (rs.next()) {
				addressUUID = rs.getString(1);
			}

		} catch (SQLException e) {
			logger.error("create address failed", e);
		}

		newAdress.setId(addressUUID);
		adressDAO.update(newAdress);

		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(
						"update user_card set first_name=?," + " second_name=?, e_mail=?, password=?, sex=?,"
								+ " date_of_birth=CAST(? AS DATE), avatar=?,role=?, session = ? where nick_name=?;");
				ps.setString(1, object.getFirst_name());
				ps.setString(2, object.getSecond_name());
				ps.setString(3, object.getE_mail());
				ps.setString(4, object.getPassword());
				ps.setString(5, object.getSex().toUpperCase());
				ps.setString(6, object.getDate_of_birth());
				ps.setString(7, object.getAvatar());
				ps.setString(8, object.getRole().toUpperCase());
				ps.setString(9, object.getSession());
				ps.setString(10, object.getNick_name());
				ps.execute();
				conn.commit();
				logger.debug("User updated");
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("Update user failed", e);
		}

	}

	public void delete(User object) {
		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				logger.debug("Deleting user");
				ps = conn.prepareStatement("delete from user_card where nick_name=?");
				ps.setString(1, object.getNick_name());
				ps.execute();
				conn.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("delete user failed", e);
		}

	}

	public List<User> getAll() {
		List<User> list = new ArrayList<User>();
		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id);");
				rs = ps.executeQuery();
				conn.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
			String address = "";
			while (rs.next()) {
				address = rs.getString("country") + ", " + rs.getString("city") + ", " + rs.getString("street") + ", "
						+ rs.getString("build_number");
				list.add(new User(rs.getString("nick_name"), rs.getString("first_name"), rs.getString("second_name"),
						address, rs.getString("e_mail"), rs.getString("password"), Sex.valueOf(rs.getString("Sex")),
						rs.getString("date_of_birth"), rs.getString("avatar"), Role.valueOf(rs.getString("role"))));
			}
		} catch (SQLException e) {
			logger.error("select all failed", e);
		}
		return list;
	}

	public User readByKey(String uuid) {
		String query = "select * from user_card left join address on (address.id=user_card.address_id)"
				+ " where uid like '" + uuid + "';";
		User user = null;
		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(query);
				rs = ps.executeQuery();
				user = resultSet(rs);
				conn.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("read by key failed", e);
		}
		return user;
	}

	public User readByNickName(String nickName) { // id as NICK_NAME
		User user = null;
		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
				ps.setString(1, nickName);
				rs = ps.executeQuery();
				user = resultSet(rs);
				conn.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("read by key failed", e);
		}
		return user;
	}

	public int countAllBySex(String sex) {
		int users = 0;
		try (Connection conn = TestDBConnection.getConnection()) {
			ps = conn.prepareStatement("select COUNT(*) from user_card where sex =? group by sex;");
			ps.setString(1, sex.toUpperCase());
			rs = ps.executeQuery();
			while (rs.next()) {
				users = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Sorry, I can't count all by sex ;(", e);
		}
		return users;
	}

	public List<User> getByYearOfBirth(String yearOfBirth) {
		List<User> usersByYear = new ArrayList<User>();
		try (Connection conn = TestDBConnection.getConnection()) {
			ps = conn.prepareStatement(
					"select * from user_card where extract (year from date_of_birth)=CAST(? AS double precision); ");
			ps.setString(1, yearOfBirth);
			rs = ps.executeQuery();
			while (rs.next()) {
				usersByYear.add(resultSet(rs));
			}
		} catch (SQLException e) {
			logger.error("get by birthday failed", e);
		}
		return usersByYear;
	}

	public List<User> getUsersByRole(Role role) {
		List<User> list = new ArrayList<User>();
		String roleStr = role.toString();
		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id)"
								+ "where role like '" + roleStr + "'");
				rs = ps.executeQuery();
				conn.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
			String address = "";
			while (rs.next()) {
				address = rs.getString("country") + ", " + rs.getString("city") + ", " + rs.getString("street") + ", "
						+ rs.getString("build_number");
				list.add(new User(rs.getString("nick_name"), rs.getString("first_name"), rs.getString("second_name"),
						address, rs.getString("e_mail"), rs.getString("password"), Sex.valueOf(rs.getString("Sex")),
						rs.getString("date_of_birth"), rs.getString("avatar"), Role.valueOf(rs.getString("role"))));
			}
		} catch (SQLException e) {
			logger.error("select all failed by role", e);
		}
		return list;
	}

	private User resultSet(ResultSet rs) {
		User user = null;

		try {
			while (rs.next()) {
				user = new User();
				user.setUuid(rs.getString("uid"));
				user.setNick_name(rs.getString("nick_name"));
				user.setFirst_name(rs.getString("first_name"));
				user.setSecond_name(rs.getString("second_name"));
				user.setAddress(rs.getString("country") + ", " + rs.getString("city") + ", " + rs.getString("street")
						+ ", " + rs.getString("build_number"));
				user.setE_mail(rs.getString("e_mail"));
				user.setPassword(rs.getString("password"));
				user.setSex(rs.getString("Sex"));
				user.setDate_of_birth(rs.getString("date_of_birth"));
				user.setAvatar(rs.getString("avatar"));
				user.setRole(rs.getString("role"));
				user.setSession(rs.getString("session"));
			}
		} catch (SQLException e) {
			logger.error("ResultSet failed", e);
		}
		return user;
	}

	@Override
	public void updateSessionByNickName(String nickName, String session) {

		try (Connection conn = TestDBConnection.getConnection()) {
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement("update user_card set session=? where nick_name=?;");
				ps.setString(1, session);
				ps.setString(2, nickName);
				ps.execute();
				conn.commit();
				logger.debug("User session updated");
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				conn.rollback();
				conn.setAutoCommit(true);
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("Update of user session failed", e);
		}

	}

	public User getUserByNickAndPassword(String nickName, String password) {
		User user = null;
		String passEncript = PasswordHelper.encrypt(password);
		try (Connection conn = TestDBConnection.getConnection()) {
			ps = conn.prepareStatement("select  * from user_card left join address on (address.id=user_card.address_id)"
					+ " where nick_name =?  and password =?;");
			ps.setString(1, nickName);
			ps.setString(2, passEncript);
			ResultSet rs = ps.executeQuery();
			user = resultSet(rs);
		} catch (SQLException e) {
			logger.error("failed" + e);
		}
		return user;
	}

	@Override
	public String logIn(String nickName, String password) {
		User user = getUserByNickAndPassword(nickName, password);
		if ( user == null) {
			return null; //if null -  password or nickName in argument not correct
		} else {
			String session = UUID.randomUUID().toString();
			user.setSession(session);
			update(user);
			return session;
		}
	}

	@Override
	public boolean logOut(String nickName) {
		User user = readByNickName(nickName);
		if (user == null) {
			return false; //log out failed
		} else {
			user.setSession(null);
			update(user);
			return true; //log out done
		}

	}
}
