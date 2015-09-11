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

import com.softserve.tc.diary.dao.implementation.TagDAOImpl;
import com.softserve.tc.diary.entity.Tag;

public class TestTagDAO {

	// public static final String URL =
	// "jdbc:postgresql://localhost:5432/DiaryTest";
	// public static final String USER = "root";
	// public static final String PASSWORD = "root";
	private static Connection conn;
	private static PreparedStatement ps;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		conn = ConnectManager.getConnectionToTestDB();
		BufferedReader br = null;
		String scriptSQL;
		StringBuilder sbResult = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader("./PostgreSQL_DB/DiaryTest.sql"));
			while ((scriptSQL = br.readLine()) != null) {
				sbResult.append(scriptSQL);
				sbResult.append("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String result = new String(sbResult);
		ps = conn.prepareStatement(result);
		ps.execute();
	}

	@Test
	public void testCreateTag() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		Tag tag = new Tag("#HelloWorld");
		tagDAO.create(tag);
		tag = null;
		try {
			ps = conn.prepareStatement("SELECT tag_message FROM tag " + 
										"WHERE tag_message Like '#HelloWorld'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tag = new Tag(rs.getString("tag_message"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		assertEquals("#HelloWorld", tag.getTagMessage());

	}

	@Test
	public void testReadByKey() {

		TagDAOImpl tagDAO = new TagDAOImpl();

		// Tag tag = tagDAO.readByKey(key);

	}

}
