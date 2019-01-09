package com.nemo.game.map.obj;

import com.nemo.commons.tuple.TwoTuple;
import com.nemo.game.map.buffer.BufferState;
import com.nemo.game.map.constant.MapConst.Dir;
import com.nemo.game.map.constant.MapConst.Speed;
import com.nemo.game.map.fsm.FSMMachine;
import com.nemo.game.map.scene.Point;
import com.nemo.game.system.attr.entity.Attribute;
import com.nemo.game.system.attr.constant.AttrinbuteConst.*;
import com.nemo.game.system.battle.entity.SpecAtt;
import com.nemo.game.system.cd.entity.Cd;
import com.nemo.game.system.cd.entity.Cdable;
import com.nemo.game.system.miji.entity.Skill;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Perf;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public abstract class Performer extends MapObject implements Cdable{
    public static final Logger LOGGER = LoggerFactory.getLogger(Performer.class);

    private int level;

    private int sex;

    private int career;

    protected long hp = 0;

    protected int mp = 0;
    //魔法
    protected long magic;

    protected boolean dead = false;

    protected Map<Long, Cd> cdMap = new HashMap<>();

    //最终属性
    private Attribute finalAttribute;
    //buff状态和属性
    private BufferState bufferState = new BufferState();
    //所有系统的属性集合
    private Map<AttributeType, TwoTuple<Attribute, Attribute>> attributes = new HashMap<>();
    //仇恨列表
    private Map<Long, Integer> threatMap = new ConcurrentHashMap<>();

    //战斗对象
    private long fightTarget;
    //攻击我的对象
    private long whoAttackMe;
    private long whoAttackMeTime;
    //我的攻击目标
    private long whoMyTarget;
    private long whoMyTargetTime;

    //主角
    protected Performer master;
    //是否跟随
    private boolean slave;

    //当前路径
    private List<Point> pathList = new ArrayList<>();
    //路径目标点
    private Point pathTargetPoint;
    //移动速度
    private int moveSpeed;
    //移动间隔
    private int moveInterval = 500;

    //特殊属性
    private Map<Integer, SpecAtt> specAttMap = new HashedMap();
    //系统可能产生的buff
    private Map<Integer, Integer> systemBuffIdMap = new HashedMap();
    //不可叠加的buff
    private Map<Integer, Buffer> bufferMap = new HashMap<>();
    //可叠加的buff
    private Map<Integer, Map<Integer, Buffer>> overlyingBufferMap = new HashMap<>();

    private int pvpLeastParam;
    //杀手id
    private long killerId;
    //死亡时间
    private long deadTime;
    //主要在gm命令中使用（初始化为false，在gm命令中可修改为true）
    private boolean invincible = false;
    //宠物
    private List<PetActor> petArray = new ArrayList<>();
    //英雄是否需要复活
    private boolean heroRelive = false;

    //技能释放目标的类型（用于判断善恶模式敌友双方）
    private int skillTargetType;
    //技能释放目标的id（用于判断善恶模式敌友双方）
    private long skillTargetId;

    //移动
    private Point lastPoint;
    private int lastMoveSpeed;
    private long lastMoveTime;
    private boolean moved = false;

    //技能buff
    private Map<Integer, Skill> skillMap = new HashMap<>();

    public abstract FSMMachine<? extends Performer> getMachine();

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public Attribute getFinalAttribute() {
        return finalAttribute;
    }

    public void setFinalAttribute(Attribute finalAttribute) {
        this.finalAttribute = finalAttribute;
    }

    public Map<AttributeType, TwoTuple<Attribute, Attribute>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<AttributeType, TwoTuple<Attribute, Attribute>> attributes) {
        this.attributes = attributes;
    }

    public Map<Long, Integer> getThreatMap() {
        return threatMap;
    }

    public void setThreatMap(Map<Long, Integer> threatMap) {
        this.threatMap = threatMap;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        /*if(hp > this.finalAttribute.getMaxHp()) {
            LOGGER.error("【{}】设置血量时超过最大血量hp:{},maxHp:{}", this.name,hp, this.finalAttribute.getMaxHp(), Utils.getStackTrace());
        }*/
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public long getFightTarget() {
        return fightTarget;
    }

    public void setFightTarget(long fightTarget) {
        this.fightTarget = fightTarget;
    }

    public Point getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(Point lastPoint) {
        this.lastPoint = lastPoint;
    }

    public int getLastMoveSpeed() {
        return lastMoveSpeed;
    }

    public void setLastMoveSpeed(int lastMoveSpeed) {
        this.lastMoveSpeed = lastMoveSpeed;
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }


    public Map<Integer, Buffer> getBufferMap() {
        return bufferMap;
    }

    public void setBufferMap(Map<Integer, Buffer> bufferMap) {
        this.bufferMap = bufferMap;
    }

    public long getKillerId() {
        return killerId;
    }

    public void setKillerId(long killerId) {
        this.killerId = killerId;
    }

    public Map<Long, Cd> getCdMap() {
        return cdMap;
    }

    public void setCdMap(Map<Long, Cd> cdMap) {
        this.cdMap = cdMap;
    }

    public Point getMovingPoint() {

        if (lastPoint == null || this.dir == Dir.NONE.getIndex()) {
            return point;
        }
        long diff = System.currentTimeMillis() - this.lastMoveTime;
        if (this.lastMoveSpeed == Speed.WALK) {
            if (diff >= Speed.WALK / 2) { // 用时一半
                return point;
            } else {
                return lastPoint;
            }
        } else if (this.lastMoveSpeed == Speed.RUN) {
            Point firstPoint = lastPoint.getNears()[this.dir]; // 一次走两个格子
            if (diff > Speed.RUN * 3 / 4) { // 走了3/4以上的时间，站在第二个点上
                return point;
            } else if (diff > Speed.RUN / 4) { // 时间过了1/4 站在第一个点上
                return firstPoint;
            } else {
                // 否则算当前还没走出第一个格子
                return lastPoint;
            }
        } else if (this.lastMoveSpeed == Speed.HORSE) {
            Point first = lastPoint.getNears()[this.dir];
            Point second = null;
            if (first != null)
                second = first.getNears()[this.dir];

            if (diff > Speed.HORSE * 5 / 6) {
                return point;
            } else if (diff > Speed.HORSE / 2) {
                return second;
            } else if (diff > Speed.HORSE / 6) {
                return first;
            } else
                return lastPoint;
        } else {
            return point;
        }
    }

    public void addThreat(Performer performer, int value) {

        Integer hate = this.threatMap.get(performer.getId());
        if (hate == null) {
            this.threatMap.put(performer.getId(), value);
        } else {
            this.threatMap.put(performer.getId(), hate + value);
        }
    }

    public byte totalHpPercent() {
        return 0;
    }

    public long getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(long deadTime) {
        this.deadTime = deadTime;
    }

    public long getWhoAttackMe() {
        return whoAttackMe;
    }

    public void setWhoAttackMe(long whoAttackMe) {

        this.whoAttackMe = whoAttackMe;
        this.whoAttackMeTime = 4000;
    }

    public long getWhoMyTarget() {
        return whoMyTarget;
    }

    public void setWhoMyTarget(long whoMyTarget) {
        this.whoMyTarget = whoMyTarget;
        this.whoMyTargetTime = 4000;
    }


    public List<PetActor> getPetArray() {
        return petArray;
    }

    public void setPetArray(List<PetActor> petArray) {
        this.petArray = petArray;
    }

    public long getWhoAttackMeTime() {
        return whoAttackMeTime;
    }

    public void setWhoAttackMeTime(long whoAttackMeTime) {
        this.whoAttackMeTime = whoAttackMeTime;
    }

    public long getWhoMyTargetTime() {
        return whoMyTargetTime;
    }

    public void setWhoMyTargetTime(long whoMyTargetTime) {
        this.whoMyTargetTime = whoMyTargetTime;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }


    public Performer getMaster() {
        return master;
    }

    public void setMaster(Performer master) {
        this.master = master;
    }

    public boolean isPlayer() {
        if (this.getType() == MapObjectType.Monster) {
            return ((MonsterActor) this).isPlayerMonster();
        }
        return this.getType() == MapObjectType.Player;
    }

    public boolean isMonster() {
        return this.getType() == MapObjectType.Monster && !((MonsterActor) this).isPlayerMonster();
    }
}
