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

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.TagDAOImpl;
import com.softserve.tc.diary.entity.Record;
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
				"insert into record_list values('1',null,'2015-02-23 00:00:00',"
				+ "'#Hello my name is Bod. I am from #NewYork',null,'public');"
				+ "insert into record_list values('2',null,'2015-05-20 12:00:56',"
				+ "'That was #nice day. #Halloween so cool',null,'private');"
				+ "insert into record_list values('3',null,'2015-06-10 17:20:56',"
				+ "'#HelloTeam, it is #nice to meet in NewYork',null,'public');";
		 
		String query2 = 
				"insert into tag (uuid, tag_message) values('testkey1','#Hell');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey2','#Hello');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey3','#HelloWorld');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey4','#HellGuy');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey5','#nice');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey6','#Halloween');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey7','#HelloTeam');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey8','#NewYork');" + '\n'
				+ "insert into tag (uuid, tag_message) values('testkey9','#HelpMe');";


		 String query3 =
				 "insert into tag_record values('rt1',1,'testkey2');" + '\n'
				+ "insert into tag_record values('rt2',1,'testkey8');" + '\n'
				+ "insert into tag_record values('rt3',2,'testkey5');" + '\n'
				+ "insert into tag_record values('rt4',2,'testkey6');" + '\n'
				+ "insert into tag_record values('rt5',3,'testkey7');" + '\n'
		 		+ "insert into tag_record values('rt6',3,'testkey5');" + '\n';
		 
		try {
			ps = conn.prepareStatement(query1);
			ps.execute();
			ps = conn.prepareStatement(query2);
			ps.execute();
			ps = conn.prepareStatement(query3);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void afterTest() {
		String query1 = "delete from tag;";
		String query2 = "delete from record_list";
		String query3= "delete from tag_record";
		try {
			ps = conn.prepareStatement(query3);
			ps.execute();
			ps = conn.prepareStatement(query2);
			ps.execute();
			ps = conn.prepareStatement(query1);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetTagByMessage() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		String message = "#HellGuy";
		String messageForNull = "#notInBase";
		Tag tag1 = tagDAO.getTagByMessage(message);
		Tag tag2 = tagDAO.getTagByMessage(messageForNull);
		
		assertEquals("#HellGuy", tag1.getTagMessage());
		assertNull(tag2);
	}
	
	@Test
	public void testCreateTag() {
		TagDAOImpl tagDAO = new TagDAOImpl();
	//	Tag tagException = new Tag("#HelloWorld");
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
	}

	@Test
	public void testCreateFromRecord() {
		TagDAOImpl tagDAO = new TagDAOImpl();

		String rec_id = "3";
		String tagMessage = "#Bimbo";
		List<Tag> listTagBefore = tagDAO.getAll();
		int countTagRecordBefore = 0;
		String query = "Select COUNT(*) from tag_record";
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				countTagRecordBefore = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		tagDAO.createFromRecord(rec_id, tagMessage);

		List<Tag> listTagAfter = tagDAO.getAll();
		assertEquals(listTagAfter.size(), listTagBefore.size() + 1);

		int countTagRecordAfter = 0;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				countTagRecordAfter = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(countTagRecordAfter, countTagRecordBefore + 1);
	}

	@Test
	public void testReadByKey() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		String uuid = "testkey7";
		Tag tag = tagDAO.readByKey(uuid);
		assertEquals(tag.getTagMessage(), "#HelloTeam");
		tag = null;
		uuid = "testkey10";
		tag = tagDAO.readByKey(uuid);
		assertNull(tag);
	}

	@Test
	public void testDeleteFromtagRecord() {
		String uuid = "testkey8";
		TagDAOImpl tagDAO = new TagDAOImpl();
		tagDAO.deleteFromTagRecord(uuid);
		String query = "Select * from tag_record where tag_uuid like '" + uuid + "';";
		int count = 0;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(0, count);
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
	public void testGetListTagsByPrefix() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		String prefix = "#Hello";
		List<Tag> list = tagDAO.getListTagsByPrefix(prefix);
		assertEquals(3, list.size());
	}

	@Test
	public void testGetListTagsBySuffix() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		String suffix = "ell";
		List<Tag> list = tagDAO.getListTagsBySuffix(suffix);
		assertEquals(5, list.size());
	}
	
	@Test
	public void testGetAll() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		List<Tag> list = tagDAO.getAll();
		assertEquals(9, list.size());
	}

	@Test
	public void testGetListRecordByTag() {
		TagDAOImpl tagDAO = new TagDAOImpl();
		Tag tag = new Tag("testkey5", "#nice");
		List<Record> list = tagDAO.getListRecordsByTag(tag);
		assertEquals(list.size(), 2);
	}
	
	@Test
	public void testGetListRecordByListTag() {
//		TagDAOImpl tagDAO = new TagDAOImpl();
//		List<Tag> listTag = tagDAO.getListTagsByPrefix("#Hell");
//		List<Record> listRecord = tagDAO.getListRecordsByListOfTags(listTag);
//		assertEquals(listRecord.size(), 1);
	}
}
