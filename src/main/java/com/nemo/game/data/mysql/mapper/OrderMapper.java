package com.nemo.game.data.mysql.mapper;

import com.nemo.common.jdbc.RowMapper;
import com.nemo.game.system.recharge.constant.OrderField;
import com.nemo.game.system.recharge.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapping(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong(OrderField.ID));
        order.setUid(rs.getLong(OrderField.UID));
        order.setLoginName(rs.getString(OrderField.LOGIN_NAME));
        order.setRoleName(rs.getString(OrderField.ROLE_NAME));
        order.setMoney(rs.getInt(OrderField.MONEY));
        order.setYuanbao(rs.getInt(OrderField.YUAN_BAO));
        order.setBindYuanbao(rs.getInt(OrderField.BIND_YUAN_BAO));
        order.setTime(rs.getInt(OrderField.TIME));
        order.setOrderSn(rs.getString(OrderField.ORDER_SN));
        order.setClient(rs.getInt(OrderField.CLIENT));
        order.setQudao(rs.getInt(OrderField.QUDAO));
        return order;
    }

    @Override
    public void release() {
    }
}
