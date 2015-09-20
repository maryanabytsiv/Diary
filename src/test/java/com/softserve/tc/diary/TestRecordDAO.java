package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.connectmanager.TestDBConnection;
import com.softserve.tc.diary.dao.implementation.RecordDAOImpl;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.log.Log;

/**
 * 
 * @author Mykola-
 *        
 */

public class TestRecordDAO {
    private Logger logger = Log.init(this.getClass().getName());
    private PreparedStatement ps = null;
    
    @BeforeClass
    public static void setUpBeforeClass() throws SQLException {
        DBCreationManager.setUpBeforeClass();
    }
    
    @Before
    public void beforeTest() throws SQLException {
        DBCreationManager.insertValue();
    }
    
    @After
    public void afterTest() throws SQLException {
        DBCreationManager.deleteAllFromTable();
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        DBCreationManager.DropTableIfExists();
    }
    
    @Test
    public void testCreateRecord() {
        
        Timestamp createdTime = new Timestamp(new java.util.Date().getTime());
        
        RecordDAOImpl RecordDAO = new RecordDAOImpl();
        Record newRecord = new Record("1", createdTime, "sport", "#JUST DO IT!!!",
                "http:/bigBoss/works/perfectly",
                Status.PRIVATE);
        RecordDAO.create(newRecord);
        Record record = null;
        
        try (Connection connection = TestDBConnection.getConnection()) {
            ps = connection.prepareStatement(
                    "select * from record_list where user_id_rec ='1' and title like 'sport' "
                    + "and supplement like 'http:/bigBoss/works/perfectly' and visibility like 'PRIVATE';");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record = new Record(rs.getString(1), rs.getString(2), rs.getTimestamp(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), Status.valueOf(rs.getString(7)));
            }
            
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertNotNull(record);
        assertEquals("1", record.getUser_name());
        assertEquals(newRecord.getCreated_time(), record.getCreated_time());
        assertEquals("sport", record.getTitle());
        assertEquals("#JUST DO IT!!!", record.getText());
        assertEquals("http:/bigBoss/works/perfectly", record.getSupplement());
        assertEquals("PRIVATE", record.getVisibility());
        logger.info("Test create record");
    }
    
    @Test
    public void testUpdateRecord() {
        Timestamp createdTime = new Timestamp(new java.util.Date().getTime());
        
        RecordDAOImpl recordDAO = new RecordDAOImpl();
        Record rec = new Record("1", createdTime, "work", "#Work HARD!!!",
                "https://motivation/inUkraine/improveMySelf",
                Status.PRIVATE);
        rec.setId_rec(UUID.randomUUID().toString());
        try (Connection connection = TestDBConnection.getConnection()) {
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement(
                        "insert into record_list values(?,?,CAST(? AS DATE),?,?,?,?)");
                ps.setString(1, rec.getId_rec());
                ps.setString(2, rec.getUser_name());
                ps.setTimestamp(3, rec.getCreated_time());
                ps.setString(4, rec.getTitle());
                ps.setString(5, rec.getText());
                ps.setString(6, rec.getSupplement());
                ps.setString(7, rec.getVisibility());
                ps.execute();
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
                connection.setAutoCommit(true);
            }
            connection.setAutoCommit(true);
        } catch (SQLException e1) {
            logger.error("insert failed", e1);
        }
        recordDAO.update(rec);
        Record record = null;
        try (Connection connection = TestDBConnection.getConnection()) {
            ps = connection.prepareStatement(
                    "select * from record_list where id_rec=?");
            ps.setString(1, rec.getId_rec());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record = new Record(rs.getString(1),rs.getString(2), rs.getTimestamp(3),
                        rs.getString(4),rs.getString(5), rs.getString(6),
                        Status.valueOf(rs.getString(7)));
            }
            
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertEquals(rec.getId_rec(), record.getId_rec());
    //    assertEquals(rec.getCreated_time(), record.getCreated_time());
        assertEquals(rec.getSupplement(), record.getSupplement());
        assertEquals(rec.getText(), record.getText());
        assertEquals(rec.getUser_name(), record.getUser_name());
        assertEquals(rec.getVisibility(), record.getVisibility());
        logger.info("test update record");
    }
    
    @Test
    public void testGetAll() {
        RecordDAOImpl recordDAO = new RecordDAOImpl();
        List<Record> list = recordDAO.getAll();
        assertEquals(3, list.size());
        logger.info("test get all");
    }
    
    @Test
    public void TestDeleteRecord() {
        Timestamp createdTime = new Timestamp(new java.util.Date().getTime());
        RecordDAOImpl recordDAO = new RecordDAOImpl();
        Record record = new Record("1", createdTime, "hello","#Hello, how are you??",
                "http:/Lviv/theBest/Town",
                Status.PRIVATE);
        recordDAO.create(record);
        List<Record> listBefore = recordDAO.getAll();
        recordDAO.delete(record);
        List<Record> listAfter = recordDAO.getAll();
        assertEquals(listBefore.size() - 1, listAfter.size());
        logger.info("test delete record");
    }
}
