package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
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

	static {
		try {
			conn = ConnectManager.getConnectionToTestDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getGeneratedId() {
		UUID idOne = UUID.randomUUID();
		return idOne.toString();
	}

	public void create(Tag tagObject) {
		try {
			if (conn.isClosed()) {
				conn = ConnectManager.getConnectionToTestDB();
			}
			ps = conn.prepareStatement("insert into tag values(?,?);");
			ps.setString(1, getGeneratedId());
			ps.setString(2, tagObject.getTagMessage());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Tag object) {
		
	}

	public void delete(Tag object) {
		String tagMessage = object.getTagMessage();
		String query = "DELETE FROM tag WHERE tag_message Like '" + tagMessage + "';";
		try {
			if (conn.isClosed()) {
				conn = ConnectManager.getConnectionToTestDB();
			}
			ps = conn.prepareStatement(query);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Tag> getListTagsByPrefix(String prefix) {
		List<Tag> list = new ArrayList<Tag>();
		try {
			if (conn.isClosed()) {
				conn = ConnectManager.getConnectionToTestDB();
			}
			String query = "SELECT tag_message FROM tag " + "WHERE tag_message LIKE '" + prefix + "%';";
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Tag(rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Tag> getListTagsBySuffix(String suffix) {
		List<Tag> list = new ArrayList<Tag>();
		try {
			if (conn.isClosed()) {
				conn = ConnectManager.getConnectionToTestDB();
			}
			String query = "SELECT tag_message FROM tag " + "WHERE tag_message LIKE '%" + suffix + "%';";
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Tag(rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Record> getListRecordsByTag() {
		
		return null;
	}

	public List<Record> getListRecordsByListOfTags(List<Tag> list) {

		return null;
	}

	public Tag readByKey(String id) {

		return null;
	}

	public List<Tag> getAll() {
		List<Tag> list = new ArrayList<Tag>();
		String query = "SELECT tag_message FROM tag;";
		try {
			if (conn.isClosed()) {
				conn = ConnectManager.getConnectionToTestDB();
			}
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Tag(rs.getString("tag_message")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) throws SQLException {
		TagDAOImpl obj = new TagDAOImpl();
		obj.create(new Tag("#Hello1"));
		List<Tag> list = obj.getListTagsByPrefix("#Hello1");
		String s = "insert into tag values('2242','Hel');";
		ps = conn.prepareStatement(s);
		ps.execute();
		System.out.println(list.size());
		list = obj.getListTagsBySuffix("Hel");
		System.out.println(list.size());
	}
}