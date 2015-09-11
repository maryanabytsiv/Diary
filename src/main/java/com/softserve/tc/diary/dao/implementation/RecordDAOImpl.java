package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.Visibility;

public class RecordDAOImpl implements RecordDAO, BaseDAO<Record>, IdGenerator{
	
	private static Connection conn = null;
	private static PreparedStatement ps;
	
	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}
	
	public void create(Record object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			
			ps = conn.prepareStatement("insert into record_list values(?,?,?,?,?,'F');");
			ps.setString(1, getGeneratedId());
			ps.setString(2, object.getUser_name());
			ps.setNull(3, 0);
			ps.setString(4, object.getText());
			ps.setString(5, object.getSupplement());  
			//ps.setString(6, object.getVisibility());
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
			ps = conn.prepareStatement("select * from record_list where id_rec=?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				record = new Record( rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), Visibility.PUBLIC);
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
                    "update record_list set user_id_rec = ? where id_rec = ?");
            ps.setString(1, object.getUser_name());
            ps.setString(2, object.getId_rec());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	public void delete(Record object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("delete from record_list where id_rec=?");
			ps.setString(1, object.getId_rec());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public List<Record> getRecordByName(String user_name) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getRecordByDate(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getRecordByVisibility(String visibility) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getRecordTypeOfSupplement(String typeOfSupplement) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
