package com.softserve.tc.diary;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.connectmanager.TestDBConnection;
import com.softserve.tc.diary.dao.implementation.RoleDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.log.Log;

public class TestRoleDAO {
	private Logger logger = Log.init(this.getClass().getName());
	private PreparedStatement ps = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBCreationManager.setUpBeforeClass();
	}

	@Before
	public void beforeTest() throws SQLException {
		DBCreationManager.insertValue();
	}

	@After
	public void afterTest() throws SQLException {
		DBCreationManager.deleteAllFromTable();
	}

	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		DBCreationManager.DropTableIfExists();
	}

	@Test
	public void testCreateRole() {
		RoleDAOImpl roleDAO = new RoleDAOImpl();

		roleDAO.create(new Role("Administrator"));
		Role role = new Role();
		try (Connection connection = TestDBConnection.getConnection()) {
			ps = connection.prepareStatement("SELECT name FROM role WHERE name ='Administrator'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				role.setName(rs.getString("name"));
			}
			ps.close();
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertEquals("Administrator", role.getName());
		logger.info("Test create role");
	}

	@Test
	public void testUpdateRole() {
		RoleDAOImpl roleDAO = new RoleDAOImpl();

		Role role = new Role("reader");
		role.setId(UUID.randomUUID().toString());
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement("insert into role values(?,?);");
				ps.setString(1, role.getId());
				ps.setString(2, role.getName());
				ps.execute();
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e1) {
			logger.error("insert failed", e1);
		}
		role.setName("reader");

		roleDAO.update(role);
		Role roleActual = new Role();
		try (Connection connection = TestDBConnection.getConnection()) {
			ps = connection.prepareStatement("select * from role where name =?");
			ps.setString(1, role.getName());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				roleActual.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNotNull(roleActual);
		assertEquals("reader", roleActual.getName());
		logger.info("test update role");
	}

	@Test
	public void testDeleteRole() {
		RoleDAOImpl roleDAO = new RoleDAOImpl();
		Role role = new Role("Admin");
		// roleDAO.create(role);
		List<User> list = new ArrayList<User>();
		try (Connection connection = TestDBConnection.getConnection()) {
			ps = connection
					.prepareStatement("select * from user_card where role LIKE 'Admin'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User userActual = new User();
				userActual.setNick_name(rs.getString("nick_name"));
				userActual.setFirst_name(rs.getString("first_name"));
				userActual.setSecond_name(rs.getString("second_name"));
				userActual.setAddress(rs.getString("address_id"));
				userActual.setE_mail(rs.getString("e_mail"));
				userActual.setPassword(rs.getString("password"));
				userActual.setSex(rs.getString("Sex"));
				userActual.setDate_of_birth(rs.getString("date_of_birth"));
				userActual.setAvatar(rs.getString("avatar"));
				userActual.setRole(rs.getString("role"));
				list.add(userActual);
			}
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNull(roleDAO.readByKey("Admin"));
		logger.info("test delete role");
	}

	@Test
	public void testGetAll() {
		RoleDAOImpl roleDAO = new RoleDAOImpl();
		Role role = new Role("SomeUser");
		roleDAO.create(role);
		int actual = roleDAO.getAll().size();
		int expected = 3;
		assertEquals(expected, actual);
		logger.info("test getAll");
	}
}
