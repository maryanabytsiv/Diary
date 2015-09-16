package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.PasswordHelper;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.log.Log;

public class TestUserDAO {
	private Logger logger = Log.init(this.getClass().getName());

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
		SQL_Statement.setUpBeforeClass();
	}

	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		SQL_Statement.DropTableIfExists();
	}

	@Before
	public void beforeTest() throws SQLException {
		SQL_Statement.insertValue();
	}

	@After
	public void afterTest() throws SQLException {
		SQL_Statement.deleteAllFromTable();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateUserNullPointerException() {
		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(new User(null, "Andriy", "Mural", "1", null, "64561", Sex.FEMALE, "1999-03-02",
				"folder/folder/image.png", null));
	}

	@Test
	public void testCreateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(new User("hary12", "Andriy", "Mural", "1", "bg@gmail.com", "64561", Sex.FEMALE, "1995-03-02",
				"folder/folder/image.png", "2"));
		User userActual = new User();
		try {
			SQL_Statement.ps = SQL_Statement.connection
					.prepareStatement("select * from user_card where nick_name ='hary12';");
			ResultSet rs = SQL_Statement.ps.executeQuery();
			userActual = resultSet(rs);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNotNull(userActual);
		assertEquals("hary12", userActual.getNick_name());
		assertEquals("Andriy", userActual.getFirst_name());
		assertEquals("Mural", userActual.getSecond_name());
		assertEquals("bg@gmail.com", userActual.getE_mail());
		assertEquals(PasswordHelper.encrypt("64561"), userActual.getPassword());
		assertEquals("1", userActual.getAddress());
		assertEquals(PasswordHelper.encrypt("64561"), userActual.getPassword());
		assertEquals("FEMALE", userActual.getSex());
		assertEquals("1995-03-02", userActual.getDate_of_birth());
		assertEquals("folder/folder/image.png", userActual.getAvatar());
		assertEquals("2", userActual.getRole());
		logger.info("test create user");
	}

	@Test
	public void testUpdateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("read", "Natalya", "Bolyk", "1", "bg@gmail.com", "64561", Sex.FEMALE, "1999-10-10",
				"some.jpeg", "1");
		user.setUuid(UUID.randomUUID().toString());
		try {
			SQL_Statement.ps = SQL_Statement.connection
					.prepareStatement("insert into user_card values(?,?,?,?,?,?,?,?,CAST(? AS DATE),?,?);");
			SQL_Statement.ps.setString(1, user.getUuid());
			SQL_Statement.ps.setString(2, user.getNick_name());
			SQL_Statement.ps.setString(3, user.getFirst_name());
			SQL_Statement.ps.setString(4, user.getSecond_name());
			SQL_Statement.ps.setString(5, user.getAddress());
			SQL_Statement.ps.setString(6, user.getE_mail());
			SQL_Statement.ps.setString(7, user.getPassword());
			SQL_Statement.ps.setString(8, user.getSex());
			SQL_Statement.ps.setString(9, user.getDate_of_birth());
			SQL_Statement.ps.setString(10, user.getAvatar());
			SQL_Statement.ps.setString(11, user.getRole());
			SQL_Statement.ps.execute();
		} catch (SQLException e1) {
			logger.error("insert failed", e1);
		}
		user.setFirst_name("IRA");
		user.setSecond_name("BLLLLL");
		userDAO.update(user);
		User userActual = new User();
		try {
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement("select * from user_card where nick_name =?");
			SQL_Statement.ps.setString(1, user.getNick_name());
			ResultSet rs = SQL_Statement.ps.executeQuery();
			userActual = resultSet(rs);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNotNull(userActual);
		assertEquals("read", userActual.getNick_name());
		assertEquals("IRA", userActual.getFirst_name());
		assertEquals("BLLLLL", userActual.getSecond_name());
		assertEquals("bg@gmail.com", userActual.getE_mail());
		assertEquals("1", userActual.getAddress());
		assertEquals("64561", userActual.getPassword());
		assertEquals("FEMALE", userActual.getSex());
		assertEquals("1999-10-10", userActual.getDate_of_birth());
		assertEquals("some.jpeg", userActual.getAvatar());
		assertEquals("1", userActual.getRole());
		logger.info("test update user");
	}

	@Test
	public void testDeleteUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("delete", "Natalya", "Bolyk", "1", "bg@gmail.com", "64561", Sex.FEMALE, null, "jfhfff.mvn",
				"1");
		userDAO.create(user);
		userDAO.delete(user);
		assertNull(userDAO.readByKey("delete"));
		logger.info("test delete user");
	}

	@Test
	public void testGetAll() {
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("Bozo", "Oleg", "Ponkin", "2", "bsss@gmail.com", "64561", Sex.MALE, null, "jsjwe.txt",
				"1");
		userDAO.create(user);
		int actual = userDAO.getAll().size();
		int expected = 4;
		assertEquals(expected, actual);
		logger.info("test get all");
	}

	@Test
	public void testGetByNickName() {
		User userActual = new User();
		try {
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement("select * from user_card where nick_name =?");
			SQL_Statement.ps.setString(1, "TreeTree");
			ResultSet rs = SQL_Statement.ps.executeQuery();
			userActual = resultSet(rs);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNotNull(userActual);
		assertEquals("TreeTree", userActual.getNick_name());
		assertEquals("Sergey", userActual.getFirst_name());
		assertEquals("Gontar", userActual.getSecond_name());
		assertEquals("jhfcjfdf@gmail.com", userActual.getE_mail());
		assertEquals("3", userActual.getAddress());
		assertEquals("flgkjhlkftjt", userActual.getPassword());
		assertEquals("MALE", userActual.getSex());
		assertEquals("1989-02-20", userActual.getDate_of_birth());
		assertEquals(null, userActual.getAvatar());
		assertEquals("2", userActual.getRole());
		logger.info("test get by nickname");
	}

	@Test
	public void testCountAllBySex() {
		UserDAOImpl user = new UserDAOImpl();
		int count = user.countAllBySex("MALE");
		assertEquals(2, count);
	}

	@Test
	public void testGetByDateOfBirth() {
		UserDAOImpl user = new UserDAOImpl();
		List<User> list = user.getByYearOfBirth("1989");
		int actual = list.size();
		int expected = 1;
		assertEquals(expected, actual);

	}

	private User resultSet(ResultSet rs) {
		User user = new User();
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
			logger.error("result set failed", e);
		}
		return user;
	}
}