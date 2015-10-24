package com.softserve.tc.diary.connectionmanager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.softserve.tc.diary.log.Log;

public class ConnectionManager implements AutoCloseable {

	private static final BasicDataSource dataSource = new BasicDataSource();
	private static ConnectionManager dbConnectionManager = null;
	private static Logger logger = Log.init(ConnectionManager.class.toString());

	private ConnectionManager() {
	}
	
	public static ConnectionManager getInstance(ConnectionPath dataBase) {

		if (dbConnectionManager == null) {
			InputStream inputStream = null;
			Properties property = new Properties();
			dbConnectionManager = new ConnectionManager();
			try {
				
				String pathToDB = dataBase.getPath();
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				inputStream = classloader.getResourceAsStream(pathToDB);

				logger.info("Creating connection to PostgreSQL from file: " + pathToDB);
				property.load(inputStream);

			} catch (IOException err) {
				logger.error("connection failed", err);
			}
			dataSource.setDriverClassName(property.getProperty("driver"));
			dataSource.setUrl(property.getProperty("Url"));
			dataSource.setUsername(property.getProperty("UserName"));
			dataSource.setPassword(property.getProperty("Password"));
		}
		return dbConnectionManager;
	}

	public Connection getConnection() {
		logger.info(
				"Number of active connections: " + dataSource.getNumActive());
		try {
			return  dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Can't get connection to database. ", e);
		}
		return null;
	}

	@Override
	public void close() throws Exception {
		dataSource.close();
		logger.info("Connections are closed.");
	}

}
