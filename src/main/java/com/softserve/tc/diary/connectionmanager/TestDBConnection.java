package com.softserve.tc.diary.connectionmanager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.softserve.tc.log.Log;

public class TestDBConnection extends ConnectionManager {
    
    private static final BasicDataSource dataSourceTestDB =
            new BasicDataSource();
            
    private static Logger logger = Log.init(TestDBConnection.class.toString());
    
    private TestDBConnection() {
    
    }
    
    static {
        dataSourceTestDB.setDriverClassName("org.postgresql.Driver");
        dataSourceTestDB
                .setUrl("jdbc:postgresql://127.11.237.2:5432/diarytest");
        dataSourceTestDB.setUsername("root");
        dataSourceTestDB.setPassword("root");
        
        // dataSourceTestDB.setMaxActive(100);
    }
    
    public static Connection getConnection() throws SQLException {
        
        logger.info("Number of active connections: "
                + dataSourceTestDB.getNumActive());
                
        return dataSourceTestDB.getConnection();
    }
    
    public void close() throws Exception {
        dataSourceTestDB.close();
    }
    
}
