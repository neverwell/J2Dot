package com.nemo.common.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//对象映射
public interface RowMapper<T> {
    T mapping(ResultSet var1) throws SQLException;

    void release();

    class MapRowMapper implements RowMapper<Map<String, Object>> {
        private ThreadLocal<ResultSetMetaData> threadLocal = new ThreadLocal<>();

        public MapRowMapper() {
        }

        @Override
        public Map<String, Object> mapping(ResultSet rs) throws SQLException {
            Map<String, Object> ret = new HashMap<>();
            ResultSetMetaData rsmd = this.threadLocal.get();
            if(rsmd == null) {
                rsmd = rs.getMetaData();
                this.threadLocal.set(rsmd);
            }

            int column = rsmd.getColumnCount();
            for(int i = 1; i <= column; ++i) {
                ret.put(rsmd.getColumnName(i), rs.getObject(i));
            }

            return ret;
        }

        @Override
        public void release() {
            this.threadLocal.remove();
        }
    }

    class ByteArrayRowMaper implements RowMapper<byte[]> {
        public ByteArrayRowMaper() {
        }

        public byte[] mapping(ResultSet rs) throws SQLException {
            return rs.getBytes(1);
        }

        public void release() {
        }
    }

    class DateRowMaper implements RowMapper<Date> {
        public DateRowMaper() {
        }

        public Date mapping(ResultSet rs) throws SQLException {
            return rs.getDate(1);
        }

        public void release() {
        }
    }

    class StringRowMapper implements RowMapper<String> {
        public StringRowMapper() {
        }

        public String mapping(ResultSet rs) throws SQLException {
            return rs.getString(1);
        }

        public void release() {
        }
    }

    class LongRowMapper implements RowMapper<Long> {
        public LongRowMapper() {
        }

        public Long mapping(ResultSet rs) throws SQLException {
            return rs.getLong(1);
        }

        public void release() {
        }
    }

    class IntegerRowMaper implements RowMapper<Integer> {
        public IntegerRowMaper() {
        }

        public Integer mapping(ResultSet rs) throws SQLException {
            return rs.getInt(1);
        }

        public void release() {
        }
    }
}
