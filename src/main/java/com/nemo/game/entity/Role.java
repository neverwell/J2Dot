package com.nemo.game.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.system.role.entity.RoleBasic;
import io.protostuff.Exclude;
import io.protostuff.Tag;
import lombok.Data;

//角色数据
@Data
public class Role implements Persistable {

    @Exclude
    private long touchTime;

    //基础数据
    @Tag(1)
    private RoleBasic basic = new RoleBasic();

    @Exclude
    private boolean dirty = false;

    @Override
    public long getId() {
        return this.getBasic().getId();
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
    public int dataType() {
        return DataType.ROLE;
    }

    public String getName() {
        return this.getBasic().getName();
    }
}
