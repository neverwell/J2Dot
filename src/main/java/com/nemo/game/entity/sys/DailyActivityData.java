package com.nemo.game.entity.sys;

import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

//日常活动数据
@Getter
@Setter
public class DailyActivityData extends AbstractSysData {

    @Tag(1)
    private long id;

    //上次活动开放时间 日常活动时间表类型，时间毫秒数
    @Tag(2)
    private Map<Integer, Long> lastOpenTime = new HashMap<>();

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
