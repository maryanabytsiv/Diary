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
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.TagDAOImpl;
import com.softserve.tc.diary.entity.Tag;

public class TestTagDAO {

	private static Connection conn;
	private static PreparedStatement ps;

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
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
	
	@Before
	public void beforeTest() {
		String baseValues = "insert into tag (uuid, tag_message) values('testkey1','#Hell');"+ '\n'
							+ "insert into tag (uuid, tag_message) values('testkey2','#Hello');" + '\n'
							+ "insert into tag (uuid, tag_message) values('testkey3','#HelloWorld');" + '\n'
							+ "insert into tag (uuid, tag_message) values('testkey4','#HellGuy');" + '\n'
							+ "insert into tag (uuid, tag_message) values('testkey5','#HellGuy');" + '\n'
							+ "insert into tag (uuid, tag_message) values('testkey6','#HelpMe');";
		try {
			ps = conn.prepareStatement(baseValues);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void afterTest() {
		String del = "delete from tag;";
		try {
			ps = conn.prepareStatement(del);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateTag() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		Tag tagException = new Tag("#HelloWorld");
		Tag workingTag = new Tag("#HelloWorldWide");
		//tagDAO.create(tagException);
		tagDAO.create(workingTag);
		try {
			String query1 = "SELECT tag_message FROM tag "
								+ "WHERE tag_message Like '#HelloWorldWide';";
//			String query2 = "SELECT tag_message FROM tag "
//								+ "WHERE tag_message Like '#HelloWorld';";
			ps = conn.prepareStatement(query1);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				workingTag = new Tag(rs.getString("tag_message"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertNotNull(workingTag);
		assertEquals("#HelloWorldWide", workingTag.getTagMessage());
		
	//	assertEquals("#HelloWorld", tagException.getTagMessage());
	}

	@Test
	public void testGetListTagsByPrefix() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		String prefix = "#Hello";
		List<Tag> list = tagDAO.getListTagsByPrefix(prefix);
		System.out.println(list.size());
		assertEquals(2, list.size());
		
		
	}

}
