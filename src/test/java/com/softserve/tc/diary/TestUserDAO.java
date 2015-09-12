package com.softserve.tc.diary;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class TestUserDAO {
	public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	private static Connection conn;
	private static PreparedStatement ps;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		BufferedReader br = null;
		String scriptSQL;
		String result = "";
		try {
			br = new BufferedReader(new FileReader("./PostgreSQL_DB/DiaryTest.sql"));
			while ((scriptSQL = br.readLine()) != null) {
				result += scriptSQL + "\n";

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			ps = conn.prepareStatement(result);
			ps.execute();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
//		ps = conn.prepareStatement("DROP TABLE IF EXISTS tag_record;" + "DROP TABLE IF EXISTS record_list;"
//				+ "DROP TABLE IF EXISTS user_card;" + "DROP TABLE IF EXISTS address;" + "DROP TABLE IF EXISTS role;"
//				+ "DROP TABLE IF EXISTS tag;");
//		ps.execute();
		ps.close();
		conn.close();
	}

	@Test
	public void testCreateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		
		userDAO.create(new User( "hary12", "Andriy", "Mural", "Lviv", "bg@gmail.com", "64561", Sex.MALE, null, null,
				"User"));
		User user = new User();
		try {

			ps = conn.prepareStatement("select * from user_card where nick_name ='hary12';");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user.setNick_name(rs.getString("nick_name"));
				user.setFirst_name(rs.getString("first_name"));
				user.setSecond_name(rs.getString("second_name"));
				user.setAddress(rs.getString("address_id"));
				user.setE_mail(rs.getString("e_mail"));
				user.setPassword(rs.getString("password"));
				user.setSex(null);
				user.setDate_of_birth(null);
				user.setAvatar(rs.getString("avatar"));
				user.setRole(rs.getString("role"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		assertNotNull(user);
		//assertEquals(5, user.getU_id());
		assertEquals("hary12", user.getNick_name());
		assertEquals("Andriy", user.getFirst_name());
		assertEquals("Mural", user.getSecond_name());
		assertEquals("bg@gmail.com", user.getE_mail());
		assertEquals("1", user.getAddress());
		assertEquals("64561", user.getPassword());
		// assertEquals(Sex.FEMALE, user.getSex());
		assertEquals(null, user.getDate_of_birth());
		assertEquals(null, user.getAvatar());
		assertEquals("user", user.getRole());
	}

	@Test
	public void testReadByKeyInt() {
		
		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("read", "Natalya", "Bolyk", "Lviv", "bg@gmail.com", "64561", Sex.FEMALE, null, null,
				"User");
		userDAO.create(user);
		userDAO.readByKey(user.getUuid());
		
		assertNotNull(userDAO.readByKey(7));
		
	}

	@Test
	public void testUpdateUser() {
	
	}

	@Test
	public void testDeleteUser() {

		UserDAOImpl userDAO = new UserDAOImpl();
		User user = new User("delete", "Natalya", "Bolyk", "Lviv", "bg@gmail.com", "64561", Sex.FEMALE, null, null,
				"User");
		userDAO.create(user);
		userDAO.delete(user);
		assertNull(userDAO.getByNickName("delete"));

	}

	@Test
	public void testGetAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByNickName() {
		fail("Not yet implemented");
	}

	@Test
	public void testCountAllBySex() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByDateOfBirth() {
		
//		UserDAOImpl userDAO = new UserDAOImpl();
//
//		User user = new User("dateOfBirth", "Natalya", "Bolyk", "Lviv", "bg@gmail.com", "64561", Sex.FEMALE, null, null,
//				"User");
//		userDAO.create(user);
//		
//		user = new User("dateOfBirth", "Natalya", "Bolyk", "Lviv", "bg@gmail.com", "64561", Sex.FEMALE, null, null,
//				"User");
//		userDAO.create(user);
//		
//		user = new User("dateOfBirth", "Natalya", "Bolyk", "Lviv", "bg@gmail.com", "64561", Sex.FEMALE, null, null,
//				"User");
//		userDAO.create(user);
//		
////		userDAO.getByDateOfBirth(dateOfBirth)
//		
//		assertNotNull(userDAO.readByKey(7));
		
	}



}
