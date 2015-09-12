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

import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;

public class TestUserDAO {

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
            conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement(result);
			ps.execute();
			ps.close();
		} catch (Exception e) {
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
	@Before
	public void beforeTest() {
		String isertData = "insert into role values('1','Admin') ;" + " insert into role values('2','User') ;"
				+ "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
				+ "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
				+ "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
				+ "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', '2', 'hgdf@gmail.com', 'kdfhgrr', 'MALE', '1992-02-02', null, '2');"
				+ "insert into user_card values('2','Sonic', 'Ira', 'Dub', '1', 'dfhfght@gmail.com', 'vfjukiuu', 'FEMALE', '1990-03-08', null, '1');"
				+ "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', '3', 'jhfcjfdf@gmail.com', 'flgkjhlkftjt', 'MALE', '1989-02-20', null, '2');"
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
	

	

	@Test
	public void testCreateUser() {
		UserDAOImpl userDAO = new UserDAOImpl();
		userDAO.create(
				new User("hary12", "Andriy", "Mural", "1", "bg@gmail.com", "64561", Sex.FEMALE, "1995-03-02", "folder/folder/image.png", "2"));
		User userActual = new User();
		try {

			ps = conn.prepareStatement("select * from user_card where nick_name ='hary12';");
			ResultSet rs = ps.executeQuery();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertNotNull(userActual);
		assertEquals("hary12", userActual.getNick_name());
		assertEquals("Andriy", userActual.getFirst_name());
		assertEquals("Mural", userActual.getSecond_name());
		assertEquals("bg@gmail.com", userActual.getE_mail());
		assertEquals("1", userActual.getAddress());
		assertEquals("64561", userActual.getPassword());
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
            ps = conn.prepareStatement("insert into user_card values(?,?,?,?,?,?,?,?,CAST(? AS DATE),?,?);");
			ps.setString(1, user.getUuid());
			ps.setString(2, user.getNick_name());
			ps.setString(3, user.getFirst_name());
			ps.setString(4, user.getSecond_name());
			ps.setString(5, user.getAddress());
			ps.setString(6, user.getE_mail());
			ps.setString(7, user.getPassword());
			ps.setString(8, user.getSex());
			ps.setString(9, user.getDate_of_birth());
			ps.setString(10, user.getAvatar());
			ps.setString(11, user.getRole());
			ps.execute();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        user.setFirst_name("IRA");
        user.setSecond_name("BLLLLL");
        userDAO.update(user);
        User userActual = new User();
        try {
            ps = conn.prepareStatement("select * from user_card where nick_name =?");
            ps.setString(1, user.getNick_name());
            ResultSet rs = ps.executeQuery();
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
            // TODO Auto-generated catch block
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
	            ps = conn.prepareStatement("select * from user_card where nick_name =?");
	            ps.setString(1, "TreeTree");
	            ResultSet rs = ps.executeQuery();
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
	            // TODO Auto-generated catch block
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