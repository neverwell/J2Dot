package com.nemo.game.data.mysql.factory;

import com.nemo.common.persist.PersistFactory;
import com.nemo.common.persist.Persistable;
import com.nemo.game.back.entity.Announce;
import com.nemo.game.data.DataType;

//公告持久化工厂
public class AnnouncePersistFactory implements PersistFactory {
    private static final String INSERT = "INSERT INTO s_announce (id, uniqueId,startTime, endTime, period, type,content) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE  s_announce SET uniqueId = ?,startTime = ?, endTime = ?, period = ?, type = ?,content = ? WHERE id = ?";

    private static final String DELETE = "DELETE FROM s_announce WHERE id = ?";

    @Override
    public String name() {
        return "定时公告";
    }

    @Override
    public int dataType() {
        return DataType.ANNOUNCE;
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
        Announce announce = (Announce) obj;
        return new Object[]{announce.getId(), announce.getUniqueId(), announce.getStarTime(), announce.getEndTime(), announce.getPeriod(), announce.getType(), announce.getContent()};
    }

    @Override
    public Object[] createUpdateParameters(Persistable obj) {
        Announce announce = (Announce) obj;
        return new Object[]{announce.getUniqueId(), announce.getStarTime(), announce.getEndTime(), announce.getPeriod(), announce.getType(), announce.getContent(), announce.getId()};
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
