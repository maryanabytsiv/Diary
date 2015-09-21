package com.softserve.tc.diary;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.connectmanager.TestDBConnection;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.dao.util.PasswordHelper;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.log.Log;

public class TestUserDAO {
	private Logger logger = Log.init(this.getClass().getName());
	private PreparedStatement ps = null;

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
		DBCreationManager.setUpBeforeClass();
	}

	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		DBCreationManager.DropTableIfExists();
	}

	@Before
	public void beforeTest() throws SQLException {
		DBCreationManager.insertValue();
	}

	@After
	public void afterTest() throws SQLException {
		DBCreationManager.deleteAllFromTable();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateUserNullPointerException() {
		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(new User(null, "Andriy", "Mural", "Ukraine, Lviv, Pasichna, 52", null, "64561", Sex.FEMALE,
				"1999-03-02", "folder/folder/image.png", null));
	}

	@Test
	public void testCreateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(new User("hary12", "Andriy", "Mural", "Ukraine, Lviv, Pasichna, 52", "bg@gmail.com", "64561",
				Sex.FEMALE, "1995-03-02", "folder/folder/image.png", Role.USER));
		User userActual = new User();
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name ='hary12';");
				ResultSet rs = ps.executeQuery();
				userActual = resultSet(rs);
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNotNull(userActual);
		assertEquals("hary12", userActual.getNick_name());
		assertEquals("Andriy", userActual.getFirst_name());
		assertEquals("Mural", userActual.getSecond_name());
		assertEquals("bg@gmail.com", userActual.getE_mail());
		assertEquals(PasswordHelper.encrypt("64561"), userActual.getPassword());
		assertEquals("Ukraine, Lviv, Pasichna, 52", userActual.getAddress());
		assertEquals(PasswordHelper.encrypt("64561"), userActual.getPassword());
		assertEquals("FEMALE", userActual.getSex());
		assertEquals("1995-03-02", userActual.getDate_of_birth());
		assertEquals("folder/folder/image.png", userActual.getAvatar());
		assertEquals("USER", userActual.getRole());
		logger.info("test create user");
	}

	@Test
	public void testUpdateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("read", "Natalya", "Bolyk", "Poland, Wrocjlav, Pasichna, 52", "bg@gmail.com", "64561",
				Sex.FEMALE, "1999-10-10", "some.jpeg", Role.ADMIN);
		userDAO.create(user);
		user.setFirst_name("IRA");
		user.setSecond_name("BLLLLL");
		user.setAddress("Poland, Gdansk, Naberejna, 52");
		userDAO.update(user);
		User userActual = new User();
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
				ps.setString(1, user.getNick_name());
				ResultSet rs = ps.executeQuery();
				userActual = resultSet(rs);
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNotNull(userActual);
		assertEquals("read", userActual.getNick_name());
		assertEquals("IRA", userActual.getFirst_name());
		assertEquals("BLLLLL", userActual.getSecond_name());
		assertEquals("bg@gmail.com", userActual.getE_mail());
		assertEquals("Poland, Gdansk, Naberejna, 52", userActual.getAddress());
		assertEquals("64561", userActual.getPassword());
		assertEquals("FEMALE", userActual.getSex());
		assertEquals("1999-10-10", userActual.getDate_of_birth());
		assertEquals("some.jpeg", userActual.getAvatar());
		assertEquals("ADMIN", userActual.getRole());
		logger.info("test update user");
	}

	@Test
	public void testDeleteUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("delete", "Natalya", "Bolyk", "Uk, Uk, gh, 5", "bg@gmail.com", "64561", Sex.FEMALE, null,
				"jfhfff.mvn", Role.ADMIN);
		userDAO.create(user);
		userDAO.delete(user);
		assertNull(userDAO.readByNickName("delete"));
		logger.info("test delete user");
	}

	@Test
	public void testGetAll() {
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("Bozo", "Oleg", "Ponkin", "Russia, Moscow, Kreml, 10", "bsss@gmail.com", "64561", Sex.MALE,
				null, "jsjwe.txt", Role.ADMIN);
		userDAO.create(user);
		int actual = userDAO.getAll().size();
		int expected = 4;
		assertEquals(expected, actual);
		logger.info("test get all");
	}

	@Test
	public void testGetByNickName() {
		User userActual = new User();
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("Bobik", "Oleg", "Ponkin", "France, Paris, Ave, 45", "bsss@gmail.com", "kjhgyiuu",
				Sex.MALE, null, "jsjwe.txt", Role.ADMIN);
		userDAO.create(user);
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
				ps.setString(1, "Bobik");
				ResultSet rs = ps.executeQuery();
				userActual = resultSet(rs);
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNotNull(userActual);
		assertEquals(user.getNick_name(), userActual.getNick_name());
		assertEquals(user.getFirst_name(), userActual.getFirst_name());
		assertEquals(user.getSecond_name(), userActual.getSecond_name());
		assertEquals(user.getE_mail(), userActual.getE_mail());
		assertEquals(user.getAddress(), userActual.getAddress());
		assertEquals(PasswordHelper.encrypt(user.getPassword()), userActual.getPassword());
		assertEquals(user.getSex(), userActual.getSex());
		assertEquals(user.getDate_of_birth(), userActual.getDate_of_birth());
		assertEquals(user.getAvatar(), userActual.getAvatar());
		assertEquals(user.getRole(), userActual.getRole());
		logger.info("test get by nickname");
	}

	@Test
	public void testreadByKey() {
		UserDAOImpl dao = new UserDAOImpl();
		User user = dao.readByKey("1");
		assertNotNull(user);
		assertEquals(user.getNick_name(), "BigBunny");
		assertEquals(user.getFirst_name(), "Oleg");
		assertEquals(user.getSecond_name(), "Pavliv");
		assertEquals(user.getAddress(), "USA, NC, timesquare, 5");
		assertEquals(user.getE_mail(), "hgdf@gmail.com");
		assertEquals(PasswordHelper.encrypt("kdfhgrr"), PasswordHelper.encrypt(user.getPassword()));
		assertEquals(user.getSex(), "MALE");
		assertEquals(user.getDate_of_birth(), "1992-02-02");
		assertEquals(user.getAvatar(), null);
		assertEquals(user.getRole(), "USER");
		logger.info("test get by id");
	}

	@Test
	public void testCountAllBySex() {
		UserDAOImpl user = new UserDAOImpl();
		int count = user.countAllBySex("MALE");
		assertEquals(2, count);
	}

	@Test
	public void testGetUsersByRole() {
		UserDAOImpl dao = new UserDAOImpl();
		List<User> list = dao.getUsersByRole(Role.USER);
		assertEquals(2, list.size());
	}

	@Test
	public void testGetByDateOfBirth() {
		UserDAOImpl user = new UserDAOImpl();
		List<User> list = user.getByYearOfBirth("1989");
		int actual = list.size();
		int expected = 1;
		assertEquals(expected, actual);
	}

	@Test
	public void testGetUserByNickAndPassword() {
		UserDAOImpl dao = new UserDAOImpl();
		User user1 = new User("Bobik", "Oleg", "Ponkin", "France, Paris, Ave, 45", "bsss@gmail.com", "kjhgyiuu",
				Sex.MALE, null, "jsjwe.txt", Role.ADMIN);
		dao.create(user1);
		user1 = null;
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
				ps.setString(1, "Bobik");
				ResultSet rs = ps.executeQuery();
				user1 = resultSet(rs);
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}

		User user2 = dao.getUserByNickAndPassword("Bobik", "kjhgyiuu");
		assertNotNull(user2);
		assertEquals(user1.getNick_name(), user2.getNick_name());
		assertEquals(user1.getFirst_name(), user2.getFirst_name());
		assertEquals(user1.getSecond_name(), user2.getSecond_name());
		assertEquals(user1.getAddress(), user2.getAddress());
		assertEquals(user1.getE_mail(), user2.getE_mail());
		assertEquals(user1.getPassword(), user2.getPassword());
		assertEquals(user1.getSex(), user2.getSex());
		assertEquals(user1.getDate_of_birth(), user2.getDate_of_birth());
		assertEquals(user1.getAvatar(), user2.getAvatar());
		assertEquals(user1.getRole(), user2.getRole());

		user2 = null;
		user2 = dao.getUserByNickAndPassword("Bobik", "another");
		assertNull(user2);
	}

	@Test
	public void testLogIn() {
		UserDAOImpl dao = new UserDAOImpl();
		User user1 = new User("Bobik", "Oleg", "Ponkin", "France, Paris, Ave, 45", "bsss@gmail.com", "kjhgyiuu",
				Sex.MALE, null, "jsjwe.txt", Role.ADMIN);
		dao.create(user1);
		user1 = null;
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
				ps.setString(1, "Bobik");
				ResultSet rs = ps.executeQuery();
				user1 = resultSet(rs);
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}

		String session1 = dao.logIn("Bobik", "kjhgyiuu");
		User user = dao.getUserByNickAndPassword("Bobik", "kjhgyiuu");
		String session2 = user.getSession();
		assertEquals(session1, session2);

		session1 = dao.logIn("Bobik", "someAnotherPassword");
		assertNull(session1);
	}

	@Test
	public void testLogOut() {
		UserDAOImpl dao = new UserDAOImpl();
		User user1 = new User("Bobik", "Oleg", "Ponkin", "France, Paris, Ave, 45", "bsss@gmail.com", "kjhgyiuu",
				Sex.MALE, null, "jsjwe.txt", Role.ADMIN);
		dao.create(user1);
		user1 = null;
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement(
						"select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
				ps.setString(1, "Bobik");
				ResultSet rs = ps.executeQuery();
				user1 = resultSet(rs);
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		dao.logIn("Bobik", "kjhgyiuu");
		boolean result = dao.logOut("Bobik");
		assertTrue(result);
		result = dao.logOut("notInBaseUser");
		assertFalse(result);
	}

	private User resultSet(ResultSet rs) {
		User user = null;
		try {
			while (rs.next()) {
				user = new User();
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
}
