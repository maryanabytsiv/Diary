package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
// import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.connectionmanager.TestDBConnection;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.log.Log;

/**
 * 
 * @author Mykola-
 *        
 */

public class RecordDAOImpl implements RecordDAO, BaseDAO<Record> {
    
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;
    private Logger logger = Log.init(this.getClass().getName());
    
    public void create(Record object) {
        logger.debug("creating record");
        try (Connection conn = TestDBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                if ((object.getVisibility() == null)) {
                    logger.error(
                            "Please, enter your visibility (PUBLIC / PRIVATE)");
                    throw new NullPointerException();
                } else {
                    ps = conn.prepareStatement(
                            "insert into record_list values(?,?,?,?,?,?,?);");
                    ps.setString(1, UUID.randomUUID().toString());
                    ps.setString(2, object.getUser_name());
                    ps.setTimestamp(3, object.getCreated_time());
                    ps.setString(4, object.getTitle());
                    ps.setString(5, object.getText());
                    ps.setString(6, object.getSupplement());
                    ps.setString(7, object.getVisibility());
                    ps.execute();
                    ps.close();
                }
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
                conn.setAutoCommit(true);
            }
            conn.setAutoCommit(true);
            logger.debug("record created");
        } catch (SQLException e) {
            logger.error("record create failed", e);
        }
    }
    
    public Record readByKey(String id) {
    	
        Record record = null;
        logger.debug("reading by key");
        try (Connection conn = TestDBConnection.getConnection()) {
            ps = conn.prepareStatement(
                    "select * from record_list where id_rec=?;");
            rs = ps.executeQuery();
            while (rs.next()) {
                record = new Record(rs.getString(1),rs.getString(2), rs.getTimestamp(3),
                		rs.getString(4), rs.getString(5), rs.getString(6),
                        Status.valueOf(rs.getString(7)));
            }
        } catch (SQLException e) {
            logger.error("readByKey failed", e);
        }
        return record;
    }
    
    public void update(Record object) {
    	
        logger.debug("updating record");
        try (Connection conn = TestDBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "update record_list set user_id_rec = ?, created_time = CAST(? AS TIMESTAMP), title = ?"
                        + "text = ?, supplement = ?, visibility = ? where user_id_rec = ?;");
                ps.setString(1, object.getUser_name());
                ps.setTimestamp(2, object.getCreated_time());
                ps.setString(3, object.getTitle());
                ps.setString(4, object.getText());
                ps.setString(5, object.getSupplement());
                ps.setString(6, object.getVisibility());
                ps.setString(7, object.getUser_name());
                ps.execute();
                ps.close();
                logger.debug("record updated");
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
                conn.setAutoCommit(true);
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("can't update record", e);
        }
    }
    
    public void delete(Record object) {
    	
        logger.debug("deleting record");
        try (Connection conn = TestDBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "delete from record_list where user_id_rec=? and title = ?;");
                ps.setString(1, object.getUser_name());
                ps.setString(2, object.getTitle());
                ps.execute();
                logger.debug("record deleted");
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
                conn.setAutoCommit(true);
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("can't delete record", e);
        }
    }
    
    public List<Record> getRecordByUserId(String user_id) {
    	
        List<Record> list = new ArrayList<Record>();
        try (Connection conn = TestDBConnection.getConnection()) {
            ps = conn.prepareStatement(
                    "select * from record_list where user_id_rec=?;");
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
            	String id_rec = rs.getString(1);
            	String user_id_rec = rs.getString(2);
            	Timestamp created_time = rs.getTimestamp(3);
            	String title = rs.getString(4);
            	String text = rs.getString(5);
            	String supplement = rs.getString(6);
            	Status status = Status.valueOf(rs.getString(7));
                list.add(new Record(id_rec, user_id_rec, created_time, title, text, supplement, status));
            }
        } catch (SQLException error) {
            logger.error("can't get record by name", error);
        }
        return list;
    }
    
    public List<Record> getRecordByDate(String date) {
        return null;
    }
    
    public List<Record> getRecordByVisibility(String visibility) {
        return null;
    }
    
    public List<Record> getRecordTypeOfSupplement(String typeOfSupplement) {
        return null;
    }
    
    public List<Record> getAll() {
    	
        List<Record> list = new ArrayList<Record>();
        try (Connection conn = TestDBConnection.getConnection()) {
            ps = conn.prepareStatement("SELECT * FROM record_list;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	String id_rec = rs.getString(1);
            	String user_id_rec = rs.getString(2);
            	Timestamp created_time = rs.getTimestamp(3);
            	String title = rs.getString(4);
            	String text = rs.getString(5);
            	String supplement = rs.getString(6);
            	Status status = Status.valueOf(rs.getString(7));
                list.add(new Record(id_rec, user_id_rec, created_time, title, text, supplement, status));
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
}
