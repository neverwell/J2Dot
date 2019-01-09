package com.nemo.game.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import lombok.Data;

@Data
public class User implements Persistable{
    private boolean dirty;
    //唯一性id
    private long id;
    //登录名
    private String loginName;
    //角色名
    private String roleName;
    //服务器id
    private int sid;
    //平台id
    private int pid;
    //注册时间
    private int regTime;
    //登录的客户端
    private int client;
    //类型 0普通玩家 >0拥有不同gm权限的玩家
    private int type;
    //身份证号码
    private String idNumber;
    //ip
    private String ip;
    //渠道来源
    private int qudao;
    //平台渠道
    private String channel;

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
    public long getTouchTime() {
        return 0;
    }

    @Override
    public int dataType() {
        return DataType.USER;
    }

    @Override
    public String toString() {
        return String.format("[%d][%s]", this.id, this.loginName);
    }
}
