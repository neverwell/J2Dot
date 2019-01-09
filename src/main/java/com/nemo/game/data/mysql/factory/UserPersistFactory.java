package com.nemo.game.data.mysql.factory;

import com.nemo.common.persist.PersistFactory;
import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.entity.User;

//用户数据持久化工厂
public class UserPersistFactory implements PersistFactory{
    private static final String INSERT = "INSERT INTO p_user (id, loginName, roleName, sid, pid, ip, client, type, IDNumber, " +
            "regTime,qudao,channel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)";

    private static final String UPDATE = "UPDATE p_user SET roleName=?, client = ?, ip = ?, type = ?, IDNumber = ? , qudao= ?, channel= ? WHERE id = ?";

    private static final String DELETE = "DELETE FROM p_user WHERE id = ?";

    @Override
    public String name() {
        return null;
    }

    @Override
    public int dataType() {
        return DataType.USER;
    }

    @Override
    public String createInsertSql() {
        return INSERT;
    }

    @Override
    public String createUpdateSql() {
        return UPDATE;
    }

    @Override
    public String createDeleteSql() {
        return DELETE;
    }

    @Override
    public Object[] createInsertParameters(Persistable obj) {
        User user = (User) obj;
        return new Object[]{user.getId(), user.getLoginName(), user.getRoleName(), user.getSid(), user.getPid(), user.getIp(), user.getClient(), user.getType(), user.getIdNumber(), user.getRegTime(), user.getQudao(), user.getChannel()};
    }

    @Override
    public Object[] createUpdateParameters(Persistable obj) {
        User user = (User) obj;
        return new Object[]{user.getRoleName(), user.getClient(), user.getIp(), user.getType(), user.getIdNumber(), user.getQudao(), user.getChannel(), user.getId()};
    }

    @Override
    public Object[] createDeleteParameters(Persistable obj) {
        return new Object[]{obj.getId()};
    }

    @Override
    public long taskPeriod() {
        return 60 * 1000;
    }
}
