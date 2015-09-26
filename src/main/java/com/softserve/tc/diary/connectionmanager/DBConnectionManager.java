package com.softserve.tc.diary.connectionmanager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.softserve.tc.diary.log.Log;

public final class DBConnectionManager extends ConnectionManager {

	private static final BasicDataSource dataSource = new BasicDataSource();
	private static DBConnectionManager dbConnectionManager = null;
	private static Logger logger = Log.init(DBConnectionManager.class.toString());

	private DBConnectionManager() {
		
	}

	public static ConnectionManager getInstance() {
		if (dbConnectionManager == null) {
			dbConnectionManager = new DBConnectionManager();
			dataSource.setDriverClassName("org.postgresql.Driver");
			dataSource.setUrl("jdbc:postgresql://localhost:5432/Diary");
			dataSource.setUsername("root");
			dataSource.setPassword("root");
		}
		return dbConnectionManager;
	}

	public Connection getConnection() throws SQLException {

		logger.info("Number of active connections: " + dataSource.getNumActive());

		return dataSource.getConnection();
	}

	public void close() throws Exception {

		dataSource.close();
		logger.info("Connections are closed.");
	}
}
