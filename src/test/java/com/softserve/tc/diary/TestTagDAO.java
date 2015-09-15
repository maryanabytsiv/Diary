package com.softserve.tc.diary;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.TagDAOImpl;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.Tag;

public class TestTagDAO {

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
		SQL_Statement.setUpBeforeClass();
	}

	@Before
	public void beforeTest() throws SQLException{
		SQL_Statement.insertValue();
	}

	@After
	public void afterTest() throws SQLException {
		SQL_Statement.deleteAllFromTable();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		SQL_Statement.DropTableIfExists();
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
		Tag workingTag = new Tag("#HelloWorldWide"); 
		tagDAO.create(workingTag);
		try {
			String query1 = "SELECT tag_message FROM tag " + "WHERE tag_message Like '#HelloWorldWide';";
			// String query2 = "SELECT tag_message FROM tag "
			// + "WHERE tag_message Like '#HelloWorld';";
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement(query1);
			ResultSet rs = SQL_Statement.ps.executeQuery();
			while (rs.next()) {
				workingTag = new Tag(rs.getString("tag_message"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertNotNull(workingTag);
		assertEquals("#HelloWorldWide", workingTag.getTagMessage());
		
		Tag tagAlreadyExist = new Tag("#HelloWorld");
		tagDAO.create(tagAlreadyExist);
		
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
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement(query);
			ResultSet rs = SQL_Statement.ps.executeQuery();
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
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement(query);
			ResultSet rs = SQL_Statement.ps.executeQuery();
			while (rs.next()) {
				countTagRecordAfter = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(countTagRecordAfter, countTagRecordBefore + 1);
	}

	@Test
	public void testCheckIfTagAlreadyExist() {
		TagDAOImpl dao = new TagDAOImpl();
		Tag existTag = new Tag("#HellGuy");
		boolean result = dao.checkIfTagExist(existTag.getTagMessage());
		assertTrue(result);
		Tag notExistTag = new Tag("#Pelmeni");
		result = dao.checkIfTagExist(notExistTag.getTagMessage());
		assertFalse(result);
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
			SQL_Statement.ps = SQL_Statement.connection.prepareStatement(query);
			ResultSet rs = SQL_Statement.ps.executeQuery();
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
	public void testCheckIfRecordHasTag() {
		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
		Record rec = new Record(null, createdTime, "#Hello my name is #Bob. I am from #NewYork", "https://motivation/", Status.PRIVATE);
		TagDAOImpl dao = new TagDAOImpl();
		List<Tag> list = dao.checkIfRecordHasTag(rec);
		assertEquals("#Hello", list.get(0).getTagMessage());
		assertEquals("#Bob.", list.get(1).getTagMessage());
		assertEquals("#NewYork", list.get(2).getTagMessage());
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
