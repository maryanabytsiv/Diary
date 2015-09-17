package com.softserve.tc.diary.connectmanager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.softserve.tc.log.Log;

public class TestDBConnection extends ConnectManager {

	private static final BasicDataSource dataSourceTestDB = new BasicDataSource();

	private static Logger logger = Log.init(TestDBConnection.class.toString());

	private TestDBConnection() {

	}

	static {
		dataSourceTestDB.setDriverClassName("org.postgresql.Driver");
		dataSourceTestDB.setUrl("jdbc:postgresql://localhost:5432/DiaryTest");
		dataSourceTestDB.setUsername("root");
		dataSourceTestDB.setPassword("root");

		// dataSourceTestDB.setMaxActive(100);
	}

	public static Connection getConnection() throws SQLException {

		logger.info("Number of active connections: " + dataSourceTestDB.getNumActive());

		return dataSourceTestDB.getConnection();
	}

	public void close() throws Exception {
		dataSourceTestDB.close();
	}

}
