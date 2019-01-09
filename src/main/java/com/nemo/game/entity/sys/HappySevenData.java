package com.nemo.game.entity.sys;

import com.google.common.collect.Maps;
import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

//欢乐7天
@Getter
@Setter
public class HappySevenData extends AbstractSysData {
    @Tag(1)
    private long id;

    @Tag(2)
    private Map<Long, Map<Integer, Integer>> happySevenCompleteMap = Maps.newConcurrentMap();

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}
