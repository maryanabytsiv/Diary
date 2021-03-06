package com.softserve.tc.diary.dao.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.log.Log;

public class TagDAOImplTest {
    private Logger logger = Log.init(this.getClass().getName());
    private PreparedStatement ps = null;

    private static ConnectionManager conn = ConnectionManager.getInstance(DataBaseTest.TESTDB);
            
    @BeforeClass
    public static void setUpBeforeClass() throws SQLException {
    	DBCreationManagerHelper.setUpBeforeClass();
    }
    
    @Before
    public void beforeTest() throws SQLException {
		DBCreationManagerHelper.insertValue();
    }
    
    @After
    public void afterTest() throws SQLException {
    	DBCreationManagerHelper.deleteAllFromTable();
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
    	DBCreationManagerHelper.DropTableIfExists();
    }
    
    @Test
    public void testGetTagByMessage() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        String message = "#HellGuy";
        String messageForNull = "#notInBase";
        Tag tag1 = tagDAO.getTagByMessage(message);
        Tag tag2 = tagDAO.getTagByMessage(messageForNull);
        
        assertEquals("#HellGuy", tag1.getTagMessage());
        assertNull(tag2);
        logger.info("test get tag by message");
    }
    
    @Test
    public void testCreateTag() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        Tag workingTag = new Tag("#HelloWorldWide");
        tagDAO.create(workingTag);
        try (Connection connection = conn.getConnection()) {
            String query1 = "SELECT tag_message FROM tag "
                    + "WHERE tag_message Like '#HelloWorldWide';";
            ps = connection.prepareStatement(query1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                workingTag = new Tag(rs.getString("tag_message"));
            }
            ps.close();
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertNotNull(workingTag);
        assertEquals("#HelloWorldWide", workingTag.getTagMessage());
        
        logger.info("test create tag");
    }
    
    @Test
    public void testCreateFromRecord() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        
        String rec_id = "3";
        String tagMessage = "#Bimbo";
        List<Tag> listTagBefore = tagDAO.getAll();
        int countTagRecordBefore = 0;
        String query = "Select COUNT(*) from tag_record";
        try (Connection connection = conn.getConnection()) {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                countTagRecordBefore = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        
        tagDAO.createFromRecord(rec_id, tagMessage);
        
        List<Tag> listTagAfter = tagDAO.getAll();
        assertEquals(listTagAfter.size(), listTagBefore.size() + 1);
        
        int countTagRecordAfter = 0;
        try (Connection connection = conn.getConnection()) {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                countTagRecordAfter = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("execute query failed", e);
        }
        assertEquals(countTagRecordAfter, countTagRecordBefore + 1);
        logger.info("test create from record");
    }
    
    @Test
    public void testCheckIfTagAlreadyExist() {
        TagDAOImpl dao = TagDAOImpl.getInstance(conn);
        Tag existTag = new Tag("#HellGuy");
        boolean result = dao.checkIfTagExist(existTag.getTagMessage());
        assertTrue(result);
        Tag notExistTag = new Tag("#Pelmeni");
        result = dao.checkIfTagExist(notExistTag.getTagMessage());
        assertFalse(result);
        logger.info("test check if tag already exist");
    }
    
    @Test
    public void testReadByKey() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        String uuid = "testkey7";
        Tag tag = tagDAO.readByKey(uuid);
        assertEquals(tag.getTagMessage(), "#HelloTeam");
        tag = null;
        uuid = "testkey10";
        tag = tagDAO.readByKey(uuid);
        assertNull(tag);
        logger.info("test read by key");
    }
    
    @Test
    public void testDeleteFromtagRecord() {
        String uuid = "testkey8";
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        tagDAO.deleteFromTagRecord(uuid);
        String query =
                "Select * from tag_record where tag_uuid like '" + uuid + "';";
        int count = 0;
        try (Connection connection = conn.getConnection()) {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertEquals(0, count);
        logger.info("test delete from tag_record");
    }
    
    @Test
    public void testDeleteTag() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        List<Tag> listBeforeDel = tagDAO.getAll();
        tagDAO.delete(listBeforeDel.get(2));
        List<Tag> listAfterDel = tagDAO.getAll();
        assertEquals(listBeforeDel.size() - 1, listAfterDel.size());
        logger.info("test delete tag");
    }
    
    @Test
    public void testCheckIfRecordHasTag() {
        Timestamp createdTime = new Timestamp(new java.util.Date().getTime());
        Record rec = new Record(null, createdTime, null,
                "#Hello my name is #Bob. I am from #NewYork",
                "https://motivation/",
                Status.PRIVATE);
        TagDAOImpl dao = TagDAOImpl.getInstance(conn);
        List<Tag> list = dao.checkIfRecordHasTag(rec);
        assertEquals("#Hello", list.get(0).getTagMessage());
        assertEquals("#Bob.", list.get(1).getTagMessage());
        assertEquals("#NewYork", list.get(2).getTagMessage());
        logger.info("test check if record has tag");
    }
    
    @Test
    public void testGetListTagsByPrefix() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        String prefix = "#Hello";
        List<Tag> list = tagDAO.getListTagsByPrefix(prefix);
        assertEquals(3, list.size());
        logger.info("test get list tags by prefix");
    }
    
    @Test
    public void testGetListTagsBySuffix() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        String suffix = "ell";
        List<Tag> list = tagDAO.getListTagsBySuffix(suffix);
        assertEquals(5, list.size());
        logger.info("test get list tags by suffix");
    }
    
    @Test
    public void testGetAll() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        List<Tag> list = tagDAO.getAll();
        assertEquals(9, list.size());
        logger.info("test get all");
    }
    
    @Test
    public void testGetListRecordByTag() {
        TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
        Tag tag = new Tag("testkey5", "#nice");
        List<Record> list = tagDAO.getListRecordsByTag(tag);
        assertEquals(list.size(), 2);
        logger.info("test get list record by tag");
    }
    
//     @Test
//     public void testGetListRecordByListTag() {
//     TagDAOImpl tagDAO = TagDAOImpl.getInstance(conn);
//     List<Tag> listTag = tagDAO.getListTagsByPrefix("#Hell");
//     List<Record> listRecord = tagDAO.getListRecordsByListOfTags(listTag);
//     assertEquals(listRecord.size(), 1);
//     }
}
