package com.nemo.game.data;

import com.nemo.game.entity.sys.AbstractSysData;
import com.nemo.game.entity.sys.DailyActivityData;
import com.nemo.game.entity.sys.GlobalCount;
import com.nemo.game.entity.sys.GodDragonTowerData;
import com.nemo.game.entity.sys.HappySevenData;
import com.nemo.game.entity.sys.OtherData;
import com.nemo.game.entity.sys.RobTreasureData;
import com.nemo.game.entity.sys.ShoBakData;
import com.nemo.game.entity.sys.SysActivityData;
import com.nemo.game.entity.sys.TianTiData;
import com.nemo.game.entity.sys.TowerData;
import com.nemo.game.system.chapter.entity.ChapterRankData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

//系统数据提供者
public class SysDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysDataProvider.class);

    private static final Map<Class<? extends AbstractSysData>, Long> CLAZZ2ID = new HashMap<>();

    public static void init() {
        CLAZZ2ID.put(GlobalCount.class, AbstractSysData.COUNT);
        CLAZZ2ID.put(OtherData.class, AbstractSysData.OTHER);
        CLAZZ2ID.put(ChapterRankData.class, AbstractSysData.CHAPTER_RANK);
        CLAZZ2ID.put(SysActivityData.class, AbstractSysData.SYS_ACTIVITY_DATA);
        //CLAZZ2ID.put(LadderData.class, AbstractSysData.LADDER_DATA);
        CLAZZ2ID.put(ShoBakData.class, AbstractSysData.SHOBAK);
        CLAZZ2ID.put(DailyActivityData.class, AbstractSysData.DAILY_ACTIVITY);
        //CLAZZ2ID.put(KingForbiddenData.class, AbstractSysData.KING_FORBIDDEN);
        CLAZZ2ID.put(RobTreasureData.class, AbstractSysData.ROB_TREASURE);
        CLAZZ2ID.put(TowerData.class, AbstractSysData.TOWER);
        //CLAZZ2ID.put(WorldWarehouse.class, AbstractSysData.WAREHOUSE);
        CLAZZ2ID.put(GodDragonTowerData.class, AbstractSysData.GOD_GRAGON_TOWER_RANK);
        CLAZZ2ID.put(TianTiData.class, AbstractSysData.TIAN_TI_DATA);
        CLAZZ2ID.put(HappySevenData.class, AbstractSysData.HAPPY_SEVEN);
    }

    public synchronized static <T extends AbstractSysData> T get(Class<? extends T> clazz) {
        long id = CLAZZ2ID.get(clazz);

        T ret = DataCenter.getSysData(id, clazz);
        if (ret == null) {
            ret = create(clazz);
            ret.setId(id);
            DataCenter.insertData(ret, true);
        }
        return ret;
    }

    private static <T extends AbstractSysData> T create(Class<? extends T> clazz) {
        T ret = null;
        try {
            ret = clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("创建系统数据失败.", e);
        }
        return ret;
    }
}
