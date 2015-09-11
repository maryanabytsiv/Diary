package com.softserve.tc.diary;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author Andrii Shupta
 *
 */
public final class ConnectManager {
	public static final BasicDataSource dataSource = new BasicDataSource();
	public static final BasicDataSource dataSourceTestDB = new BasicDataSource();

	static {
		dataSource.setDriverClassName("com.postgres.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/diary");
		dataSource.setUsername("root");
		dataSource.setPassword("root");

		dataSourceTestDB.setDriverClassName("com.postgres.Driver");
		dataSourceTestDB.setUrl("jdbc:postgresql://localhost:5432/diarytest");
		dataSourceTestDB.setUsername("root");
		dataSourceTestDB.setPassword("root");
	}

	private ConnectManager() {
		//
	}

	public static Connection getConnectionToTestDB() throws SQLException {
		return dataSourceTestDB.getConnection();
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public static void closeConnectionToTestDB() throws SQLException{
		dataSourceTestDB.close();
	}

	public static void closeConnection() throws SQLException{
		dataSource.close();
	}
	
}
