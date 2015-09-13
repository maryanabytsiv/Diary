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
import com.softserve.tc.diary.dao.RoleDAO;
import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.User;

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

	public Role readByKey(String id) { // id as NAME
		Role role = new Role();
		try {
			ps = conn.prepareStatement("select * from role where name =?;");
			ps.setString(1, role.getName());
			ResultSet rs = ps.executeQuery();
			if (!ps.executeQuery().next())
				return null;
			while (rs.next()) {
				role.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return role;
	}

	public void update(Role object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("update role set name=?;");
			ps.setString(1, object.getName());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void delete(Role object) {
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("delete from role where role_id=?");
			ps.setString(1, object.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Role> getAll() {
		List<Role> list = new ArrayList<Role>();
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("select * from role");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Role(rs.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Role> findByRole(String role) {
		// TODO Auto-generated method stub
		return null;
	}

}
