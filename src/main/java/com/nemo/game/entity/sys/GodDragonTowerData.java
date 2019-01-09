package com.nemo.game.entity.sys;

import io.protostuff.Tag;

public class GodDragonTowerData extends AbstractSysData {
    @Tag(1)
    private long id;

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
