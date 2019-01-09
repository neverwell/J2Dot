package com.nemo.game.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.system.count.entity.Count;
import io.protostuff.Exclude;
import io.protostuff.Tag;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class RoleCount implements Persistable {

    @Exclude
    private long touchTime;

    @Exclude
    private boolean dirty = false;

    @Tag(1)
    private long id;

    @Tag(2)
    private Map<String, Count> countMap = new ConcurrentHashMap<>();

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
    public int dataType() {
        return DataType.COUNT;
    }
}
