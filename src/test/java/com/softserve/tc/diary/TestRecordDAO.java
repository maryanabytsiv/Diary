package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.RecordDAOImpl;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;

/**
 * 
 * @author Mykola-
 *
 */

public class TestRecordDAO {
	
	@BeforeClass
	public static void setUpBeforeClass() throws SQLException{
		Query.setUpBeforeClass();
		}

	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		Query.DropTableIfExists();
		}

	@Before
	public void beforeTest() throws SQLException{
		Query.insertValue();
		}
	
	 @After
	public void afterTest() throws SQLException{
		Query.deleteAllFromTable();
		}
	
	
	@Test
		public void testCreateRecord() {

		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
		
		RecordDAOImpl RecordDAO = new RecordDAOImpl();
		Record newRecord = new Record( "1", createdTime, "#JUST DO IT!!!", "http:/bigBoss/works/perfectly", Status.PRIVATE );
		RecordDAO.create(newRecord);
		Record record = null;
		
		try {
			Query.ps = Query.connection.prepareStatement("select * from record_list where user_id_rec ='1';");
			ResultSet rs = Query.ps.executeQuery();
			while (rs.next()) {
				record = new Record( rs.getString(2), rs.getTimestamp(3), rs.getString(4), rs.getString(5),Status.PRIVATE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertNotNull(record);
		assertEquals("1", record.getUser_name());
		assertEquals(newRecord.getCreated_time(), record.getCreated_time());
		assertEquals("#JUST DO IT!!!", record.getText());
		assertEquals("http:/bigBoss/works/perfectly", record.getSupplement());
		assertEquals("PRIVATE" , record.getVisibility());
	}
	

	@Test
	public void testUpdateRecord() {
		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
		
        RecordDAOImpl recordDAO = new RecordDAOImpl();
        Record rec = new Record("1", createdTime, "#Work HARD!!!","https://motivation/inUkraine/improveMySelf", Status.PRIVATE);
        rec.setId_rec(recordDAO.getGeneratedId());

        try {
            Query.ps = Query.connection.prepareStatement("insert into record_list values(?,?,CAST(? AS DATE),?,?,?)");
            Query.ps.setString(1, rec.getId_rec());
            Query.ps.setString(2, rec.getUser_name());
            Query.ps.setTimestamp(3, rec.getCreated_time());
            Query.ps.setString(4, rec.getText());
            Query.ps.setString(5, rec.getSupplement());
            Query.ps.setString(6, rec.getVisibility());
            Query.ps.execute();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        recordDAO.update(rec);
        Record record = null;
        try {
        	Query.ps = Query.connection.prepareStatement("select * from record_list where id_rec=?");
        	Query.ps.setString(1, rec.getId_rec());
            ResultSet rs = Query.ps.executeQuery();
            while (rs.next()) {
            	record = new Record(rs.getString(2), rs.getTimestamp(3), rs.getString(4), rs.getString(5),Status.valueOf(rs.getString(6)));
            	record.setId_rec(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(rec.getId_rec(), record.getId_rec());
        assertEquals(rec.getCreated_time(), record.getCreated_time());
        assertEquals(rec.getSupplement(), record.getSupplement());
        assertEquals(rec.getText(), record.getText());
        assertEquals(rec.getUser_name(), record.getUser_name());
        assertEquals(rec.getVisibility(), record.getVisibility());
	}

	@Test
	public void testGetAll() {
		RecordDAOImpl recordDAO = new RecordDAOImpl();
		List<Record> list = recordDAO.getAll();
		assertEquals(3, list.size());
	}
	
    @Test
    public void TestDeleteRecord() {
		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
        RecordDAOImpl recordDAO = new RecordDAOImpl();
        Record record = new Record( "2", createdTime, "#Hello, how are you??", "http:/Lviv/theBest/Town", Status.PRIVATE );
        recordDAO.create(record);
        recordDAO.delete(record);
        assertNull(recordDAO.getRecordByName("2"));
    }
}
