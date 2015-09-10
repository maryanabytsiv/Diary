package com.softserve.tc.diary;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUserDAO {
	public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	private static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BufferedReader br = null;
		String scriptSQL;
		String result = "";
		try {
			br = new BufferedReader(new FileReader("G:\\Projects\\Diary\\PostgreSQL_DB\\DiaryTest.sql"));
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
			PreparedStatement ps = conn.prepareStatement(result);
			ps.execute();
			// ResultSet myRs = ps.executeQuery("select * from city");
			// while (myRs.next()){
			// System.out.println(myRs.getString(1) + " , "
			// + myRs.getString("country_id") + " , " +
			// myRs.getString("city_name"));}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCreateUser() {
		//PreparedStatement ps = conn.prepareStatement("");
		
//		UserDAOImpl userDAO = new UserDAOImpl();
//		//User user = new User(1, "honey", "Khrystyna", "Bulych", "1", "kh@gmail.com", "111", Sex.FEMALE, "2015-01-01", "", "2");
//
//		userDAO.create(user);
//        assertNotNull(user);
//        assertEquals(1, user.getU_id());
//        assertEquals("honey", user.getNick_name());
//        assertEquals("Khrystyna", user.getFirst_name());
//        assertEquals("Bulych", user.getSecond_name());
//        assertEquals("1", user.getAddress_id());
//        assertEquals("111", user.getPassword());
//        assertEquals(Sex.FEMALE, user.getSex());
//        assertEquals("2015-01-01", user.getDate_of_birth());
//        assertEquals("", user.getAvatar());
//        assertEquals("2", user.getRole_id());
	}

	@Test
	public void testReadByKeyInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteUser() {
		fail("Not yet implemented");
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
	public void testGetCityByMaxNumberOfUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByDAteOfBirth() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAvarageAgeOfUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllEmailsByNickNames() {
		fail("Not yet implemented");
	}

}
