package com.nemo.common.jdbc;

import io.protostuff.ProtostuffIOUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProtostuffRowMapper<T> implements RowMapper<T> {
    private Class<T> clazz;

    public ProtostuffRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T mapping(ResultSet rs) throws SQLException {
        byte[] bytes = rs.getBytes(1);
        return SerializerUtil.decode(bytes, this.clazz);
    }

    @Override
    public void release() {
    }
}
