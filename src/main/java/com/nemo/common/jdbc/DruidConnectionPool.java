package com.nemo.common.jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

//数据库连接池
public class DruidConnectionPool implements ConnectionPool{
    //数据源 数据库的位置来源
    private DataSource ds;

    public DruidConnectionPool(String configFile) throws Exception {
        InputStream in = new FileInputStream(configFile);
        Properties props = new Properties();
        props.load(in);
        this.ds = DruidDataSourceFactory.createDataSource(props);
    }

    public DruidConnectionPool(InputStream in) throws Exception {
        Properties props = new Properties();
        props.load(in);
        this.ds = DruidDataSourceFactory.createDataSource(props);
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }

    @Override
    public void release(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public String toString() {
        return this.ds.toString();
    }
}
