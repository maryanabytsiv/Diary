/**
 * 
 */
package com.softserve.tc.diary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Andrii Shupta
 *
 */
public class ConnectManager {
	public static Connection getConnection() {
        String jdbcUrl = "jdbc:postgresql://localhost:3310/ok";
        String user = "root";
        String password = "password";
        Connection conn = null;

        try {
            Class.forName("com.postgres.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}