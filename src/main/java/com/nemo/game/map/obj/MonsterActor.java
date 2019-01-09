package com.nemo.game.map.obj;

import com.nemo.game.map.fsm.FSMMachine;
import com.nemo.game.map.obj.data.MonsterExtraData;
import com.nemo.game.map.scene.Point;

//怪物
public class MonsterActor extends Performer {

	// ================AI相关属性=======================================
	private FSMMachine<? extends MonsterActor> machine;

	/**
	 * AI类型 </br>
	 * 1、怪物 怪物的AI类型   @see AiType
	 * 2、宠物 0 保护 1战斗  2跟随 3停止
	 */
	private int aiType = 1;

	/**
	 * AI如果是脚本，就是脚本的名称
	 */
	private String script;

	/**
	 * 警戒范围（原点为出生点）
	 */
	private int toAttackArea = 3;

	/**
	 * 追击距离（原点为出生点）
	 */
	private int chaseDis = 4;

	/**
	 * 周围警戒的反应速度
	 */
	private int heart = 3;

	/**
	 * 睡眠时间
	 */
	private int reliveDelay = 30;

	/**
	 * 死亡尸体停留时间(ms)
	 */
	private int dieDelay = 1000;

	/**
	 * 复活类型
	 * 1. 固定时间
	 * 2. 每天定点复活
	 */
	private int reliveType = 0;

	/**
	 * 出生点
	 */
	private Point birthPoint;

	/**
	 * 最近的可攻击对象
	 */
	private long nearestObjectId;

	/**
	 * 单次最大伤害
	 */
	private long maxdamage;

	/**
	 * 是否刚刚初始化
	 */
	private boolean init;

	private FixHurtType fixHurtType = FixHurtType.FIX_0;

    /**
     * 是否受到元素伤害
     */
	private boolean canElementHurt;

	// ================AI相关属性=======================================
	/**
	 * 怪物归属
	 */
	private long owner;

	/**
	 * 怪物类型
	 */
	private int monsterType;

	/**
	 * 是否死亡以后立即从场景中移除
	 */
	private boolean removeAfterDie;

	/**
	 * boss的护盾触发（特殊玩法，考虑是否放到这里，占用内存）
	 */
	private boolean hudunTriggered;

	/**
	 *
	 */
	private int canRepel;

	/**
	 * 额外数据
	 */
	private MonsterExtraData extraData;

    /**
     *  怪物类型 0普通怪 1真人副本怪 2配置表随机怪
     */
    private int playerMonster;

	@Override
	public int getType() {
		return MapObjectType.Monster;
	}

	@Override
	public FSMMachine<? extends MonsterActor> getMachine() {
		return machine;
	}

	public void setMachine(FSMMachine<? extends MonsterActor> machine) {
		this.machine = machine;
	}

	public Point getBirthPoint() {
		return birthPoint;
	}

	public void setBirthPoint(Point birthPoint) {
		this.birthPoint = birthPoint;
	}

	public int getAiType() {
		return aiType;
	}

	public void setAiType(int aiType) {
		this.aiType = aiType;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public int getToAttackArea() {
		return toAttackArea;
	}

	public void setToAttackArea(int toAttackArea) {
		this.toAttackArea = toAttackArea;
	}

	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
	}

	public int getReliveDelay() {
		return reliveDelay;
	}

	public void setReliveDelay(int reliveDelay) {
		this.reliveDelay = reliveDelay;
	}

	public int getDieDelay() {
		return dieDelay;
	}

	public void setDieDelay(int dieDelay) {
		this.dieDelay = dieDelay;
	}

	public int getReliveType() {
		return reliveType;
	}

	public void setReliveType(int reliveType) {
		this.reliveType = reliveType;
	}

	public long getNearestObjectId() {
		return nearestObjectId;
	}

	public void setNearestObjectId(long nearestObjectId) {
		this.nearestObjectId = nearestObjectId;
	}

	public long getMaxdamage() {
		return maxdamage;
	}

	public void setMaxdamage(long maxdamage) {
		this.maxdamage = maxdamage;
	}

    public boolean isCanElementHurt() {
        return canElementHurt;
    }

    public void setCanElementHurt(boolean canElementHurt) {
        this.canElementHurt = canElementHurt;
    }

    public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public int getMonsterType() {
		return monsterType;
	}

	public void setMonsterType(int monsterType) {
		this.monsterType = monsterType;
	}

	public boolean isRemoveAfterDie() {
		return removeAfterDie;
	}

	public void setRemoveAfterDie(boolean removeAfterDie) {
		this.removeAfterDie = removeAfterDie;
	}

	public FixHurtType getFixHurtType() {
		return fixHurtType;
	}

	public void setFixHurtType(FixHurtType fixHurtType) {
		this.fixHurtType = fixHurtType;
	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	@Override
	public boolean penetrate(IMapObject obj, boolean cross) {
		if (obj.getType() == MapObjectType.Player || obj.getType() == MapObjectType.Pet) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean overlying(IMapObject obj, boolean cross) {
		if (obj.getType() == MapObjectType.Player || obj.getType() == MapObjectType.Pet || obj.getType() == MapObjectType.Hero) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isEnemy(IMapObject obj, boolean ignoreTargetOnly) {
		if (obj == this) {
			return false;
		} else if (obj.getType() == MapObjectType.Player) {
			return true;
		} else if (obj.getType() == MapObjectType.Hero) {
			return false;
		} else if (obj.getType() == MapObjectType.Pet) {
			return false;
		}else {
			return false;
		}
	}

	@Override
	public boolean isFriend(IMapObject obj, boolean ignoreTargetOnly) {
		return false;
	}

	public boolean isHudunTriggered() {
		return hudunTriggered;
	}

	public void setHudunTriggered(boolean hudunTriggered) {
		this.hudunTriggered = hudunTriggered;
	}

	public int getChaseDis() {
		return chaseDis;
	}

	public void setChaseDis(int chaseDis) {
		this.chaseDis = chaseDis;
	}

	public int getCanRepel() {
		return canRepel;
	}

	public void setCanRepel(int canRepel) {
		this.canRepel = canRepel;
	}

	public MonsterExtraData getExtraData() {
		return extraData;
	}

	public void setExtraData(MonsterExtraData extraData) {
		this.extraData = extraData;
	}

	public boolean isPlayerMonster() {
		return playerMonster != 0;
	}

	public void setPlayerMonster(int playerMonster) {
		this.playerMonster = playerMonster;
	}

	public int getPlayerMonster() {
		return playerMonster;
	}
}
