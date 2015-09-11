package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Tag;

public class TagDAOImpl extends BaseDAOImpl<Tag> implements TagDAO {
	
	public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	private static Connection conn;
	private static PreparedStatement ps;

	private static void getConnection() {
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void create(Tag object) {
		getConnection();
		try {
			ps = conn.prepareStatement("insert into tag values(?,?);");
			ps.setInt(1, object.getU_u_id());
			ps.setString(2, object.getTag());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Tag readByKey(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Tag object) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(Tag object) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Tag> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTagIdByTagMessage(String TagMessage) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Tag> getListTagsByPrefix(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Tag> getListTagsBySuffix(String suffix) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getListRecordsByTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getListRecordsByListOfTags(List<Tag> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public static void main(String[] args) {
//		TagDAOImpl t = new TagDAOImpl();
//		t.create(new Tag(1,"gg"));
//	}
	
}