package com.softserve.tc.diary.dao.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.Constant.RecordList;

/**
 * 
 * @author Mykola-
 * 
 */

public class RecordDAOImplTest {
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
    public void testCreateRecord() {

        Timestamp createdTime = new Timestamp(new java.util.Date().getTime());

        RecordDAOImpl RecordDAO = RecordDAOImpl.getInstance(conn);
        Record newRecord = new Record("1", createdTime, "sport", "#JUST DO IT!!!", "http:/bigBoss/works/perfectly",
                Status.PRIVATE);
        RecordDAO.create(newRecord);
        Record record = null;

        try (Connection connection = conn.getConnection()) {
            ps = connection.prepareStatement("select * from record_list where user_id_rec ='1' and title like 'sport' "
                    + "and supplement like 'http:/bigBoss/works/perfectly' and visibility like 'PRIVATE';");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record = new Record(rs.getString(RecordList.IDREC), rs.getString(RecordList.USERIDREC),
                        rs.getTimestamp(RecordList.CREATEDTIME), rs.getString(RecordList.TITLE),
                        rs.getString(RecordList.TEXT), rs.getString(RecordList.SUPPLEMENT),
                        Status.valueOf(rs.getString(RecordList.VISIBILITY)));
            }

        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertNotNull(record);
        assertEquals("1", record.getUserId());
        assertEquals("sport", record.getTitle());
        assertEquals("#JUST DO IT!!!", record.getText());
        assertEquals("http:/bigBoss/works/perfectly", record.getSupplement());
        assertEquals("PRIVATE", record.getVisibility());
        logger.info("Test create record");
    }

    @Test
    public void testUpdateRecord() {
        // Timestamp createdTime = new Timestamp(new
        // java.util.Date().getTime());

        RecordDAOImpl recordDAO = RecordDAOImpl.getInstance(conn);
        Record rec = recordDAO.readByKey("1");
        rec.setText("NewText");
        rec.setTitle("NewTitle");
        rec.setSupplement("NewSupplement");
        rec.setVisibility("PRIVATE");
        recordDAO.update(rec);
        Record record = null;
        try (Connection connection = conn.getConnection()) {
            ps = connection.prepareStatement("select * from record_list where id_rec=?");
            ps.setString(1, rec.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record = new Record(rs.getString(RecordList.IDREC), rs.getString(RecordList.USERIDREC),
                        rs.getTimestamp(RecordList.CREATEDTIME), rs.getString(RecordList.TITLE),
                        rs.getString(RecordList.TEXT), rs.getString(RecordList.SUPPLEMENT),
                        Status.valueOf(rs.getString(RecordList.VISIBILITY)));
            }

        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertEquals(rec.getUuid(), record.getUuid());
        // assertEquals(rec.getCreated_time(), record.getCreated_time());
        assertEquals(rec.getSupplement(), record.getSupplement());
        assertEquals(rec.getText(), record.getText());
        assertEquals(rec.getUserId(), record.getUserId());
        assertEquals(rec.getVisibility(), record.getVisibility());
        logger.info("test update record");
    }

    @Test
    public void testGetAll() {
        RecordDAOImpl recordDAO = RecordDAOImpl.getInstance(conn);
        List<Record> list = recordDAO.getAll();
        assertEquals(3, list.size());
        logger.info("test get all");
    }

    @Test
    public void testDeleteRecord() {
        RecordDAOImpl recordDAO = RecordDAOImpl.getInstance(conn);
        List<Record> listBefore = recordDAO.getAll();
        Record rec = recordDAO.readByKey("3");
        recordDAO.delete(rec);
        List<Record> listAfter = recordDAO.getAll();
        assertEquals(listBefore.size() - 1, listAfter.size());
        logger.info("test delete record");
    }
}
