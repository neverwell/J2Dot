package com.nemo.game.entity.sys;

import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobTreasureData extends AbstractSysData {

    @Tag(1)
    private long id;


    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}
