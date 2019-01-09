package com.nemo.game.map.obj.data;

import java.util.HashMap;
import java.util.Map;

public class MonsterExtraData {
    /**
     * 主id 即玩家id
     */
    private long lid;
    /**
     * 装备
     */
    private Map<Integer, Integer> equipMap = new HashMap<>();

    /**
     * 翅膀
     */
    private int wing;

    /**
     * 神斧Id
     */
    private int godAxId;

    /**
     * 威望
     */
    private long weiWang;

    /**
     * 帮会ID
     */
    private long unionId;

    /**
     * 帮会名称
     */
    private String unionName;

    /**
     * 称号
     */
    private int title;

    /**
     * 军衔
     */
    private int junxian;

    /**
     * 时装
     */
    private Map<Integer, Integer> fashion = new HashMap<>();

    public Map<Integer, Integer> getEquipMap() {
        return equipMap;
    }

    public int getWing() {
        return wing;
    }

    public int getGodAxId() {
        return godAxId;
    }

    public long getWeiWang() {
        return weiWang;
    }

    public long getUnionId() {
        return unionId;
    }

    public String getUnionName() {
        return unionName;
    }

    public int getTitle() {
        return title;
    }

    public int getJunxian() {
        return junxian;
    }

    public Map<Integer, Integer> getFashion() {
        return fashion;
    }

    public void setWing(int wing) {
        this.wing = wing;
    }

    public void setGodAxId(int godAxId) {
        this.godAxId = godAxId;
    }

    public void setWeiWang(long weiWang) {
        this.weiWang = weiWang;
    }

    public void setUnionId(long unionId) {
        this.unionId = unionId;
    }

    public void setUnionName(String unionName) {
        this.unionName = unionName;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public void setJunxian(int junxian) {
        this.junxian = junxian;
    }

    public long getLid() {
        return lid;
    }

    public void setLid(long lid) {
        this.lid = lid;
    }
}
