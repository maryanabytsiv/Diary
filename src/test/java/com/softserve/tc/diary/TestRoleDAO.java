package com.softserve.tc.diary;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.dao.implementation.RoleDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.User;

public class TestRoleDAO {
        static final Logger logger = Logger.getLogger(TestRoleDAO.class);
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SQL_Statement.setUpBeforeClass();
	}

	@Before
	public void beforeTest() throws SQLException{
		SQL_Statement.insertValue();
	}

	@After
	public void afterTest() throws SQLException{
		SQL_Statement.deleteAllFromTable();
	}

	@Test
	public void testCreateRole() {
	        PropertyConfigurator.configure("log4j.properties");
		RoleDAOImpl roleDAO = new RoleDAOImpl();

		roleDAO.create(new Role("Administrator"));
		Role role = new Role();
		try {
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement("SELECT name FROM role WHERE name ='Administrator'");
			ResultSet rs = SQL_Statement.ps.executeQuery();
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
	        PropertyConfigurator.configure("log4j.properties");
		RoleDAOImpl roleDAO = new RoleDAOImpl();

		Role role = new Role("reader");
		role.setId(roleDAO.getGeneratedId());
		try {
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement("insert into role values(?,?);");
			SQL_Statement.ps.setString(1, role.getId());
			SQL_Statement.ps.setString(2, role.getName());
			SQL_Statement.ps.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			logger.error("insert failed", e1);
		}
		role.setName("reader");

		roleDAO.update(role);
		Role roleActual = new Role();
		try {
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement("select * from role where name =?");
			SQL_Statement.ps.setString(1, role.getName());
			ResultSet rs = SQL_Statement.ps.executeQuery();
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
	        PropertyConfigurator.configure("log4j.properties");
		RoleDAOImpl roleDAO = new RoleDAOImpl();
		Role role = new Role("Admin");
		//roleDAO.create(role);
		List <User> list = new ArrayList<User>();
		
		try {
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement("select * from user_card where role LIKE 'Admin'");
			ResultSet rs = SQL_Statement.ps.executeQuery();
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
	        PropertyConfigurator.configure("log4j.properties");
		RoleDAOImpl roleDAO = new RoleDAOImpl();
		Role role = new Role("SomeUser");
		roleDAO.create(role);
		int actual = roleDAO.getAll().size();
		int expected = 3;
		assertEquals(expected, actual);
		logger.info("test getAll");
	}
}
