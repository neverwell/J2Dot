package com.nemo.game.back.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import io.protostuff.Exclude;
import lombok.Data;

@Data
public class Announce implements Persistable{
    //id
    private long id;

    private int uniqueId;
    //开始时间
    private int starTime;
    //结束时间
    private int endTime;
    //间隔
    private int period;
    //公告类型
    private int type;
    //公告内容
    private String content;

    @Exclude
    private boolean dirty;

    @Override
    public long getId() {
        return id;
    }

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
        return DataType.ANNOUNCE;
    }
}
