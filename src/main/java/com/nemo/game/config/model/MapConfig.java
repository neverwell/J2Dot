package com.nemo.game.config.model;

import com.nemo.common.config.AbstractConfigData;
import com.nemo.common.config.IConfigData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapConfig extends AbstractConfigData{
    /**
     * 地图id
     */
    private int id;
    /**
     * 地图名字
     */
    private String name;

    /**
     * 地形文件名
     */
    private int data;

    /**
     * 分线
     */
    private int line;

    /**
     * 地图宽度
     */
    private int width;

    /**
     * 地图高度
     */
    private int height;

    /**
     * 是否可以无条件穿人
     */
    private int cancross;

    /**
     * 屏蔽 1
     */
    private int forbidden;

    /**
     * 是否是安全地图
     */
    private int safe;

    /**
     * 城镇复活的地图id
     */
    private int homeMapId;

    /**
     * 城镇复活的x
     */
    private int homeX;

    /**
     * 城镇复活的y
     */

    private int homeY;

    /**
     * 玩家复活需要的时间
     *//*
	private int reliveHomeTime;

	*//**
     * 是否可以传送 1 不允许传送
     *//*
	private int canFly;

	*//**
     * 原地复活血量
     *//*
	private int recoverPercent;

	private int[] reliveType;

	*//**
     * 原地附后需要的道具 id#count
     *//*
	private int[] reliveHereItem;*/

    /**
     * 是否是挂机地图
     */
    private int rpg;

    /**
     * 是否是副本
     */
    private int duplicate;

    /**
     * 角色AI技能表id
     */
    private int playerAI;

    /**
     * 地图列i型主要是一些玩法 0 普通，1 野外boss
     */
    private int mapType;

    /**
     * 是否可以花钱买活，0、不可以，1、回到挂机、2、出生点复活，3、原地复活
     */
    private int canHereRelive;

    /**
     * 自动复活 type#initTime#addTime#maxTime
     */
    private int[] autoRelive;

    /**
     * 原地复活消耗的元宝 init#add#max
     */
    private int[] hereReliveCost;

    /**
     * 默认攻击模式 0 全体 1 帮会 2 善恶
     */
    private int fightModel;

    private int cls;

    private int isQuickPick;
}
