package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class TestUserDAO {

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
		Query.setUpBeforeClass();
	}
	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		Query.DropTableIfExists();
	}
	@Before
	public void beforeTest() throws SQLException{
		Query.insertValue();
	}
	@After
	public void afterTest() throws SQLException{
		Query.deleteAllFromTable();
	}

	@Test
	public void testCreateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(
				new User("hary12", "Andriy", "Mural", "1", "bg@gmail.com", "64561", Sex.FEMALE, "1995-03-02", "folder/folder/image.png", "2"));
		User userActual = new User();
		try {

			Query.ps = Query.connection.prepareStatement("select * from user_card where nick_name ='hary12';");
			ResultSet rs = Query.ps.executeQuery();
			while (rs.next()) {
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertNotNull(userActual);
		assertEquals("hary12", userActual.getNick_name());
		assertEquals("Andriy", userActual.getFirst_name());
		assertEquals("Mural", userActual.getSecond_name());
		assertEquals("bg@gmail.com", userActual.getE_mail());
		assertEquals("1", userActual.getAddress());
		//assertEquals("64561", userActual.getPassword());
	    assertEquals("FEMALE", userActual.getSex());
		assertEquals("1995-03-02", userActual.getDate_of_birth());
		assertEquals("folder/folder/image.png", userActual.getAvatar());
		assertEquals("2", userActual.getRole());
	}

	@Test
	public void testUpdateUser() {
        	 
		UserDAOImpl userDAO = new UserDAOImpl();
        
		User user = new User("read", "Natalya", "Bolyk", "1", "bg@gmail.com", "64561", Sex.FEMALE, "1999-10-10", "some.jpeg",
		"1");
		user.setUuid(userDAO.getGeneratedId());
        try {
        	Query.ps = Query.connection.prepareStatement("insert into user_card values(?,?,?,?,?,?,?,?,CAST(? AS DATE),?,?);");
        	Query.ps.setString(1, user.getUuid());
        	Query.ps.setString(2, user.getNick_name());
        	Query.ps.setString(3, user.getFirst_name());
        	Query.ps.setString(4, user.getSecond_name());
        	Query.ps.setString(5, user.getAddress());
        	Query.ps.setString(6, user.getE_mail());
        	Query.ps.setString(7, user.getPassword());
        	Query.ps.setString(8, user.getSex());
        	Query.ps.setString(9, user.getDate_of_birth());
        	Query.ps.setString(10, user.getAvatar());
        	Query.ps.setString(11, user.getRole());
        	Query.ps.execute();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        user.setFirst_name("IRA");
        user.setSecond_name("BLLLLL");
        userDAO.update(user);
        User userActual = new User();
        try {
        	Query.ps = Query.connection.prepareStatement("select * from user_card where nick_name =?");
        	Query.ps.setString(1, user.getNick_name());
            ResultSet rs = Query.ps.executeQuery();
            while (rs.next()) {
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

	}

	@Test
	public void testDeleteUser() {
		
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("delete", "Natalya", "Bolyk", "1", "bg@gmail.com", "64561", Sex.FEMALE, null, "jfhfff.mvn",
				"1");
		userDAO.create(user);
		userDAO.delete(user);
		assertNull(userDAO.readByKey("delete"));

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
	}

	@Test
	public void testGetByNickName() {
		 User userActual = new User();
	        try {
	        	Query.ps = Query.connection.prepareStatement("select * from user_card where nick_name =?");
	        	Query.ps.setString(1, "TreeTree");
	            ResultSet rs = Query.ps.executeQuery();
	            while (rs.next()) {
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
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
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
	}

	@Test
	public void testCountAllBySex() {

	}

	@Test
	public void testGetByDateOfBirth() {


	}

}