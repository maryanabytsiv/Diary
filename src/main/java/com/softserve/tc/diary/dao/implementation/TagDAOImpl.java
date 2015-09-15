package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.log.Log;

public class TagDAOImpl implements TagDAO, IdGenerator {

	private static Connection conn;
	private static PreparedStatement ps;
	private Logger logger = Log.init(this.getClass().getName());
	
	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}
	
	public Tag getTagByMessage(String tagMessage) {
		Tag tag = null;
		String query = "Select * from tag where tag_message like '" + tagMessage + "';";
		try {
			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tag = new Tag(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			logger.error("can't get tag by message", e);
		}
		return tag;
	}
	
	public void create(Tag object) {
		String tagMessage = object.getTagMessage();
		if (true == checkIfTagExist(tagMessage)) {
			System.out.println("Tag already exist");
		} else {
		try {
			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}
			ps = conn.prepareStatement("insert into tag values(?,?);");
			String tagId = getGeneratedId();
			ps.setString(1, tagId);
			ps.setString(2, object.getTagMessage());
			ps.execute();
		} catch (SQLException e) {
			logger.error("can't create tag", e);
		}
		}
	}
	
	public boolean checkIfTagExist(String tagMessage) {
		List<Tag> list = getAll();
		for (Tag tag : list) {
			if (tag.getTagMessage().equals(tagMessage)) {
				return true;
			}
		}
		return false;
	}

	public void createFromRecord(String recordId, String tagMessage) {
		if (true == checkIfTagExist(tagMessage)) {
			logger.warn("Tag already exist");
		} else {
		try {
			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}
			ps = conn.prepareStatement("insert into tag values(?,?);");
			String tagId = getGeneratedId();
			ps.setString(1, tagId);
			ps.setString(2, tagMessage);
			ps.execute();
			insertValuesInTagRecord(recordId, tagId);
		} catch (SQLException e) {
			logger.error("can't create from record", e);
		}
		}
	}
	
	public void insertValuesInTagRecord(String recordId, String tagId) {
		String uuid_tr = getGeneratedId();
		String query = "Insert into tag_record values(?,?,?);";
		try {
		if ((conn == null) || (conn.isClosed())) {
			conn = ConnectManager.getConnectionToTestDB();
			}
		ps = conn.prepareStatement(query);
		ps.setString(1, uuid_tr);
		ps.setString(2, recordId);
		ps.setString(3, tagId);
		ps.execute();
		} catch (SQLException e) {
			logger.error("insert failed", e);
		}
	}

	public void delete(Tag object) {
		String uuid = object.getUuid();
		deleteFromTagRecord(uuid);
		String tagMessage = object.getTagMessage();
		String query = "DELETE FROM tag WHERE tag_message Like '" + tagMessage + "';";
		try {

			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}

			ps = conn.prepareStatement(query);
			ps.execute();
		} catch (SQLException e) {
			logger.error("delete failed", e);
		}
	}
	
	public void deleteFromTagRecord(String uuid) {
		String query = "DELETE FROM tag_record WHERE tag_uuid LIKE '" + uuid + "';";
		try {

			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}

			ps = conn.prepareStatement(query);
			ps.execute();
		} catch (SQLException e) {
			logger.error("delete from tag_record failed", e);
		}
	}
	
	public List<Tag> getListTagsByPrefix(String prefix) {
		List<Tag> list = new ArrayList<Tag>();
		try {
			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}

			String query = "SELECT tag_message FROM tag " + "WHERE tag_message LIKE '" + prefix + "%';";
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Tag(rs.getString(1)));
			}
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		return list;
	}

	public List<Tag> getListTagsBySuffix(String suffix) {
		List<Tag> list = new ArrayList<Tag>();
		try {
			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}
			String query = "SELECT tag_message FROM tag " + "WHERE tag_message LIKE '%" + suffix + "%';";
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Tag(rs.getString(1)));
			}
		} catch (SQLException e) {
			logger.error("select by suffix failed", e);
		}
		return list;
	}

	public List<Record> getListRecordsByTag(Tag object) {
		List<Record> listRecordsWithTag = new ArrayList<Record>();
		String uuid = object.getUuid();
		String query = "Select * from record_list where id_rec IN "
				+ "(Select record_uuid from tag_record where tag_uuid Like '" + uuid + "');";
		try {
			if ((conn == null) || (conn.isClosed())) {
				conn = ConnectManager.getConnectionToTestDB();
				}
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String rec_id = rs.getString(1);
				String user_id_rec = rs.getString(2);
				Timestamp created_time = rs.getTimestamp(3);
				String text = rs.getString(4);
				String supplement = rs.getString(5);
				String visability = rs.getString(6);
				Record rec = new Record(rec_id, user_id_rec, created_time, 
						text, supplement, checkVisability(visability));
				listRecordsWithTag.add(rec);
			}
		}catch (SQLException e) {
			logger.error("select by tag failed", e);
		}
		
		return listRecordsWithTag;
	}
	
	private Status checkVisability(String visability) {
		if (Status.PRIVATE.toString().equals(visability)) {
			return Status.PRIVATE;
		}
		else {
			return Status.PUBLIC;
		}
	}

	public List<Tag> checkIfRecordHasTag(Record record) {
		String textRecord = record.getText();
        List<Tag> list = new ArrayList<Tag>();
        for (int i = 0; i < textRecord.length(); i++) {
            if ((textRecord.charAt(i) == '#' && i==0  ) ||
                    (textRecord.charAt(i) == '#' && textRecord.charAt(i - 1) == ' ')) {
                String tagMessage = "";
                for (int j = i; j < textRecord.length(); j++) {
                    if ((textRecord.charAt(j) == ' ')) {
                        tagMessage = textRecord.substring(i, j);
                        i = j;
                        list.add(new Tag(tagMessage));
                        break;
                    }
                    if (j == (textRecord.length() - 1)) {
                        tagMessage = textRecord.substring(i);
                        list.add(new Tag (tagMessage));
                    }
                }
            }
        }
        return list;
    }

	public List<Tag> getAll() {
		List<Tag> list = new ArrayList<Tag>();
		String query = "SELECT * FROM tag;";
		try {
			if ((conn == null) || (conn.isClosed())) {
			conn = ConnectManager.getConnectionToTestDB();
			}
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Tag(rs.getString(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			logger.error("select all failed", e);
		}
		return list;
	}

	public Tag readByKey(String uuid) {
		Tag tag = null;
		try {
		String query = "Select * from tag where uuid Like '" + uuid + "';";
		if ((conn == null) || (conn.isClosed())) {
			conn = ConnectManager.getConnectionToTestDB();
			}
		ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
		tag = new Tag(rs.getString(1), rs.getString(2));
		}
		} catch (SQLException e) {
			logger.error("read by key failed", e);
		}
		return tag;
	}
	
	public List<Record> getListRecordsByListOfTags(List<Tag> listTags) {
		List<Record> finalList = new ArrayList<Record>();
		for (Tag t : listTags) {
			List<Record> forEachTagList = getListRecordsByTag(t);
			if (!(forEachTagList.isEmpty())) {
				finalList.addAll(forEachTagList);
			}
		}	
		return finalList;
	}

	public void update(Tag object) {
	    logger.info("not supported");
	}
}