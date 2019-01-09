package com.nemo.game.map;

import com.google.common.collect.Maps;
import com.nemo.game.map.obj.PlayerActor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Slf4j
public class Player {
    private long id;

    private String name;
    //是否登录了地图
    private boolean loginMap;
    //所有角色总血量
    private long totalMaxHp;
    //内功
    private long totalMaxIp;

    private long totalMaxMagic;

    private int dieCount;
    //当前主角
    private PlayerActor current;
    //玩家本身Actor
    private PlayerActor main;

    //是否在远程地图里
    private boolean remote;
    //远程主机id
    private int remoteHostId;
    //远程地图id
    private int remoteMapId;

    //药品等道具使用情况
    private Map<Integer, Integer> itemUsedMap = Maps.newHashMap();

    private Map<Long, PlayerActor> actorMap = new HashMap<>();

    private Map<Long, Long> attackMap = new HashMap<>();
    //战力
    private long totalPower;
    //守护cd
    private int shouhuCd;
    //下次守护时间
    private long nextShouHuTime;
    //守护id
    private int shouhuId;
    //守护buff
    private Map<Integer, Integer> shouhuBuffMap = Maps.newHashMap();
    //buffcd
    private Map<Integer, Long> shouhuBuffCdMap = Maps.newHashMap();

    //血量百分比
    public byte totalHpPercent() {
        double totalHp = 0;
        for (PlayerActor playerActor : actorMap.values()) {
            if (playerActor.isDead()) {
                continue;
            }
            totalHp += playerActor.getHp();

        }

        if (totalHp <= 0) {
            return -1;
        }

        if (totalMaxHp > 0) {
            byte percent = (byte) Math.floor(totalHp / totalMaxHp * 100);
            return percent;
        } else {
            return 0;
        }
    }

    //内功百分比 InnerPower
    public byte totalIpPercent() {
        double totalIp = 0;
        for (PlayerActor playerActor : actorMap.values()) {
            if (playerActor.isDead()) {
                continue;
            }
            totalIp += playerActor.getIp();
        }
        if (totalIp <= 0) {
            return -1;
        }

        return (byte) Math.floor(totalIp / totalMaxIp * 100);
    }

    //魔法值百分比 magic
    public byte totalMagicPercent() {
        double totalMagic = 0;
        for (PlayerActor playerActor : actorMap.values()) {
            if (playerActor.isDead()) {
                continue;
            }
            totalMagic += playerActor.getMagic();
        }

        if (totalMagic <= 0) {
            return -1;
        }

        return (byte) Math.floor(totalMagic / totalMaxMagic * 100);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerActor getCurrent() {
        return current;
    }

    public void setCurrent(PlayerActor current) {
        this.current = current;
    }

    public Map<Long, PlayerActor> getActorMap() {
        return actorMap;
    }

    public void setActorMap(Map<Long, PlayerActor> actorMap) {
        this.actorMap = actorMap;
    }

    public Map<Integer, Integer> getItemUsedMap() {
        return itemUsedMap;
    }

    public void setItemUsedMap(Map<Integer, Integer> itemUsedMap) {
        this.itemUsedMap = itemUsedMap;
    }

    public long getTotalMaxHp() {
        return totalMaxHp;
    }

    public void setTotalMaxHp(long totalMaxHp) {
        this.totalMaxHp = totalMaxHp;
    }

    public long getTotalMaxIp() {
        return totalMaxIp;
    }

    public void setTotalMaxIp(long totalMaxIp) {
        this.totalMaxIp = totalMaxIp;
    }
}
