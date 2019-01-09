package com.nemo.game.system.cd;

import com.nemo.commons.util.Cast;
import com.nemo.game.map.obj.PlayerActor;
import com.nemo.game.system.cd.entity.Cd;
import com.nemo.game.system.cd.entity.Cdable;

import java.util.Map;

public class CdManager {
    private static CdManager ourInstance = new CdManager();

    private CdManager(){}

    public static CdManager getInstance() {
        return ourInstance;
    }

    public void addCd(Cdable soldier, int cdType, int key, long endTime) {
        Map<Long, Cd> cdMap = soldier.getCdMap();
        long cdKey = getKey(cdType, key);
        Cd cd = new Cd();
        cd.setEndTime(endTime);
        cd.setKey(key);
        cd.setType(cdType);
        cdMap.put(cdKey, cd);
    }

    public long getCd(Cdable soldier, int cdType, int key) {
        Map<Long, Cd> cdMap = soldier.getCdMap();
        long cdKey = getKey(cdType, key);
        Cd cd = cdMap.get(cdKey);
        if(cd != null) {
            return cd.getEndTime();
        }
        return 0;
    }

    //curTime是否不在冷却时间内
    public boolean isCool(Cdable soldier, int cdType, int key, long curTime) {
        Map<Long, Cd> cdMap = soldier.getCdMap();
        long cdKey = getKey(cdType, key);
        Cd cd = cdMap.get(cdKey);

        if(soldier instanceof PlayerActor) {
            PlayerActor actor = (PlayerActor) soldier;
            if(actor.isSlave()) {
                //...
            }
        }

        if(cd != null) {
            return curTime > cd.getEndTime();
        }
        return true;
    }

    //当前是否不在冷却时间内
    public boolean isCool(Cdable soldier, int cdType, int key) {
        return isCool(soldier, cdType, key, System.currentTimeMillis());
    }

    private long getKey(int cdType, int key) {
        return Cast.combineInt2Long(cdType, key);
    }
}
