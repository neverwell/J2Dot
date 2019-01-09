package com.nemo.common.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);
    public static final RowMapper<Integer> INT_MAPPER = new RowMapper.IntegerRowMaper();
    public static final RowMapper<Long> LONG_MAPPER = new RowMapper.LongRowMapper();
    public static final RowMapper<String> STRING_MAPPER = new RowMapper.StringRowMapper();
    public static final RowMapper<Date> DATE_MAPPER = new RowMapper.DateRowMaper();
    public static final RowMapper<byte[]> BYTEARRAY_MAPPER = new RowMapper.ByteArrayRowMaper();
    public static final RowMapper<Map<String, Object>> MAP_MAPPER = new RowMapper.MapRowMapper();
    private ConnectionPool pool;

    public JdbcTemplate(ConnectionPool pool) throws Exception {
        this.pool = pool;
    }

    public <T> T query(String sql, RowMapper<T> mapper, Object... parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = this.pool.getConnection();
            pstmt = conn.prepareStatement(sql);

            for(int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i+1, parameters[i]);
            }

            rs = pstmt.executeQuery();
            if(rs.next()) {
                T t = mapper.mapping(rs);
                return t;
            }
        } catch (Exception e) {
            LOGGER.error("查询单条数据失败, sql：" + sql, e);
        } finally {
            this.release(conn, pstmt, rs, mapper);
        }
        return null;
    }

    public <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = this.pool.getConnection();
            pstmt = conn.prepareStatement(sql);

            for(int i = 0; i < parameters.length; ++i) {
                pstmt.setObject(i + 1, parameters[i]);
            }

            rs = pstmt.executeQuery();
            ArrayList<T> ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(mapper.mapping(rs));
            }

            return ret;
        } catch (Exception e) {
            LOGGER.error("查询多条数据失败,sql:" + sql, e);
        } finally {
            this.release(conn, pstmt, rs, mapper);
        }
        return Collections.emptyList();
    }

    public int update(String sql, Object[] parameters) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        byte b;
        try {
            conn = this.pool.getConnection();
            pstmt = conn.prepareStatement(sql);

            for(int i = 0; i < parameters.length; ++i) {
                pstmt.setObject(i+1, parameters[i]);
            }

            int ret = pstmt.executeUpdate();
            return ret;
        } catch (Exception e) {
            LOGGER.error("数据库更新失败,sql:" + sql, e);
            b = -1;
        } finally {
            this.release(conn, pstmt, null, null);
        }
        return b;
    }

    public void batchUpdate(String sql, List<Object[]> parameters) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = this.pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            Iterator<Object[]> var5 = parameters.iterator();

            while (var5.hasNext()) {
                Object[] parameterArray = var5.next();
                for(int i = 0; i < parameterArray.length; ++i) {
                    pstmt.setObject(i + 1, parameterArray[i]);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (Exception e) {
            LOGGER.error("批处理更新失败,sql:" + sql, e);
        } finally {
            this.release(conn, pstmt, null, null);
        }
    }

    private void release(Connection conn, PreparedStatement pstmt, ResultSet rs, RowMapper<?> mapper) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error("Result关闭出错 ", e);
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                LOGGER.error("PreparedStatement关闭出错。", e);
            }
        }

        if (conn != null) {
            try {
                this.pool.release(conn);
            } catch (SQLException e) {
                LOGGER.error("Connection关闭出错。", e);
            }
        }

        if (mapper != null) {
            try {
                mapper.release();
            } catch (Exception e) {
                LOGGER.error("Mapper释放出错。", e);
            }
        }
    }

    public ConnectionPool getPool() {
        return pool;
    }

    public void setPool(ConnectionPool pool) {
        this.pool = pool;
    }
}
