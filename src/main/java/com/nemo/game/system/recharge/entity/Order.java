package com.nemo.game.system.recharge.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import lombok.Data;

@Data
public class Order implements Persistable{

    private boolean dirty = false;

    private long id;

    private long uid;

    private String orderSn;

    private String loginName;

    private String roleName;

    private int money;

    private int yuanbao;

    private int bindYuanbao;

    private int time;

    //默认 1ios 2 android 3 web 特殊会传appId
    private int client;

    private int qudao;

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean b) {
        this.dirty = b;
    }

    @Override
    public long getTouchTime() {
        return 0;
    }

    @Override
    public int dataType() {
        return DataType.ORDER;
    }
}
