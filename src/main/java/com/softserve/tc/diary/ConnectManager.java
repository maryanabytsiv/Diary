
package com.softserve.tc.diary;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.softserve.tc.log.Log;

//import com.softserve.tc.log.Log;

/**
 * @author Andrii Shupta
 *
 */
public final class ConnectManager {
	public static final BasicDataSource dataSource = new BasicDataSource();
	public static final BasicDataSource dataSourceTestDB = new BasicDataSource();

	private static Logger logger = Log.init(ConnectManager.class.toString());

	static {
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/Diary");
		dataSource.setUsername("root");
		dataSource.setPassword("root");

		dataSourceTestDB.setDriverClassName("org.postgresql.Driver");
		dataSourceTestDB.setUrl("jdbc:postgresql://127.11.237.2:5432/diarytest");
		dataSourceTestDB.setUsername("root");
		dataSourceTestDB.setPassword("root");
		dataSourceTestDB.setMaxActive(100);
	}

	private ConnectManager() {
	}

	public static Connection getConnectionToTestDB() throws SQLException {
		logger.info("Number of active connections: " + dataSourceTestDB.getNumActive());
		return dataSourceTestDB.getConnection();
	}

	public static Connection getConnection() throws SQLException {
		logger.info("Number of active connections: " + dataSourceTestDB.getNumActive());
		return dataSource.getConnection();
	}

	public static void closeConnectionToTestDB() throws SQLException {
		dataSourceTestDB.close();
	}

	public static void closeConnection() throws SQLException {
		dataSource.close();
	}

}
