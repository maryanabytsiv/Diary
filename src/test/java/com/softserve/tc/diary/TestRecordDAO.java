package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import com.softserve.tc.diary.dao.implementation.RecordDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.entity.Visibility;

public class TestRecordDAO {
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
		ps.close();
		conn.close();
	}

	@Test
	public void testCreateRecord() {
		RecordDAOImpl RecordDAO = new RecordDAOImpl();
		RecordDAO.create(new Record( "Nikko", "2015-12-12", "#Hello, how are you??", "http:/ntiguwgni/gtrwgtwg/gwt", Visibility.PRIVATE ));
		Record record = null;
		try {

			ps = conn.prepareStatement("select * from user_card where nick_name ='hary12';");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				record = new Record( rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),Visibility.PRIVATE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertNotNull(record);
		//assertEquals(5, user.getU_id());
		assertEquals("hary12", record.getNick_name().trim());
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

}
