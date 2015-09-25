package com.softserve.tc.diary.connectionmanager;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionManager implements AutoCloseable {
    
    public Connection getConnection() throws SQLException {
        return null;
    }
}
