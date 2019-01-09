package com.nemo.common.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
    Connection getConnection() throws SQLException;

    void release(Connection var1) throws SQLException;
}
