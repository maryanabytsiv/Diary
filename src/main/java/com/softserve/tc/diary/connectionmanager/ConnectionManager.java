package com.softserve.tc.diary.connectionmanager;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionManager implements AutoCloseable {
    
    public static Connection getConnection() throws SQLException {
        return null;
    }
}
