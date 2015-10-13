package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.connectionmanager.DBConnectionManager;
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
    
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Logger logger = Log.init(this.getClass().getName());
    private ConnectionManager connection = null;
    
    public RecordDAOImpl() {
        this.connection =
                DBConnectionManager.getInstance(true);
    }
    
    public RecordDAOImpl(ConnectionManager conn) {
        this.connection = conn;
    }
    
    public void create(Record object) {
        logger.debug("creating record");
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                if ((object.getVisibility() == null)) {
                    logger.error(
                            "Please, enter your visibility (PUBLIC / PRIVATE)");
                    throw new NullPointerException();
                } else {
                    String uuid = UUID.randomUUID().toString();
                    object.setUuid(uuid);
                    ps = conn.prepareStatement(
       "insert into record_list(id_rec, user_id_rec, title, text, supplement, visibility) values(?,?,?,?,?,?);");
                    ps.setString(1, uuid);
                    ps.setString(2, object.getUserId());
                    ps.setString(3, object.getTitle());
                    ps.setString(4, object.getText());
                    ps.setString(5, object.getSupplement());
                    ps.setString(6, object.getVisibility());
                    ps.execute();
                    ps.close();
                }
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
                conn.setAutoCommit(true);
            }
            conn.setAutoCommit(true);
            checkIfRecordHasTag(object);
            logger.debug("record created");
        } catch (SQLException e) {
            logger.error("record create failed", e);
        }
    }
    
    public void checkIfRecordHasTag(Record record) {
        String textRecord = record.getText();
        TagDAOImpl dao = new TagDAOImpl();
        for (int i = 0; i < textRecord.length(); i++) {
            if ((textRecord.charAt(i) == '#' && i == 0)
                    || (textRecord.charAt(i) == '#'
                            && textRecord.charAt(i - 1) == ' ')) {
                String tagMessage = "";
                for (int j = i; j < textRecord.length(); j++) {
                    if ((textRecord.charAt(j) == ' ')) {
                        tagMessage = textRecord.substring(i, j);
                        i = j;
                        dao.createFromRecord(record.getUuid(), tagMessage);
                        break;
                    }
                    if (j == (textRecord.length() - 1)) {
                        tagMessage = textRecord.substring(i);
                        dao.createFromRecord(record.getUuid(), tagMessage);
                    }
                }
            }
        }
    }
    
    public Record readByKey(String id) {
        
        Record record = null;
        logger.debug("reading by key");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "select * from record_list where id_rec=?;");
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                record = new Record(rs.getString(1), rs.getString(2),
                        rs.getTimestamp(3),
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
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "update record_list set user_id_rec = ?, created_time = CAST(? AS TIMESTAMP), title = ?"
                                + "text = ?, supplement = ?, visibility = ? where user_id_rec = ?;");
                ps.setString(1, object.getUserId());
                ps.setTimestamp(2, object.getCreatedTime());
                ps.setString(3, object.getTitle());
                ps.setString(4, object.getText());
                ps.setString(5, object.getSupplement());
                ps.setString(6, object.getVisibility());
                ps.setString(7, object.getUserId());
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
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "delete from tag_record where record_uuid =?;"
                                + "delete from record_list where id_rec=?;");
                ps.setString(1, object.getUuid());
                ps.setString(2, object.getUuid());
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
        try (Connection conn = connection.getConnection()) {
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
                list.add(new Record(id_rec, user_id_rec, created_time, title,
                        text, supplement, status));
            }
        } catch (SQLException error) {
            logger.error("can't get record by name", error);
        }
        return list;
    }
    
    public int getUserAmountOfRecord(String user_id) {
        int num_of_records = 0;
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "select count(*) from record_list where user_id_rec=?;");
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                num_of_records = rs.getInt(1);
            }
        } catch (SQLException error) {
            logger.error("can't get record by name", error);
        }
        return num_of_records;
        
    }
    
    public List<Record> getRecordByDate(Timestamp date) {
        List<Record> list = new ArrayList<Record>();
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "SELECT * FROM record_list where created_time=?;");
            ps.setTimestamp(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id_rec = rs.getString(1);
                String user_id_rec = rs.getString(2);
                Timestamp created_time = rs.getTimestamp(3);
                String title = rs.getString(4);
                String text = rs.getString(5);
                String supplement = rs.getString(6);
                Status status = Status.valueOf(rs.getString(7));
                list.add(new Record(id_rec, user_id_rec, created_time, title,
                        text, supplement, status));
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
    public List<Record> getRecordByNickNameAndDate(String userId,
            Timestamp date) {
        List<Record> list = new ArrayList<Record>();
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("SELECT * FROM record_list "
            		+ "where user_id_rec=? and created_time BETWEEN ? AND ?;");
            ps.setString(1, userId);
            // Notes per day from 00:00:00 to 23:59:59
    		date.setHours(0);
    		date.setMinutes(0);
    		date.setSeconds(0);
            ps.setTimestamp(2, date);
    		date.setHours(23);
    		date.setMinutes(59);
    		date.setSeconds(59);
            ps.setTimestamp(3, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id_rec = rs.getString(1);
                String user_id_rec = rs.getString(2);
                Timestamp created_time = rs.getTimestamp(3);
                String title = rs.getString(4);
                String text = rs.getString(5);
                String supplement = rs.getString(6);
                Status status = Status.valueOf(rs.getString(7));
                list.add(new Record(id_rec, user_id_rec, created_time, title,
                        text, supplement, status));
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
    public List<Record> getRecordByVisibility(String userId,
            Status visibility) {
        List<Record> list = new ArrayList<Record>();
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "SELECT * FROM record_list where visibility=? and user_id_rec=?;");
            ps.setString(1, visibility.toString());
            ps.setString(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id_rec = rs.getString(1);
                String user_id_rec = rs.getString(2);
                Timestamp created_time = rs.getTimestamp(3);
                String title = rs.getString(4);
                String text = rs.getString(5);
                String supplement = rs.getString(6);
                Status status = Status.valueOf(rs.getString(7));
                list.add(new Record(id_rec, user_id_rec, created_time, title,
                        text, supplement, status));
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
    public List<Record> getAllPublicRecords() {
        List<Record> list = new ArrayList<Record>();
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "SELECT * FROM record_list where visibility like 'PUBLIC';");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id_rec = rs.getString(1);
                String user_id_rec = rs.getString(2);
                Timestamp created_time = rs.getTimestamp(3);
                String title = rs.getString(4);
                String text = rs.getString(5);
                String supplement = rs.getString(6);
                Status status = Status.valueOf(rs.getString(7));
                list.add(new Record(id_rec, user_id_rec, created_time, title,
                        text, supplement, status));
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
    public List<Record> getRecordTypeOfSupplement(String typeOfSupplement) {
        return null;
    }
    
    public List<Record> getAll() {
        
        List<Record> list = new ArrayList<Record>();
        try (Connection conn = connection.getConnection()) {
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
                list.add(new Record(id_rec, user_id_rec, created_time, title,
                        text, supplement, status));
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
	public List<String> getDatesWichHaveRecordsPerMonth(String userId, LocalDateTime date) {
		List<String> list = new ArrayList<String>();

		try (Connection conn = connection.getConnection()) {
			ps = conn.prepareStatement("SELECT distinct CAST(created_time as date)"
					+ "  FROM record_list where user_id_rec=? and created_time"
					+ " BETWEEN ? AND ?;");
			ps.setString(1, userId);
			// Notes per month from 00:00:00 to 23:59:59
			ps.setTimestamp(2, Timestamp.valueOf(date));
			ps.setTimestamp(3, Timestamp.valueOf(date.plusMonths(1).minusSeconds(1)));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LocalDateTime created_time = rs.getTimestamp(1).toLocalDateTime();
				list.add(created_time.toString());
			}
		} catch (SQLException e) {
			logger.error("can't get all records", e);
		}
		return list;
	}
    
}