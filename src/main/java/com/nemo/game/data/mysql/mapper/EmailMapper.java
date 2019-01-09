package com.nemo.game.data.mysql.mapper;

import com.nemo.common.jdbc.RowMapper;
import com.nemo.game.system.email.constant.EmailField;
import com.nemo.game.system.email.entity.Email;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 汪柄锐 <wangbingruia@qq.com>
 * @version 创建时间：2017年12月26日 15:09
 */
public class EmailMapper implements RowMapper<Email> {
    @Override
    public Email mapping(ResultSet rs) throws SQLException {
        Email email = new Email();
        email.setEmailId(rs.getLong(EmailField.ID));
        email.setOwnerId(rs.getLong(EmailField.UID));
        email.setState(rs.getInt(EmailField.STATE));
        email.setTitle(rs.getString(EmailField.TITLE));
        email.setDesc(rs.getString(EmailField.CONTENT));
        email.setItems(rs.getString(EmailField.ITEMS));
        email.setTime(rs.getInt(EmailField.TIME));
        email.setSender(rs.getString(EmailField.SENDER));
        return email;
    }

    @Override
    public void release() {

    }
}
