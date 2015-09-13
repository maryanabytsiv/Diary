package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.Tag;

/**
 * 
 * @author Mykola-
 *
 */

public class RecordDAOImpl implements RecordDAO, BaseDAO<Record>, IdGenerator {

	private static Connection conn = null;
	private static PreparedStatement ps;
	private static PreparedStatement ps1;

	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}

	public void create(Record object) {
		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
		
		try {
			conn = ConnectManager.getConnectionToTestDB();

			ps = conn.prepareStatement("insert into record_list values(?,?,?,?,?,?);");
			ps.setString(1, getGeneratedId());
			ps.setString(2, object.getUser_name());
			ps.setTimestamp(3, object.getCreated_time());
			ps.setString(4, object.getText());
			ps.setString(5, object.getSupplement());
			ps.setString(6, object.getVisibility());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Record readByKey(String id) {
		Record record = null;
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("select * from record_list where id_rec=?;");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				record = new Record(rs.getString(2), rs.getTimestamp(3), rs.getString(4), rs.getString(5), Status.PRIVATE);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return record;
	}

	public void update(Record object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement(
					"update record_list set user_id_rec = ?, created_time = CAST(? AS TIMESTAMP), text = ?,"
							+ " supplement = ?, visibility = ? where user_id_rec = ?;");
			ps.setString(1, object.getUser_name());
			ps.setTimestamp(2, object.getCreated_time());
			ps.setString(3, object.getText());
			ps.setString(4, object.getSupplement());
			ps.setString(5, object.getVisibility());
			ps.setString(6, object.getUser_name());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(Record object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("delete from record_list where user_id_rec=?;");
			ps.setString(1, object.getUser_name());
			
			
//			Tag objt = new Tag("1");
//			ps1 = conn.prepareStatement("select from tag where uuid=?;");
//			ps1.setString(1, objt.getUuid());
//			ResultSet rs = ps1.executeQuery();
//			System.out.println("\nn"+rs);
//			String record_uuid = rs.getString("tag_message");
//			System.out.println("/n/n"+record_uuid);
						
			
			ps.execute();
			

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Record getRecordByName(String user_name) {
		Record record = new Record();
		try {
			ps = conn.prepareStatement("select * from record_list where user_id_rec=?;");
			ps.setString(1, user_name);
			ResultSet rs = ps.executeQuery();
			if (!ps.executeQuery().next())
				return null;
			while (rs.next()) {
				record.setUser_name(rs.getString("user_id_rec"));
				record.setCreated_time(rs.getTimestamp("created_time"));
				record.setText(rs.getString("text"));
				record.setSupplement(rs.getString("supplement"));
				record.setVisibility(rs.getString("visibility"));
			}
		} catch (SQLException error) {
			error.printStackTrace();
		}
		return record;
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
		 try {
			 if (conn == null || conn.isClosed()) {
				 conn = ConnectManager.getConnectionToTestDB();
				 }
			 ps = conn.prepareStatement("SELECT * FROM record_list;");
			 ResultSet rs = ps.executeQuery();
			 while (rs.next()) {
				 list.add( new Record (rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4),
							rs.getString(5), Status.valueOf(rs.getString(6))));
				 }
		 } catch (SQLException e) {
		 e.printStackTrace();
		 }
		return list;
	}

}
