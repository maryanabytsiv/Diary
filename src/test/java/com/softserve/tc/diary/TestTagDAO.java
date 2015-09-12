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
		String query1 =
			"insert into tag (uuid, tag_message) values('testkey1','#Hell');"+ '\n'
				+ "insert into tag (uuid, tag_message) values('testkey2','#Hello');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey3','#HelloWorld');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey4','#HellGuy');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey5','#HellGuy');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey6','#HelpMe');";
		
//		String query2 = 
//			     "insert into record_list values('1','1','2015-02-23 00:00:00',"
//			    		+ "'#Hello my name is #Bod. I am from #NewYork',null,'public');"
//			    + "insert into record_list values('2','3','2015-05-20 12:00:56',"
//			    		+ "'That was #nice day. #Halloween so cool',null,'private');"
//			    + "insert into record_list values('3','1','2015-06-10 17:20:56',"
//			    		+ "'#HelloTeam, it is #nice to meet in #NewYork',null,'public');";


		try {
			ps = conn.prepareStatement(query1);
			ps.execute();
//			ps = conn.prepareStatement(query2);
//			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void afterTest() {
		String query = "delete from tag;";
		try {
			ps = conn.prepareStatement(query);
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
		// tagDAO.create(tagException);
		tagDAO.create(workingTag);
		try {
			String query1 = "SELECT tag_message FROM tag " + "WHERE tag_message Like '#HelloWorldWide';";
			// String query2 = "SELECT tag_message FROM tag "
			// + "WHERE tag_message Like '#HelloWorld';";
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

		// assertEquals("#HelloWorld", tagException.getTagMessage());
	}

	@Test
	public void testGetListTagsByPrefix() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		String prefix = "#Hello";
		List<Tag> list = tagDAO.getListTagsByPrefix(prefix);
		assertEquals(2, list.size());
	}

	@Test
	public void testGetListTagsBySuffix() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		String suffix = "Hello";
		List<Tag> list = tagDAO.getListTagsBySuffix(suffix);
		assertEquals(2, list.size());
	}

	@Test
	public void testDeleteTag() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		List<Tag> listBeforeDel = tagDAO.getAll();
		tagDAO.delete(listBeforeDel.get(2));
		List<Tag> listAfterDel = tagDAO.getAll();
		assertEquals(listBeforeDel.size() - 1, listAfterDel.size());

	}

	@Test
	public void testGetAll() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		List<Tag> list = tagDAO.getAll();
		assertEquals(6, list.size());
	}

}
