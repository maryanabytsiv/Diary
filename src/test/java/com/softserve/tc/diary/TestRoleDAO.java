package com.softserve.tc.diary;

import static org.junit.Assert.*;

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

import com.softserve.tc.diary.dao.implementation.RoleDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.log.Log;

public class TestRoleDAO {
        private Logger logger = Log.init(this.getClass().getName());
	
        @BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBCreationManager.setUpBeforeClass();
	}

	@Before
	public void beforeTest() throws SQLException{
		DBCreationManager.insertValue();
	}

	@After
	public void afterTest() throws SQLException{
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
		try {
			DBCreationManager.ps = DBCreationManager.connection.prepareStatement("SELECT name FROM role WHERE name ='Administrator'");
			ResultSet rs = DBCreationManager.ps.executeQuery();
			while (rs.next()) {
				role.setName(rs.getString("name"));
			}
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
		try {
			DBCreationManager.ps = DBCreationManager.connection.prepareStatement("insert into role values(?,?);");
			DBCreationManager.ps.setString(1, role.getId());
			DBCreationManager.ps.setString(2, role.getName());
			DBCreationManager.ps.execute();
		} catch (SQLException e1) {
			logger.error("insert failed", e1);
		}
		role.setName("reader");

		roleDAO.update(role);
		Role roleActual = new Role();
		try {
			DBCreationManager.ps = DBCreationManager.connection.prepareStatement("select * from role where name =?");
			DBCreationManager.ps.setString(1, role.getName());
			ResultSet rs = DBCreationManager.ps.executeQuery();
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
		//roleDAO.create(role);
		List <User> list = new ArrayList<User>();
		
		try {
			DBCreationManager.ps = DBCreationManager.connection.prepareStatement("select * from user_card where role LIKE 'Admin'");
			ResultSet rs = DBCreationManager.ps.executeQuery();
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
		UserDAOImpl ud = new UserDAOImpl();
		for(User u:list){
			ud.delete(u);
		}
		roleDAO.delete(role);
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
