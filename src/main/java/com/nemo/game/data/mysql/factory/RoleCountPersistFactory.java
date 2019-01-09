package com.nemo.game.data.mysql.factory;

import com.nemo.common.jdbc.SerializerUtil;
import com.nemo.common.persist.PersistFactory;
import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.entity.RoleCount;
import lombok.extern.slf4j.Slf4j;

//玩家计数数据持久化工厂
@Slf4j
public class RoleCountPersistFactory implements PersistFactory {
    private static final String INSERT = "insert into p_count (id, data) values (?, ?)";

    private static final String UPDATE = "update p_count set data = ? where id = ?";

    private static final String DELETE = "delete from p_count where id = ?";

    @Override
    public String name() {
        return null;
    }

    @Override
    public int dataType() {
        return DataType.COUNT;
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
        RoleCount roleCount = (RoleCount) obj;

        byte[] bytes = null;
        int retry = 0;
        while (retry < 10) {
            try {
                bytes = SerializerUtil.encode(roleCount, RoleCount.class);
                break;
            } catch (Throwable e) {
                retry++;
                log.error("数据" + roleCount.getId() + "入库序列化失败,进行重试，重试次数->" + retry, e);
            }
        }
        if (retry > 1 && retry < 10) {
            log.error("数据{}入库序列化重试次数->{}", roleCount.getId(), retry);
        } else if (retry == 10) {
            log.error("数据{}入库失败", roleCount.getId());
        }
        if (bytes == null) {
            log.error("Protostuff编码错误,data:{}" + roleCount.getId());
            return null;
        }

        return new Object[]{obj.getId(), bytes};
    }

    @Override
    public Object[] createUpdateParameters(Persistable obj) {
        RoleCount roleCount = (RoleCount) obj;
        byte[] bytes = null;
        int retry = 0;
        while (retry < 10) {
            try {
                bytes = SerializerUtil.encode(roleCount, RoleCount.class);
                break;
            } catch (Throwable e) {
                retry++;
                log.error("数据" + roleCount.getId() + "入库序列化失败,进行重试，重试次数->" + retry, e);
            }
        }
        if (retry > 1 && retry < 10) {
            log.error("数据{}入库序列化重试次数->{}", roleCount.getId(), retry);
        } else if (retry == 10) {
            log.error("数据{}入库失败", roleCount.getId());
        }
        if (bytes == null) {
            log.error("Protostuff编码错误,data:{}" + roleCount.getId());
            return null;
        }
        return new Object[]{bytes, obj.getId()};
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
