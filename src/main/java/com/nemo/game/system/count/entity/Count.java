package com.nemo.game.system.count.entity;

import io.protostuff.Tag;
import lombok.Data;

@Data
public class Count {
    //计数类型
    @Tag(1)
    private int type;
    //计数的key
    @Tag(2)
    private long key;
    //当前计数
    @Tag(3)
    private int count;
    //重置类型
    @Tag(4)
    private int resetType;
    //本周期第一次更新时间
    @Tag(5)
    private int updateTime;
}
