package com.nemo.game.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import lombok.Data;

//排行
@Data
public class RoleRank implements Persistable{
    private boolean dirty;

    private long id;

    private String name;

    private int roleLevel;

    private int roleRein;

    private long roleExp;

    private int sex;

    private int career;

    private long fightPower;

    private long heroFightPower;

    private int junxian;

    private long honor;

    private long wingFightPower;

    private long weiwang;

    private int barrier;

    private int vipLevel;

    private int searchPk;

    private int weiwangLevel;

    private int lastLoginTime;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public long getTouchTime() {
        return 0;
    }

    @Override
    public int dataType() {
        return DataType.RANK;
    }

    @Override
    public String toString() {
        return String.format("[%d][%s]", this.id, this.name);
    }
}
