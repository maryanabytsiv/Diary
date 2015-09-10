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
		ps = conn.prepareStatement("DROP TABLE IF EXISTS tag_record;" + "DROP TABLE IF EXISTS record_list;"
				+ "DROP TABLE IF EXISTS user_card;" + "DROP TABLE IF EXISTS address;" + "DROP TABLE IF EXISTS role;"
				+ "DROP TABLE IF EXISTS tag;");
		ps.execute();
		ps.close();
	}

	@Test
	public void testCreateUser() {

		// try {
		// ps = conn.prepareStatement("insert into user_card
		// values(?,?,?,?,?,?,?,?,?,?,?);");
		// ps.setInt(1, 4);
		// ps.setString(2, "honey");
		// ps.setString(3, "Khrystyna");
		// ps.setString(4, "Bulych");
		// ps.setInt(5, 1);
		// ps.setString(6, "hjgfjhgfhj@mail.com");
		// ps.setString(7, "dkjrhgkltr");
		// ps.setString(8, "F");
		// ps.setNull(9, 0);
		// ps.setNull(10, 0);
		// ps.setInt(11,1);
		// ps.execute();
		//
		// //insert into user_card values(1, 'BigBunny', 'Oleg', 'Pavliv', 2,
		// 'hgdf@gmail.com', 'kdfhgrr', 'M', '1992-02-02', null, 2);
		//
		// ps.close();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(new User(5, "hary12", "Andriy", "Mural", "Lviv", "bg@gmail.com", "64561", Sex.MALE, null, null,
				"User"));
		User user = null;
		try {

			ps = conn.prepareStatement("select * from user_card where u_id=5");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), "Lviv",
						rs.getString(6), rs.getString(7), Sex.FEMALE, rs.getString(9), rs.getString(10), "admin");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// UserDAOImpl userDAO = new UserDAOImpl();
		// //User user = new User(1, "honey", "Khrystyna", "Bulych", "1",
		// "kh@gmail.com", "111", Sex.FEMALE, "2015-01-01", "", "2");
		//
		// userDAO.create(user);

		assertNotNull(user);
		assertEquals(5, user.getU_id());
		assertEquals("hary12", user.getNick_name().trim());
		assertEquals("Andriy", user.getFirst_name().trim());
		assertEquals("Mural", user.getSecond_name().trim());
		assertEquals("bg@gmail.com", user.getE_mail().trim());
		// assertEquals("1", user.getAddress_id());
		assertEquals("64561", user.getPassword().trim());
		// assertEquals(Sex.FEMALE, user.getSex());
		assertEquals(null, user.getDate_of_birth());
		assertEquals(null, user.getAvatar());
		assertEquals("admin", user.getRole_id().trim());
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
