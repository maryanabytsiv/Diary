package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.connectionmanager.DataBase;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.Constant.RecordList;

/**
 * 
 * @author Mykola-
 *         
 */

public class RecordDAOImpl implements RecordDAO, BaseDAO<Record> {
    
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Logger logger = Log.init(this.getClass().getName());
    private static ConnectionManager connection = null;
    private static RecordDAOImpl recordDAO = null;
    private UserDAOImpl userDAO = UserDAOImpl
            .getInstance(ConnectionManager.getInstance(DataBase.REALDB));
    
    private RecordDAOImpl() {
    }
    
    public static RecordDAOImpl getInstance(ConnectionManager connect) {
        
        if (recordDAO == null) {
            recordDAO = new RecordDAOImpl();
            connection = connect;
        }
        return recordDAO;
        
    }
    
    public String create(Record object) {
        String uuid = UUID.randomUUID().toString();
        logger.debug("creating record");
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                if ((object.getVisibility() == null)) {
                    logger.error(
                            "Please, enter your visibility (PUBLIC / PRIVATE)");
                    throw new NullPointerException();
                } else {
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
            } finally {
                conn.setAutoCommit(true);
            }
            checkIfRecordHasTag(object);
            logger.debug("record created");
        } catch (SQLException e) {
            logger.error("record create failed", e);
        }
        return uuid;
    }
    
    public void checkIfRecordHasTag(Record record) {
        String textRecord = record.getText();
        TagDAOImpl dao = TagDAOImpl.getInstance(connection);
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
                record = new Record(rs.getString(RecordList.IDREC),
                        rs.getString(RecordList.USERIDREC),
                        rs.getTimestamp(RecordList.CREATEDTIME),
                        rs.getString(RecordList.TITLE),
                        rs.getString(RecordList.TEXT),
                        rs.getString(RecordList.SUPPLEMENT),
                        Status.valueOf(rs.getString(RecordList.VISIBILITY)));
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
                        "update record_list SET created_time = ?, title = ?, text = ?, supplement = ?, "
                                + "visibility = ? where user_id_rec = ? AND id_rec = ?;");
                ps.setTimestamp(1,
                        new Timestamp(new java.util.Date().getTime()));
                ps.setString(2, object.getTitle());
                ps.setString(3, object.getText());
                ps.setString(4, object.getSupplement());
                ps.setString(5, object.getVisibility());
                ps.setString(6, object.getUserId());
                ps.setString(7, object.getUuid());
                ps.execute();
                ps.close();
                logger.debug("record updated");
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
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
            } finally {
                conn.setAutoCommit(true);
            }
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
                String id_rec = rs.getString(RecordList.IDREC);
                String user_id_rec = rs.getString(RecordList.USERIDREC);
                Timestamp created_time =
                        rs.getTimestamp(RecordList.CREATEDTIME);
                String title = rs.getString(RecordList.TITLE);
                String text = rs.getString(RecordList.TEXT);
                String supplement = rs.getString(RecordList.SUPPLEMENT);
                Status status =
                        Status.valueOf(rs.getString(RecordList.VISIBILITY));
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
                    "select count(*) AS id_rec from record_list where user_id_rec=?;");
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                num_of_records = rs.getInt(RecordList.IDREC);
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
                String id_rec = rs.getString(RecordList.IDREC);
                String user_id_rec = rs.getString(RecordList.USERIDREC);
                Timestamp created_time =
                        rs.getTimestamp(RecordList.CREATEDTIME);
                String title = rs.getString(RecordList.TITLE);
                String text = rs.getString(RecordList.TEXT);
                String supplement = rs.getString(RecordList.SUPPLEMENT);
                Status status =
                        Status.valueOf(rs.getString(RecordList.VISIBILITY));
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
                String id_rec = rs.getString(RecordList.IDREC);
                String user_id_rec = rs.getString(RecordList.USERIDREC);
                Timestamp created_time =
                        rs.getTimestamp(RecordList.CREATEDTIME);
                String title = rs.getString(RecordList.TITLE);
                String text = rs.getString(RecordList.TEXT);
                String supplement = rs.getString(RecordList.SUPPLEMENT);
                Status status =
                        Status.valueOf(rs.getString(RecordList.VISIBILITY));
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
                String id_rec = rs.getString(RecordList.IDREC);
                String user_id_rec = rs.getString(RecordList.USERIDREC);
                Timestamp created_time =
                        rs.getTimestamp(RecordList.CREATEDTIME);
                String title = rs.getString(RecordList.TITLE);
                String text = rs.getString(RecordList.TEXT);
                String supplement = rs.getString(RecordList.SUPPLEMENT);
                Status status =
                        Status.valueOf(rs.getString(RecordList.VISIBILITY));
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
                String id_rec = rs.getString(RecordList.IDREC);
                String user_id_rec = rs.getString(RecordList.USERIDREC);
                Timestamp created_time =
                        rs.getTimestamp(RecordList.CREATEDTIME);
                String title = rs.getString(RecordList.TITLE);
                String text = rs.getString(RecordList.TEXT);
                String supplement = rs.getString(RecordList.SUPPLEMENT);
                Status status =
                        Status.valueOf(rs.getString(RecordList.VISIBILITY));
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
                String id_rec = rs.getString(RecordList.IDREC);
                String user_id_rec = rs.getString(RecordList.USERIDREC);
                Timestamp created_time =
                        rs.getTimestamp(RecordList.CREATEDTIME);
                String title = rs.getString(RecordList.TITLE);
                String text = rs.getString(RecordList.TEXT);
                String supplement = rs.getString(RecordList.SUPPLEMENT);
                Status status =
                        Status.valueOf(rs.getString(RecordList.VISIBILITY));
                list.add(new Record(id_rec, user_id_rec, created_time, title,
                        text, supplement, status));
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
    public List<String> getDatesWichHaveRecordsPerMonth(String userId,
            LocalDateTime date) {
        List<String> list = new ArrayList<String>();
        
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "SELECT distinct CAST(created_time as date)"
                            + "  FROM record_list where user_id_rec=? and created_time"
                            + " BETWEEN ? AND ?;");
            ps.setString(1, userId);
            // Notes per month from 00:00:00 to 23:59:59
            ps.setTimestamp(2, Timestamp.valueOf(date));
            ps.setTimestamp(3,
                    Timestamp.valueOf(date.plusMonths(1).minusSeconds(1)));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDateTime created_time =
                        rs.getTimestamp(1).toLocalDateTime();
                list.add(created_time.toString());
            }
        } catch (SQLException e) {
            logger.error("can't get all records", e);
        }
        return list;
    }
    
    public int getCountRecordsByDate(Date date) {
    	int countRecords = 0;
    	System.out.println(date);
    	try (Connection conn = connection.getConnection()) {
    		String stringOfDate = new Timestamp(date.getTime()).toString();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM record_list "
            		+ "WHERE date_trunc('day', created_time) = date_trunc('day', timestamp '" + stringOfDate + "');");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            countRecords =  rs.getInt(1);
            }
            logger.info("successfully got count of record for day=" + date + ", count= " + countRecords);
        } catch (SQLException e) {
            logger.error("can't get count of records by date", e);
        }
    	return countRecords; 
    }
    
     public String[][] getRecordDate() {
    
         String[][] mass = new String[31][2];
     try (Connection conn = connection.getConnection()) {
     ps = conn.prepareStatement("SELECT date(created_time) as DATE1, COUNT(*) FROM record_list GROUP BY DATE1 ORDER BY DATE1;");
     ResultSet rs = ps.executeQuery();
     int i = 0;
     int j = 0;
     while (rs.next()) {
     mass[i++][0] = rs.getString(1);
     mass[j++][1] = rs.getString(2);
     }
     } catch (SQLException e) {
     logger.error("can't get all records date", e);
     }
     return mass;
     }
     
     public List<Record> getAllPublicRecordsByNickName(String nickName) {
         User user = userDAO.readByNickName(nickName);
         List<Record> list = new ArrayList<Record>();
         try (Connection conn = connection.getConnection()) {
             ps = conn.prepareStatement(
                     "SELECT * FROM record_list where visibility = 'PUBLIC' and user_id_rec=?;");
             ps.setString(1, user.getUuid());
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
                 String id_rec = rs.getString(RecordList.IDREC);
                 String user_id_rec = rs.getString(RecordList.USERIDREC);
                 Timestamp created_time =
                         rs.getTimestamp(RecordList.CREATEDTIME);
                 String title = rs.getString(RecordList.TITLE);
                 String text = rs.getString(RecordList.TEXT);
                 String supplement = rs.getString(RecordList.SUPPLEMENT);
                 Status status =
                         Status.valueOf(rs.getString(RecordList.VISIBILITY));
                 list.add(new Record(id_rec, user_id_rec, created_time, title,
                         text, supplement, status));
             }
         } catch (SQLException e) {
             logger.error("can't get all records", e);
         }
         return list;
     }
}
