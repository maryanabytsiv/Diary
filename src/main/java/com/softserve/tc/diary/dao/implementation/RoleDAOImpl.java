package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RoleDAO;
import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.Tag;

public class RoleDAOImpl implements RoleDAO, BaseDAO<Role>, IdGenerator {

	private static Connection conn;
	private static PreparedStatement ps;

	private static void getConnection() {
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

	public void create(Role object) {
		// TODO Auto-generated method stub
		getConnection();

		try {
			ps = conn.prepareStatement("insert into role values(?,?);");
			ps.setString(1, getGeneratedId());
			ps.setString(2, object.getName());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Role readByKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(Role object) {
		// TODO Auto-generated method stub

	}

	public void delete(Role object) {
		// TODO Auto-generated method stub

	}

	public List<Role> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Role> findByRole(String role) {
		// TODO Auto-generated method stub
		return null;
	}

}
