package com.nemo.game.entity.sys;

import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

//王者禁地系统数据
@Getter
@Setter
public class KingForbiddenData extends AbstractSysData {

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
