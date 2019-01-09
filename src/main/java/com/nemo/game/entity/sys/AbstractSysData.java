package com.nemo.game.entity.sys;

import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import io.protostuff.Exclude;

//系统总数据
public abstract class AbstractSysData implements Persistable{

    //系统计数
    public static final long COUNT = 1;
    //其他一些乱七八糟的数据
    public static final long OTHER = 2;
    //关卡排行数据
    public static final long CHAPTER_RANK = 3;
    //系统活动数据
    public static final long SYS_ACTIVITY_DATA = 4;
    //天梯相关系统数据
//    public static final long LADDER_DATA = 5;
    //沙巴克（龙城争霸）系统数据
    public static final long SHOBAK = 6;
    //日常活动数据
    public static final long DAILY_ACTIVITY = 7;
    //王者禁地
//    public static final long KING_FORBIDDEN = 8;
    //夺宝
    public static final long ROB_TREASURE = 9;
    //通天塔
    public static final long TOWER = 10;
    //世界仓库
//    public static final long WAREHOUSE = 11;
    //神龙副本排行数据
    public static final long GOD_GRAGON_TOWER_RANK = 13;
    //天梯参与玩家
    public static final long TIAN_TI_DATA = 14;
    //七日狂欢
    public static final long HAPPY_SEVEN = 15;

    @Exclude
    private boolean dirty;

    //设置id
    public abstract void setId(long id);

    @Override
    public int dataType() {
        return DataType.SYS;
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
}
