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
import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Tag;

public class TagDAOImpl implements TagDAO, BaseDAO<Tag>, IdGenerator {

	private static Connection conn;
	private static PreparedStatement ps;

	private static void getConnection() {
		try {
			conn = ConnectManager.getConnectionToTestDB();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private static void closeConnection() {
		try {
			ConnectManager.closeConnectionToTestDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}

	public void create(Tag tagObject) {
		getConnection();
		TagDAOImpl obj = new TagDAOImpl();
		try {
			ps = conn.prepareStatement("insert into tag values(?,?);");
			ps.setString(1, obj.getGeneratedId());
			ps.setString(2, tagObject.getTagMessage());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public List<Tag> getListTagsByPrefix(String prefix) {
		getConnection();
		List<Tag> list = new ArrayList<Tag>();
		try {
			ps = conn.prepareStatement("SELECT tag_message FROM tag " + "WHERE tag_message LIKE '" + prefix + "%';");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Tag(rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return list;
	}

	public List<Tag> getListTagsBySuffix(String suffix) {

		return null;
	}

	public List<Record> getListRecordsByTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getListRecordsByListOfTags(List<Tag> list) {

		return null;
	}

	public Tag readByKey(String id) {

		return null;
	}

	public void update(Tag object) {

	}

	public void delete(Tag object) {

	}

	public List<Tag> getAll() {

		return null;
	}

	public static void main(String[] args) {
		TagDAOImpl obj = new TagDAOImpl();
		obj.create(new Tag("#Hello1"));
		List<Tag> list = obj.getListTagsByPrefix("#Hello1");
		System.out.println(list.size());
	}

}