package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.log.Log;

/**
 * 
 * @author Mykola-
 *
 */

public class RecordDAOImpl implements RecordDAO, BaseDAO<Record> {

	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	private Logger logger = Log.init(this.getClass().getName());

	public void create(Record object) {
		// Timestamp createdTime = new Timestamp(new
		// java.util.Date().getTime());
		logger.debug("creating record");
		try {
			if ((object.getVisibility() == null)) {
				logger.error("Please, enter your visibility (PUBLIC / PRIVATE)");
				throw new NullPointerException();
			} else {
				conn = ConnectManager.getConnectionToTestDB();

				ps = conn.prepareStatement("insert into record_list values(?,?,?,?,?,?);");
				ps.setString(1, UUID.randomUUID().toString());
				ps.setString(2, object.getUser_name());
				ps.setTimestamp(3, object.getCreated_time());
				ps.setString(4, object.getText());
				ps.setString(5, object.getSupplement());
				ps.setString(6, object.getVisibility());
				ps.execute();
				ps.close();
			}
			logger.debug("record created");
		} catch (SQLException e) {
			logger.error("record create failed", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Record readByKey(String id) {
		Record record = null;
		logger.debug("reading by key");
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("select * from record_list where id_rec=?;");
			ps.setString(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				record = new Record(rs.getString(2), rs.getTimestamp(3), rs.getString(4), rs.getString(5),
						Status.PRIVATE);
			}
		} catch (SQLException e) {
			logger.error("readByKey failed", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return record;
	}

	public void update(Record object) {
		logger.debug("updating record");
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
			logger.debug("record updated");
		} catch (SQLException e) {
			logger.error("can't update record", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void delete(Record object) {
		logger.debug("deleting record");
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("delete from record_list where user_id_rec=?;");
			ps.setString(1, object.getUser_name());
			ps.execute();

			logger.debug("record deleted");
		} catch (SQLException e) {
			logger.error("can't delete record", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public Record getRecordByName(String user_name) {
		Record record = new Record();
		try {
			conn = ConnectManager.getConnectionToTestDB();
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
			logger.error("can't get record by name", error);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				list.add(new Record(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4),
						rs.getString(5), Status.valueOf(rs.getString(6))));
			}
		} catch (SQLException e) {
			logger.error("can't get all records", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

}
