package com.nemo.game.data.mysql.mapper;

import com.nemo.common.jdbc.RowMapper;
import com.nemo.game.entity.User;
import com.nemo.game.system.user.field.UserField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User>{
	@Override
	public User mapping(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(UserField.ID));
        user.setLoginName(rs.getString(UserField.LOGIN_NAME));
        user.setRoleName(rs.getString(UserField.ROLE_NAME));
        user.setSid(rs.getInt(UserField.SID));
        user.setPid(rs.getInt(UserField.PID));
        user.setIp(rs.getString(UserField.IP));
        user.setType(rs.getInt(UserField.TYPE));
        user.setIdNumber(rs.getString(UserField.ID_NUMBER));
        user.setClient(rs.getInt(UserField.CLIENT));
        user.setRegTime(rs.getInt(UserField.REG_TIME));
        user.setQudao(rs.getInt(UserField.QUDAO));
        user.setChannel(rs.getString(UserField.CHANNEL));
        return user;
	}

	@Override
	public void release() {
	}
}
