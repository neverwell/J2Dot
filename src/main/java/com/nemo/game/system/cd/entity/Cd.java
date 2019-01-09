package com.nemo.game.system.cd.entity;

import lombok.Data;

@Data
public class Cd {
    //CD类型
    private int type;
    //CD的key 比如技能id等 是用来区别同类型的字段
    private int key;
    //CD到期时间
    private long endTime;
}
