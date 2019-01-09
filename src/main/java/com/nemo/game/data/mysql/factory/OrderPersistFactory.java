package com.nemo.game.data.mysql.factory;

import com.nemo.common.persist.PersistFactory;
import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.system.recharge.entity.Order;

//订单数据持久化工厂
public class OrderPersistFactory implements PersistFactory {
    private static final String INSERT = "INSERT INTO s_order (id, orderId, uid, loginName,`client`, roleName, money,yuanbao,bindYuanbao, " +
            "`time`, qudao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";

    @Override
    public String name() {
        return "订单";
    }

    @Override
    public int dataType() {
        return DataType.ORDER;
    }

    @Override
    public String createInsertSql() {
        return INSERT;
    }

    @Override
    public String createUpdateSql() {
        return "";
    }

    @Override
    public String createDeleteSql() {
        return "";
    }

    @Override
    public Object[] createInsertParameters(Persistable obj) {
        Order order = (Order) obj;
        return new Object[]{order.getId(), order.getOrderSn(), order.getUid(), order.getLoginName(), order.getClient(), order.getRoleName(), order.getMoney(), order.getYuanbao(), order.getBindYuanbao(), order.getTime(), order.getQudao()};
    }

    @Override
    public Object[] createUpdateParameters(Persistable obj) {
        return null;
    }

    @Override
    public Object[] createDeleteParameters(Persistable obj) {
        return null;
    }

    @Override
    public long taskPeriod() {
        return 1000;
    }
}
