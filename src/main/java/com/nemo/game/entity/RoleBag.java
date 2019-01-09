package com.nemo.game.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import io.protostuff.Exclude;
import io.protostuff.Tag;
import lombok.Data;

//背包
@Data
public class RoleBag implements Persistable{

    @Exclude
    private long touchTime;

    @Exclude
    private boolean dirty = false;

    @Tag(1)
    private long id;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int dataType() {
        return DataType.BAG;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
