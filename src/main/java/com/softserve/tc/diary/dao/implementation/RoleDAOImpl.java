package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.RoleDAO;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.log.Log;

public class RoleDAOImpl implements RoleDAO, BaseDAO<Role>{

	private static Connection conn;
	private static PreparedStatement ps;
	private Logger logger = Log.init(this.getClass().getName());
	
	private static void getConnection() {
		try {
			conn = ConnectManager.getConnectionToTestDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void create(Role object) {
		getConnection();
		logger.debug("Creating role");
		try {
			ps = conn.prepareStatement("insert into role values(?,?);");
			ps.setString(1, UUID.randomUUID().toString());
			ps.setString(2, object.getName());
			ps.execute();
			logger.debug("Role created");
		} catch (SQLException e) {
		        logger.error("Creating role failed" ,e);
		}

	}

	public Role readByKey(String id) { // id as NAME
		Role role = new Role();
		logger.debug("read by key");
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
			logger.error("select failed in readyByKey", e);
		}
		return role;
	}

	public void update(Role object) {
	        logger.debug("updating role");
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("update role set name=?;");
			ps.setString(1, object.getName());
			ps.execute();
			ps.close();
			logger.debug("role updated");
		} catch (SQLException e) {
			logger.error("updated failed", e);
		}

	}

	public void delete(Role object) {
	        logger.debug("deleting role");
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("delete from role where role_id=?");
			ps.setString(1, object.getId());
			ps.execute();
			logger.debug("role deleted");
		} catch (SQLException e) {
			logger.error("delete failed", e);
		}
	}

	public List<Role> getAll() {
		List<Role> list = new ArrayList<Role>();
		logger.debug("Get all role records");
		try {
			conn = ConnectManager.getConnectionToTestDB();
			ps = conn.prepareStatement("select * from role");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Role(rs.getString("name")));
			}
		} catch (SQLException e) {
			logger.error("selest all failed", e);
		}
		return list;
	}

	public List<Role> findByRole(String role) {
		// TODO Auto-generated method stub
		return null;
	}

}
