package com.softserve.tc.diary.connectionmanager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.softserve.tc.diary.log.Log;

public final class DBConnectionManagerNew extends ConnectionManager {
    private static final BasicDataSource dataSource = new BasicDataSource();
    private static DBConnectionManagerNew dbConnectionManager = null;
    private static Logger logger =
            Log.init(DBConnectionManagerNew.class.toString());
            
    private DBConnectionManagerNew() {
    }
    
    public static ConnectionManager getInstance(boolean isRealDB) {
        
        InputStream is = null;
        Properties property = new Properties();
        
        if (dbConnectionManager == null) {
            dbConnectionManager = new DBConnectionManagerNew();
            try {
                ClassLoader classloader =
                        Thread.currentThread().getContextClassLoader();
                if (isRealDB) {
                    is = classloader
                            .getResourceAsStream("realDB.properties");
                    logger.info("Creating RealDB DBConnectionManagerNew");
                } else {
                    is = classloader
                            .getResourceAsStream("testDB.properties");
                            
                    logger.info("Creating TestDB DBConnectionManagerNew");
                }
                property.load(is);
                
            } catch (IOException err) {
                logger.error("connection failed", err);
            }
            dataSource.setDriverClassName(property.getProperty("driver"));
            dataSource.setUrl(property.getProperty("Url"));
            dataSource.setUsername(property.getProperty("UserName"));
            dataSource.setPassword(property.getProperty("Password"));
        }
        //
        // read from propsDB
        // or
        // read from propsDBTest
        
        //
        //
        // dataSource.setUsername("root");
        // dataSource.setPassword("root");
        //
        
        // ;
        // dataSource.setDriverClassName("org.postgresql.Driver");
        // if (isRealDB) {
        //
        // dataSource.setUrl("jdbc:postgresql://localhost:5432/Diary");
        //
        // } else {
        // logger.info("Creating TestDB DBConnectionManagerNew");
        // dataSource.setUrl(
        // "jdbc:postgresql://127.11.237.2:5432/diarytest");
        // }
        //
        // dataSource.setUsername("root");
        // dataSource.setPassword("root");
        // }
        
        return dbConnectionManager;
    }
    
    public Connection getConnection() throws SQLException {
        
        logger.info(
                "Number of active connections: " + dataSource.getNumActive());
                
        return dataSource.getConnection();
    }
    
    @Override
    public void close() throws Exception {
        dataSource.close();
        logger.info("Connections are closed.");
    }
    
}
