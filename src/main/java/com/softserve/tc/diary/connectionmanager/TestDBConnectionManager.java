package com.softserve.tc.diary.connectionmanager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.softserve.tc.diary.log.Log;

public class TestDBConnectionManager extends ConnectionManager {
    
    private static final BasicDataSource dataSourceTestDB =
            new BasicDataSource();
    
	private static TestDBConnectionManager testDBConnectionManagerInstance = null;
    private static Logger logger = Log.init(TestDBConnectionManager.class.toString());
    
    private TestDBConnectionManager() {
    
    }
    
	public static TestDBConnectionManager getInstance() {
		if (testDBConnectionManagerInstance == null) {
			testDBConnectionManagerInstance = new TestDBConnectionManager();
	        dataSourceTestDB.setDriverClassName("org.postgresql.Driver");
	        dataSourceTestDB
	                .setUrl("jdbc:postgresql://127.11.237.2:5432/diarytest");
	        dataSourceTestDB.setUsername("root");
	        dataSourceTestDB.setPassword("root");
		}
		return testDBConnectionManagerInstance;
	}
    
    public Connection getConnection() throws SQLException {
        
        logger.info("Number of active connections: "
                + dataSourceTestDB.getNumActive());
                
        return dataSourceTestDB.getConnection();
    }
    
    public void close() throws Exception {
        dataSourceTestDB.close();
    }
    
}