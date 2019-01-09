package com.nemo.game.entity;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.system.union.entity.MemberInfo;
import io.protostuff.Exclude;
import io.protostuff.Tag;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//帮会数据
@Data
public class Union implements Persistable{
    @Exclude
    private boolean dirty;

    //使用 IDUtil.getId生成一个唯一id
    @Tag(1)
    private long id;




    //帮会成员信息
    @Tag(4)
    private Map<Long, MemberInfo> memberInfos = new ConcurrentHashMap<>();

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
        return DataType.SYS_UNION;
    }

    @Override
    public long getId() {
        return id;
    }

}
