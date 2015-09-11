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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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

	@Before
	public void beforeTest() {
		String isertData = "insert into role values('1','Admin') ;" + " insert into role values('2','User') ;"
				+ "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
				+ "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
				+ "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
				+ "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', '2', 'hgdf@gmail.com', 'kdfhgrr', 'M', '1992-02-02', null, '2');"
				+ "insert into user_card values('2','Sonic', 'Ira', 'Dub', '1', 'dfhfght@gmail.com', 'vfjukiuu', 'F', '1990-03-08', null, '1');"
				+ "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', '3', 'jhfcjfdf@gmail.com', 'flgkjhlkftjt', 'M', '1989-02-20', null, '2');"
				+ "insert into record_list values('1','1','2015-02-23 00:00:00','skjdhugfkdxufgesiurkgtiudshkfjghkdf',null,'public');"
				+ "insert into record_list values('2','3','2015-05-20 12:00:56','skjdhugfkdxufge',null,'private');"
				+ "insert into record_list values('3','1','2015-06-10 17:20:56','fkjb5kj4g5khg4555xufge',null,'public');"
				+ "insert into tag values('1','#cars');" + "insert into tag values('3','#family');"
				+ "insert into tag values('2','#Love');" + "insert into tag values('4','#murderrrrrr');"
				+ "insert into tag_record values('1','1', '2');"
				+ "insert into tag_record values('2','3', '1');"
				+ "insert into tag_record values('3','2', '3');";
		try {
			ps = conn.prepareStatement(isertData);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@After
	public void afterTest(){
		String deleteData="delete from tag_record;"+"delete from record_list;"+"delete from user_card;"+"delete from address;"+"delete from role;"+"delete from tag;";
				try {
					ps = conn.prepareStatement(deleteData);
					ps.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		 ps = conn.prepareStatement("DROP TABLE IF EXISTS tag_record;" + "DROP TABLE IF EXISTS record_list;"
		 + "DROP TABLE IF EXISTS user_card;" + "DROP TABLE IF EXISTS address;"
		 + "DROP TABLE IF EXISTS role;"
		 + "DROP TABLE IF EXISTS tag;");
		 ps.execute();
		ps.close();
		conn.close();
	}

	@Test
	public void testCreateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(
				new User("hary12", "Andriy", "Mural", "1", "bg@gmail.com", "64561", Sex.MALE, null, null, "2"));
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
		// assertEquals(5, user.getU_id());
		assertEquals("hary12", user.getNick_name());
		assertEquals("Andriy", user.getFirst_name());
		assertEquals("Mural", user.getSecond_name());
		assertEquals("bg@gmail.com", user.getE_mail());
		assertEquals("1", user.getAddress());
		assertEquals("64561", user.getPassword());
		// assertEquals(Sex.FEMALE, user.getSex());
		assertEquals(null, user.getDate_of_birth());
		assertEquals(null, user.getAvatar());
		assertEquals("2", user.getRole());
	}

	@Test
	public void testReadByKeyInt() {

//		UserDAOImpl userDAO = new UserDAOImpl();
//		User user = new User("read", "Natalya", "Bolyk", "1", "bg@gmail.com", "64561", Sex.FEMALE, null, null,
//				"1");
//		userDAO.create(user);
//		userDAO.readByKey(user.getUuid());
//
//		assertNotNull(userDAO.readByKey(7));

	}

	@Test
	public void testUpdateUser() {

	}

	@Test
	public void testDeleteUser() {

//		UserDAOImpl userDAO = new UserDAOImpl();
//		User user = new User("delete", "Natalya", "Bolyk", "1", "bg@gmail.com", "64561", Sex.FEMALE, null, null,
//				"1");
//		userDAO.create(user);
//		userDAO.delete(user);
//		assertNull(userDAO.getByNickName("delete"));

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

		// UserDAOImpl userDAO = new UserDAOImpl();
		//
		// User user = new User("dateOfBirth", "Natalya", "Bolyk", "Lviv",
		// "bg@gmail.com", "64561", Sex.FEMALE, null, null,
		// "User");
		// userDAO.create(user);
		//
		// user = new User("dateOfBirth", "Natalya", "Bolyk", "Lviv",
		// "bg@gmail.com", "64561", Sex.FEMALE, null, null,
		// "User");
		// userDAO.create(user);
		//
		// user = new User("dateOfBirth", "Natalya", "Bolyk", "Lviv",
		// "bg@gmail.com", "64561", Sex.FEMALE, null, null,
		// "User");
		// userDAO.create(user);
		//
		//// userDAO.getByDateOfBirth(dateOfBirth)
		//
		// assertNotNull(userDAO.readByKey(7));

	}

}
