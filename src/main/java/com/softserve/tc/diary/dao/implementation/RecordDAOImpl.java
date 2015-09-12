package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Sex;
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
            ps.setString(2, object.getUuid());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	public void delete(Record object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("delete from record_list where uuid=?");
			ps.setString(1, object.getUuid());
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
		List<Record> list = new ArrayList<Record>();
		String query = "SELECT * FROM record_list;";
		try {
			if (conn == null || conn.isClosed()) {
				conn = ConnectManager.getConnectionToTestDB();
			}
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String user_name = rs.getString("user_id_rec");
				System.out.println(user_name);
				String created_time = rs.getString("created_time");
				System.out.println(created_time);
				String text = rs.getString("text");
				System.out.println(text);
				String supplement = rs.getString("supplement");
				System.out.println(supplement);
				String visibility= rs.getString("visibility");
				System.out.println(visibility);
				if (visibility.equals(Visibility.PRIVATE)) {
					list.add(new Record(user_name, created_time, text, supplement, Visibility.PRIVATE));
				}
				else if (visibility.equals(Visibility.PUBLIC)) {
					list.add(new Record(user_name, created_time, text, supplement, Visibility.PUBLIC));
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		RecordDAOImpl r = new RecordDAOImpl();
		List<Record> list = r.getAll();
		System.out.println(list.size());
	}
	
}
