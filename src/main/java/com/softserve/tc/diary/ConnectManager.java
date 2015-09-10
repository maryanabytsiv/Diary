//package com.softserve.tc.diary;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
//
///**
// * @author Andrii Shupta
// *
// */
//public final class ConnectManager {
//	public static final BasicDataSource dataSource = new BasicDataSource();
//	static{
//		dataSource.setDriverClassName("com.postgres.Driver");
//		dataSource.setUrl("jdbc:postgresql://localhost:5432/diary");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("password"); 
//	}
//	private ConnectManager(){
//		//
//	}
//	
//	public static Connection getConnection() throws SQLException{
//       return dataSource.getConnection();
//    }
//}
//
