package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.entity.Visibility;

public class RecordDAOImpl extends BaseDAOImpl<Record> implements RecordDAO{
	public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	private static Connection conn;
	private static PreparedStatement ps;
	
	private static void getConnection(){
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void create(Record object) {
		getConnection();
		try {
			ps = conn.prepareStatement("insert into record_list(user_uuid, created_time, text, supplement, visibility) values(?,?,?,?,?);");
			ps.setString(1, object.getUser_name());
			ps.setString(2, object.getCreated_time());
			ps.setString(3, object.getText());
			ps.setString(4, object.getSupplement());  
			ps.setString(5, "pb");
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Record readByKey(int id) {
		getConnection();
		Record record = null;
		try {
			ps = conn.prepareStatement("select * from record_list where uuid=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				record = new Record( rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), Visibility.PUBLIC);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return record;
	}
	
	@Override
	public void update(Record object) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void delete(Record object) {
		getConnection();
		try {
			ps = conn.prepareStatement("delete from record_list where uuid=?");
			ps.setInt(1, object.getUuid());
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


	@Override
	public List<Record> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
