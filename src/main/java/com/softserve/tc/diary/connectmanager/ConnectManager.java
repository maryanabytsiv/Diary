package com.softserve.tc.diary.connectmanager;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectManager implements AutoCloseable {
    
    public static Connection getConnection() throws SQLException {
        return null;
    }
    
}
