package com.nemo.game.system.attr.entity;

import io.protostuff.Exclude;
import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

//玩家属性
@Getter
@Setter
public class Attribute implements Cloneable{

    /**
     * 攻击
     */
    @Tag(1)
    protected int attack = 0;

    /**
     * 物理防御
     */
    @Tag(2)
    protected int phyDefence = 0;

    /**
     * 魔法防御
     */
    @Tag(3)
    protected int magicDefence = 0;

    /**
     * 最大HP
     */
    @Tag(4)
    protected long maxHp = 0;

    /**
     * 暴击概率
     */
    @Tag(5)
    protected int critical = 0;

    /**
     * 暴击固定增加伤害
     */
    @Tag(6)
    protected int critFix = 0;

    /**
     * 抗暴值
     */
    @Tag(7)
    protected int critResist = 0;

    /**
     * 战斗力
     */
    @Tag(8)
    public int fightPower = 0;

    //------------------- PVP 属性 -------------------------

    /**
     * 对战士伤害增加
     */
    @Tag(9)
    protected int zsHurtAdd = 0;

    /**
     * 对法师伤害增加
     */
    @Tag(10)
    protected int fsHurtAdd = 0;

    /**
     * 对道士伤害增加
     */
    @Tag(11)
    protected int dsHurtAdd = 0;

    /**
     * 受战士伤害减免
     */
    @Tag(12)
    protected int zsHurtedRelief = 0;

    /**
     * 受法师伤害减免
     */
    @Tag(13)
    protected int fsHurtedRelief = 0;

    /**
     * 受道士伤害减免
     */
    @Tag(14)
    protected int dsHurtedRelief = 0;

    //--------------- 只对怪物生效 --------------------

    /**
     * 对怪物伤害增加
     */
    @Tag(15)
    protected int monHurtAdd = 0;

    /**
     * 受怪物伤害减免
     */
    @Tag(16)
    protected int monHurtedRelief = 0;

    /**
     * 对怪物暴击概率
     */
    @Tag(17)
    protected int monCritical = 0;

    /**
     * 对怪物暴击伤害
     */
    @Tag(18)
    protected int monCritFix = 0;

    /**
     * 对怪物伤害固定增加xx点
     */
    @Tag(19)
    private int toMonsterHurtFixAdd = 0;

    /**
     * 免伤
     */
    @Tag(20)
    protected int relief = 0;

    /**
     * 对指定等级怪物的伤害增加(万分比)
     */
    @Tag(21)
    private Map<Integer, Integer> pointedToMonsterHurtAdd = new HashMap<>();
    /**
     * 受指定等级的怪物伤害减免(万分比)
     */
    @Tag(22)
    private Map<Integer, Integer> pointedFromMonsterHurtDef = new HashMap<>();
    /**
     * 对指定等级怪物爆击伤害增加
     */
    @Tag(23)
    private Map<Integer, Integer> pointedToMonsterBsAdd = new HashMap<>();


    @Tag(24)
    private int maxMp;

    /**
     * 攻速
     */
    @Tag(25)
    private int attackSpeed;

    /**
     * 战斗回血
     */
    @Tag(26)
    private int fightRec;

    /**
     * 威望伤害加成
     */
    @Tag(27)
    private int weiwangHurtAdd;

    /**
     * 最大HP
     */
    @Tag(28)
    protected long maxHpNew;

    /**
     * 挂机金币加成（万分比）
     */
    @Tag(29)
    protected int hangUpGoldAdd;

    /**
     * 挂机经验加成（万分比）
     */
    @Tag(30)
    protected int hangUpExpAdd;

    /**
     * 当前剩余内功值
     */
    @Tag(31)
    protected int currentInnerPower;

    /**
     * 内功最大值
     */
    @Tag(32)
    protected int innerPowerMax;

    /**
     * 内功减伤
     */
    @Tag(33)
    protected int innerRelief;

    /**
     * 内功每秒回复
     */
    @Tag(34)
    protected int innerRestore;

    /**
     * 造成伤害增加
     */
    @Tag(35)
    protected int hurtAdd;

    /**
     * 暴击强度
     */
    @Tag(36)
    protected int critrate;

    /**
     * 基础暴击强度
     */
    @Tag(37)
    protected int basicCritrate;

    /**
     * 连击神圣伤害固定值
     */
    @Tag(38)
    protected int ljholyAtk;

    /**
     * 连击伤害减免万分比
     */
    @Tag(39)
    protected int ljHurtedRelief;

    /**
     * 神圣伤害
     */
    @Tag(40)
    protected int holyAtk;

    /**
     * 神圣精通
     */
    @Tag(41)
    protected int holyAdd;

    /**
     * 暴击伤害增加
     */
    @Tag(42)
    protected int critFixAdd;

    /**
     * 暴击伤害减免
     */
    @Tag(43)
    protected int critFixRelief;

    /**
     * 暴击概率plus
     */
    @Tag(44)
    protected int criticalPlus;

    /**
     * 暴击抗性（韧性）plus
     */
    @Tag(45)
    protected int critResistPlus;

    /**
     * 伤害增加plus
     */
    @Tag(46)
    protected int hurtAddPlus;

    /**
     * 免伤plus
     */
    @Tag(47)
    protected int reliefPlus;

    /**
     * 致命一击概率
     */
    @Tag(48)
    protected int deadatk;

    /**
     * 致命一击抵抗
     */
    @Tag(49)
    protected int deadatkRelief;

    /**
     * 致命一击伤害
     */
    @Tag(50)
    protected int deadHurt;

    /**
     * 致命一击伤害增加
     */
    @Tag(51)
    protected int deadHurtAdd;

    /**
     * 致命一击伤害减少
     */
    @Tag(52)
    protected int deadHurtRelief;

    /**
     * 魔法值
     */
    @Tag(53)
    protected int maxMagic;

    /**
     * 护身
     */
    @Tag(54)
    protected int guard;

    /**
     * 地元素攻击
     */
    @Tag(55)
    protected int dAtk;

    /**
     * 地元素抵抗
     */
    @Tag(56)
    protected int dDef;

    /**
     * 地攻击减伤
     */
    @Tag(57)
    protected int dRelief;

    /**
     * 地攻击加伤
     */
    @Tag(58)
    protected int dHurtAdd;

    /**
     * 风元素攻击
     */
    @Tag(59)
    protected int fAtk;

    /**
     * 风元素抵抗
     */
    @Tag(60)
    protected int fDef;

    /**
     * 风攻击减伤
     */
    @Tag(61)
    protected int fRelief;

    /**
     * 风攻击加伤
     */
    @Tag(62)
    protected int fHurtAdd;

    /**
     * 水元素攻击
     */
    @Tag(63)
    protected int sAtk;

    /**
     * 水元素抵抗
     */
    @Tag(64)
    protected int sDef;

    /**
     * 水元素减伤
     */
    @Tag(65)
    protected int sRelief;

    /**
     * 水元素加伤
     */
    @Tag(66)
    protected int sHurtAdd;

    /**
     * 火元素攻击
     */
    @Tag(67)
    protected int hAtk;

    /**
     * 火元素抵抗
     */
    @Tag(68)
    protected int hDef;

    /**
     * 火元素减伤
     */
    @Tag(69)
    protected int hRelief;

    /**
     * 火元素加伤
     */
    @Tag(70)
    protected int hHurtAdd;

    /**
     * 免伤穿透(暂时假人和boss生效)
     */
    @Exclude
    private int reliefPenetrate;

    @Exclude()
    public int percent = 10000;

    public static Attribute create() {
        return new Attribute();
    }

    public long getMaxHpNew() {
        if (maxHpNew == 0) {//强制兼容
            maxHpNew = maxHp;
        }
        return maxHpNew;
    }


    public void fixAdd(Attribute attr) {


        //攻击
        attack += attr.attack;

        //物理防御
        phyDefence += attr.phyDefence;

        //魔法防御
        magicDefence += attr.magicDefence;

        //最大HP
        //maxHp += attr.maxHp;

        maxHpNew += attr.maxHpNew;

        //------------------- 暴击属性 -------------------------

        // 暴击概率
        critical += attr.critical;

        //暴击固定增加伤害
        critFix += attr.critFix;

        // 暴击防御(韧性)
        critResist += attr.critResist;

        // 战斗力
        fightPower += attr.fightPower;

        //------------------- PVP 属性 -------------------------

        //对战士伤害增加
        zsHurtAdd += attr.zsHurtAdd;

        // 对法师伤害增加
        fsHurtAdd += attr.fsHurtAdd;

        //对道士伤害增加
        dsHurtAdd += attr.dsHurtAdd;


        //受战士伤害减免
        zsHurtedRelief += attr.zsHurtedRelief;

        //受法师伤害减免
        fsHurtedRelief += attr.fsHurtedRelief;

        // 受道士伤害减免
        dsHurtedRelief += attr.dsHurtedRelief;

        // 免伤
        relief += attr.relief;

        //--------------- 只对怪物生效 --------------------

        // 对怪物伤害增加
        monHurtAdd += attr.monHurtAdd;

        // 受怪物伤害减免
        monHurtedRelief += attr.monHurtedRelief;

        //对怪物暴击值
        monCritical += attr.monCritical;

        //对怪物暴击伤害
        monCritFix += attr.monCritFix;

        //对怪物伤害固定增加xx点
        toMonsterHurtFixAdd += attr.toMonsterHurtFixAdd;

        //威望伤害
        weiwangHurtAdd += attr.weiwangHurtAdd;

        //对指定怪物爆击伤害增加
//        AttributeManager.getInstance().addBestAtt(this.pointedToMonsterBsAdd, attr.pointedToMonsterBsAdd);

        //挂机金币加成
        hangUpGoldAdd += attr.hangUpGoldAdd;

        //挂机经验加成
        hangUpExpAdd += attr.hangUpExpAdd;

        innerPowerMax += attr.innerPowerMax;

        innerRelief += attr.innerRelief;

        innerRestore += attr.innerRestore;

        hurtAdd += attr.hurtAdd;

        critrate += attr.critrate;

        basicCritrate += attr.basicCritrate;

        ljholyAtk += attr.ljholyAtk;

        ljHurtedRelief += attr.ljHurtedRelief;

        holyAtk += attr.holyAtk;

        holyAdd += attr.holyAdd;

        critFixAdd += attr.critFixAdd;

        critFixRelief += attr.critFixRelief;

        criticalPlus += attr.criticalPlus;

        critResistPlus += attr.critResistPlus;

        hurtAddPlus += attr.hurtAddPlus;

        reliefPlus += attr.reliefPlus;

        deadatk += attr.deadatk;

        deadatkRelief += attr.deadatkRelief;

        deadHurt += attr.deadHurt;

        deadHurtAdd += attr.deadHurtAdd;

        deadHurtRelief += attr.deadHurtRelief;

        maxMagic += attr.maxMagic;

        guard += attr.guard;

        dAtk += attr.dAtk;

        dDef += attr.dDef;

        dRelief += attr.dRelief;

        dHurtAdd += attr.dHurtAdd;

        fAtk += attr.fAtk;

        fDef += attr.fDef;

        fRelief += attr.fRelief;

        fHurtAdd += attr.fHurtAdd;

        sAtk += attr.sAtk;

        sDef += attr.sDef;

        sRelief += attr.sRelief;

        sHurtAdd += attr.sHurtAdd;

        hAtk += attr.hAtk;

        hDef += attr.hDef;

        hRelief += attr.hRelief;

        hHurtAdd += attr.hHurtAdd;
    }

    public void percentAdd(Attribute attr) {

        //攻击
        attack += ((long) attack * (attr.attack)) / percent;

        //物理防御
        phyDefence += ((long) phyDefence * (attr.phyDefence)) / percent;

        //魔法防御
        magicDefence += ((long) magicDefence * (attr.magicDefence)) / percent;

        //最大HP
        //maxHp += ((long) maxHp * (attr.maxHp)) / percent;
        maxHpNew += maxHpNew * (attr.maxHpNew) / percent;

        //------------------- 暴击属性 -------------------------

        //暴击概率
        critical += ((long) critical * (attr.critical)) / percent;

        // 暴击伤害
        critFix += ((long) critFix * (attr.critFix)) / percent;

        //暴击抗性（韧性）
        critResist += ((long) critResist * (attr.critResist)) / percent;

        //战斗力
        fightPower += ((long) fightPower * (attr.fightPower)) / percent;

        //------------------- PVP 属性 -------------------------

        //对战士伤害增加
        zsHurtAdd += ((long) zsHurtAdd * (attr.zsHurtAdd)) / percent;

        //对法师伤害增加
        fsHurtAdd += ((long) fsHurtAdd * (attr.fsHurtAdd)) / percent;

        //对道士伤害增加
        zsHurtAdd += ((long) zsHurtAdd * (attr.zsHurtAdd)) / percent;


        //受战士伤害减免
        zsHurtedRelief += ((long) zsHurtedRelief * (attr.zsHurtedRelief)) / percent;

        //受法师伤害减免
        fsHurtedRelief += ((long) fsHurtedRelief * (attr.fsHurtedRelief)) / percent;

        //受道士伤害减免
        dsHurtedRelief += ((long) dsHurtedRelief * (attr.dsHurtedRelief)) / percent;

        //免伤
        relief += ((long) relief * (attr.relief)) / percent;

        //--------------- 只对怪物生效 --------------------

        //对怪物伤害增加
        monHurtAdd += ((long) monHurtAdd * (attr.monHurtAdd)) / percent;

        //受怪物伤害减免
        monHurtedRelief += ((long) monHurtedRelief * (attr.monHurtedRelief)) / percent;

        //对怪物暴击概率
        monCritical += ((long) monCritical * (attr.monCritical)) / percent;

        //对怪物暴击伤害
        monCritFix += ((long) monCritFix * (attr.monCritFix)) / percent;


        // 对指定怪物的伤害增加
        Map<Integer, Integer> pointedToMonsterHurtAdd = this.pointedToMonsterHurtAdd;
        Map<Integer, Integer> newPointedToMonsterHurtAdd = attr.pointedToMonsterHurtAdd;
        addPercentBestAtt(pointedToMonsterHurtAdd, newPointedToMonsterHurtAdd);

        // 受指定怪物的伤害减免
        Map<Integer, Integer> pointedFromMonsterHurtDef = this.pointedFromMonsterHurtDef;
        Map<Integer, Integer> newPointedFromMonsterHurtDef = attr.pointedFromMonsterHurtDef;
        addPercentBestAtt(pointedFromMonsterHurtDef, newPointedFromMonsterHurtDef);

        //对怪物伤害固定增加xx点
        toMonsterHurtFixAdd += ((long) toMonsterHurtFixAdd * (attr.toMonsterHurtFixAdd)) / percent;

        //威望伤害
        weiwangHurtAdd += ((long) weiwangHurtAdd * (attr.weiwangHurtAdd)) / percent;

        //挂机金币加成
        hangUpGoldAdd += ((long) hangUpGoldAdd * attr.hangUpGoldAdd) / percent;

        //挂机经验加成
        hangUpExpAdd += ((long) hangUpExpAdd * attr.hangUpExpAdd) / percent;

        innerPowerMax += ((long) innerPowerMax * attr.innerPowerMax) / percent;

        innerRelief += ((long) innerRelief * attr.innerRelief) / percent;

        innerRestore += ((long) innerRestore * attr.innerRestore) / percent;

        hurtAdd += ((long) hurtAdd * attr.hurtAdd) / percent;

        critrate += ((long) critrate * attr.critrate) / percent;

        basicCritrate += ((long) basicCritrate * attr.basicCritrate) / percent;

        ljholyAtk += ((long) ljholyAtk * attr.ljholyAtk) / percent;

        ljHurtedRelief += ((long) ljHurtedRelief * attr.ljHurtedRelief) / percent;

        holyAtk += ((long) holyAtk * attr.holyAtk) / percent;

        holyAdd += ((long) holyAdd * attr.holyAdd) / percent;

        critFixAdd += ((long) critFixAdd * attr.critFixAdd) / percent;

        critFixRelief += ((long) critFixRelief * attr.critFixRelief) / percent;

        criticalPlus += ((long) criticalPlus * attr.criticalPlus) / percent;

        critResistPlus += ((long) critResistPlus * attr.critResistPlus) / percent;

        hurtAddPlus += ((long) hurtAddPlus * attr.hurtAddPlus) / percent;

        reliefPlus += ((long) reliefPlus * attr.reliefPlus) / percent;

        deadatk += ((long) deadatk * attr.deadatk) / percent;

        deadatkRelief += ((long) deadatkRelief * attr.deadatkRelief) / percent;

        deadHurt += ((long) deadHurt * attr.deadatk) / percent;

        deadHurtAdd += ((long) deadHurtAdd * attr.deadatk) / percent;

        deadHurtRelief += ((long) deadHurtRelief * attr.deadatk) / percent;

        maxMagic += ((long) maxMagic * attr.maxMagic) / percent;

        guard += ((long) guard * attr.guard) / percent;

        dAtk += ((long) dAtk * attr.dAtk) / percent;

        dDef += ((long) dDef * attr.dDef) / percent;

        dRelief += ((long) dRelief * attr.dRelief) / percent;

        dHurtAdd += ((long) dHurtAdd * attr.dHurtAdd) / percent;

        fAtk += ((long) fAtk * attr.fAtk) / percent;

        fDef += ((long) fDef * attr.fDef) / percent;

        fRelief += ((long) fRelief * attr.fRelief) / percent;

        fHurtAdd += ((long) fHurtAdd * attr.fHurtAdd) / percent;

        sAtk += ((long) sAtk * attr.sAtk) / percent;

        sDef += ((long) sDef * attr.sDef) / percent;

        sRelief += ((long) sRelief * attr.sRelief) / percent;

        sHurtAdd += ((long) sHurtAdd * attr.sHurtAdd) / percent;

        hAtk += ((long) hAtk * attr.hAtk) / percent;

        hDef += ((long) hDef * attr.hDef) / percent;

        hRelief += ((long) hRelief * attr.hRelief) / percent;

        hHurtAdd += ((long) hHurtAdd * attr.hHurtAdd) / percent;
    }

    private void addPercentBestAtt(Map<Integer, Integer> pointedFromMonsterHurtDef, Map<Integer, Integer> newPointedFromMonsterHurtDef) {
        for (Integer level : pointedFromMonsterHurtDef.keySet()) {
            Integer current = pointedFromMonsterHurtDef.get(level);
            Integer addCount = newPointedFromMonsterHurtDef.get(level);
            if (current == null) {
                current = 0;
                pointedFromMonsterHurtDef.put(level, 0);
            }
            if (addCount != null) {
                int percentAdd = (int) ((long) current * addCount / percent);
                pointedFromMonsterHurtDef.put(level, current + percentAdd);
            }
        }
    }

//    public ResPlayerAttributeChangeMessage compare(Attribute attr, Role role) {
//        List<Byte> types = new ArrayList<>();
//        List<Integer> values = new ArrayList<>();
//        //1
//        if (attr.attack != attack) {
//            types.add(AttributeConst.PropertiesType.ATTACK);
//            values.add(attr.attack);
//        }
//        //2
//        if (attr.phyDefence != phyDefence) {
//            types.add(AttributeConst.PropertiesType.PHY_DEFENCE);
//            values.add(attr.phyDefence);
//        }
//        //3
//        if (attr.magicDefence != magicDefence) {
//            types.add(AttributeConst.PropertiesType.MAGIC_DEFENCE);
//            values.add(attr.magicDefence);
//        }
//        //4
//       /* if (attr.maxHp != maxHp) {
//            types.add(AttributeConst.PropertiesType.MAX_HP);
//            values.add((int) attr.maxHp);
//        }*/
//        if (attr.maxHpNew != maxHpNew) {
//            types.add(AttributeConst.PropertiesType.MAX_HP);
//            values.add((int) attr.maxHpNew);
//        }
//        //5
//        if (attr.critical != critical) {
//            types.add(AttributeConst.PropertiesType.CRITICAL);
//            values.add(attr.critical);
//        }
//        //6
//        if (attr.critFix != critFix) {
//            types.add(AttributeConst.PropertiesType.CRIT_FIX);
//            values.add(attr.critFix);
//        }
//        //7
//        if (attr.critResist != critResist) {
//            types.add(AttributeConst.PropertiesType.CRIT_RESIST);
//            values.add(attr.critResist);
//        }
//        //8
//        if (attr.zsHurtAdd != zsHurtAdd) {
//            types.add(AttributeConst.PropertiesType.ZS_HURT_ADD);
//            values.add(attr.zsHurtAdd);
//        }
//        //9
//        if (attr.fsHurtAdd != fsHurtAdd) {
//            types.add(AttributeConst.PropertiesType.FS_HURT_ADD);
//            values.add(attr.fsHurtAdd);
//        }
//        //10
//        if (attr.dsHurtAdd != dsHurtAdd) {
//            types.add(AttributeConst.PropertiesType.DS_HURT_ADD);
//            values.add(attr.dsHurtAdd);
//        }
//        //11
//        if (attr.zsHurtedRelief != zsHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.ZS_HURTED_RELIEF);
//            values.add(attr.zsHurtedRelief);
//        }
//        //12
//        if (attr.fsHurtedRelief != fsHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.FS_HURTED_RELIEF);
//            values.add(attr.fsHurtedRelief);
//        }
//        //13
//        if (attr.dsHurtedRelief != dsHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.DS_HURTED_RELIEF);
//            values.add(attr.dsHurtedRelief);
//        }
//        //14
//        if (attr.monHurtAdd != monHurtAdd) {
//            types.add(AttributeConst.PropertiesType.MON_HURT_ADD);
//            values.add(attr.monHurtAdd);
//        }
//        //15
//        if (attr.monHurtedRelief != monHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.MON_HURTED_RELIEF);
//            values.add(attr.monHurtedRelief);
//        }
//        //16
//        if (attr.monCritical != monCritical) {
//            types.add(AttributeConst.PropertiesType.MON_CRITICAL);
//            values.add(attr.monCritical);
//        }
//        //17
//        if (attr.monCritFix != monCritFix) {
//            types.add(AttributeConst.PropertiesType.MON_CRIT_FIX);
//            values.add(attr.monCritFix);
//        }
//        //18
//        if (attr.relief != relief) {
//            types.add(AttributeConst.PropertiesType.RELIEF);
//            values.add(attr.relief);
//        }
//        //19
//        if (attr.toMonsterHurtFixAdd != toMonsterHurtFixAdd) {
//            types.add(AttributeConst.PropertiesType.TO_MONSTER_HURT_FIX_ADD);
//            values.add(attr.toMonsterHurtFixAdd);
//        }
//        //20
//        if (attr.innerPowerMax != innerPowerMax) {
//            types.add(AttributeConst.PropertiesType.INNER_POWER_MAX);
//            values.add(attr.innerPowerMax);
//        }
//        //21
//        if (attr.critrate + attr.basicCritrate != critrate + basicCritrate) {
//            types.add(AttributeConst.PropertiesType.CRITRATE);
//            values.add(attr.critrate + attr.basicCritrate);
//        }
//        //22
//        if (attr.innerRestore != innerRestore) {
//            types.add(AttributeConst.PropertiesType.INNER_RESTORE);
//            values.add(attr.innerRestore);
//        }
//        //23
//        if (attr.innerRelief != innerRelief) {
//            types.add(AttributeConst.PropertiesType.INNER_RELIEF);
//            values.add(attr.innerRelief);
//        }
//        //24
//        if (attr.hurtAdd != hurtAdd) {
//            types.add(AttributeConst.PropertiesType.HURT_ADD);
//            values.add(attr.hurtAdd);
//        }
//        //25
//        if (attr.ljholyAtk != ljholyAtk) {
//            types.add(AttributeConst.PropertiesType.LJHOLY_ATK);
//            values.add(attr.ljholyAtk);
//        }
//        //26
//        if (attr.ljHurtedRelief != ljHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.LJ_HURTED_RELIEF);
//            values.add(attr.ljHurtedRelief);
//        }
//        //27
//        if (attr.holyAtk != holyAtk) {
//            types.add(AttributeConst.PropertiesType.HOLY_ATK);
//            values.add(attr.holyAtk);
//        }
//        //28
//        if (attr.holyAdd != holyAdd) {
//            types.add(AttributeConst.PropertiesType.HOLY_ADD);
//            values.add(attr.holyAdd);
//        }
//        //29
//        if (attr.critFixAdd != critFixAdd) {
//            types.add(AttributeConst.PropertiesType.CRIT_FIX_ADD);
//            values.add(attr.critFixAdd);
//        }
//        //30
//        if (attr.critFixRelief != critFixRelief) {
//            types.add(AttributeConst.PropertiesType.CRIT_FIX_RELIEF);
//            values.add(attr.critFixRelief);
//        }
//        //31
//        if (attr.criticalPlus != criticalPlus) {
//            types.add(AttributeConst.PropertiesType.CRITCAL_PLUS);
//            values.add(attr.criticalPlus);
//        }
//        //32
//        if (attr.critResistPlus != critResistPlus) {
//            types.add(AttributeConst.PropertiesType.CRIT_RESIST_PLUS);
//            values.add(attr.critResistPlus);
//        }
//        //33
//        if (attr.hurtAddPlus != hurtAddPlus) {
//            types.add(AttributeConst.PropertiesType.HURT_ADD_PLUS);
//            values.add(attr.hurtAddPlus);
//        }
//        //34
//        if (attr.reliefPlus != reliefPlus) {
//            types.add(AttributeConst.PropertiesType.RELIEF_PLUS);
//            values.add(attr.reliefPlus);
//        }
//        //35
//        if (attr.deadatk != deadatk) {
//            types.add(AttributeConst.PropertiesType.DEAD_ATK);
//            values.add(attr.deadatk);
//        }
//        //36
//        if (attr.deadatkRelief != deadatkRelief) {
//            types.add(AttributeConst.PropertiesType.DEAD_ATK_RELIEF);
//            values.add(attr.deadatkRelief);
//        }
//        //37
//        if (attr.deadHurt != deadHurt) {
//            types.add(AttributeConst.PropertiesType.DEAD_HURT);
//            values.add(attr.deadHurt);
//        }
//        //38
//        if (attr.deadHurtAdd != deadHurtAdd) {
//            types.add(AttributeConst.PropertiesType.DEAD_HURT_ADD);
//            values.add(attr.deadHurtAdd);
//        }
//        //39
//        if (attr.deadHurtRelief != deadHurtRelief) {
//            types.add(AttributeConst.PropertiesType.DEAD_HURT_RELIEF);
//            values.add(attr.deadHurtRelief);
//        }
//        //40
//        if (attr.maxMagic != maxMagic) {
//            types.add(AttributeConst.PropertiesType.MAX_MAGIC);
//            values.add(attr.maxMagic);
//        }
//        //41
//        if (attr.dAtk != dAtk) {
//            types.add(AttributeConst.PropertiesType.D_ATK);
//            values.add(attr.dAtk);
//        }
//        //42
//        if (attr.dDef != dDef) {
//            types.add(AttributeConst.PropertiesType.D_DEF);
//            values.add(attr.dDef);
//        }
//        //43
//        if (attr.dRelief != dRelief) {
//            types.add(AttributeConst.PropertiesType.D_RELIEF);
//            values.add(attr.dRelief);
//        }
//        //44
//        if (attr.dHurtAdd != dHurtAdd) {
//            types.add(AttributeConst.PropertiesType.D_HURT_ADD);
//            values.add(attr.dHurtAdd);
//        }
//        //45
//        if (attr.fAtk != fAtk) {
//            types.add(AttributeConst.PropertiesType.F_ATK);
//            values.add(attr.fAtk);
//        }
//        //46
//        if (attr.fDef != fDef) {
//            types.add(AttributeConst.PropertiesType.F_DEF);
//            values.add(attr.fDef);
//        }
//        //47
//        if (attr.fRelief != fRelief) {
//            types.add(AttributeConst.PropertiesType.F_RELIEF);
//            values.add(attr.fRelief);
//        }
//        //48
//        if (attr.fHurtAdd != fHurtAdd) {
//            types.add(AttributeConst.PropertiesType.F_HURT_ADD);
//            values.add(attr.fHurtAdd);
//        }
//        //49
//        if (attr.sAtk != sAtk) {
//            types.add(AttributeConst.PropertiesType.S_ATK);
//            values.add(attr.sAtk);
//        }
//        //50
//        if (attr.sDef != sDef) {
//            types.add(AttributeConst.PropertiesType.S_DEF);
//            values.add(attr.sDef);
//        }
//        //51
//        if (attr.sRelief != sRelief) {
//            types.add(AttributeConst.PropertiesType.S_RELIEF);
//            values.add(attr.sRelief);
//        }
//        //52
//        if (attr.sHurtAdd != sHurtAdd) {
//            types.add(AttributeConst.PropertiesType.S_HURT_ADD);
//            values.add(attr.sHurtAdd);
//        }
//        //53
//        if (attr.hAtk != hAtk) {
//            types.add(AttributeConst.PropertiesType.H_ATK);
//            values.add(attr.hAtk);
//        }
//        //54
//        if (attr.hDef != hDef) {
//            types.add(AttributeConst.PropertiesType.H_DEF);
//            values.add(attr.hDef);
//        }
//        //55
//        if (attr.hRelief != hRelief) {
//            types.add(AttributeConst.PropertiesType.H_RELIEF);
//            values.add(attr.hRelief);
//        }
//        //56
//        if (attr.hHurtAdd != hHurtAdd) {
//            types.add(AttributeConst.PropertiesType.H_HURT_ADD);
//            values.add(attr.hHurtAdd);
//        }
//
//        boolean b = attr.fightPower == fightPower && (types.isEmpty() || values.isEmpty());
//        if (b) {
//            return null;
//        } else {
//            ResPlayerAttributeChangeMessage msg = new ResPlayerAttributeChangeMessage();
//            msg.setUid(role.getId());
//            msg.setAttributeType(types);
//            msg.setAttributeValue(values);
//            msg.setPower(attr.fightPower);
//            return msg;
//        }
//    }
//
//    public ResHeroAttributeChangeMessage compareHero(Attribute attr, Hero hero) {
//        List<Byte> types = new ArrayList<>();
//        List<Integer> values = new ArrayList<>();
//        //1
//        if (attr.attack != attack) {
//            types.add(AttributeConst.PropertiesType.ATTACK);
//            values.add(attr.attack);
//        }
//        //2
//        if (attr.phyDefence != phyDefence) {
//            types.add(AttributeConst.PropertiesType.PHY_DEFENCE);
//            values.add(attr.phyDefence);
//        }
//        //3
//        if (attr.magicDefence != magicDefence) {
//            types.add(AttributeConst.PropertiesType.MAGIC_DEFENCE);
//            values.add(attr.magicDefence);
//        }
//        //4
//        /*if (attr.maxHp != maxHp) {
//            types.add(AttributeConst.PropertiesType.MAX_HP);
//            values.add((int) attr.maxHp);
//        }*/
//        if (attr.maxHpNew != maxHpNew) {
//            types.add(AttributeConst.PropertiesType.MAX_HP);
//            values.add((int) attr.maxHpNew);
//        }
//        //5
//        if (attr.critical != critical) {
//            types.add(AttributeConst.PropertiesType.CRITICAL);
//            values.add(attr.critical);
//        }
//        //6
//        if (attr.critFix != critFix) {
//            types.add(AttributeConst.PropertiesType.CRIT_FIX);
//            values.add(attr.critFix);
//        }
//        //7
//        if (attr.critResist != critResist) {
//            types.add(AttributeConst.PropertiesType.CRIT_RESIST);
//            values.add(attr.critResist);
//        }
//        //8
//        if (attr.zsHurtAdd != zsHurtAdd) {
//            types.add(AttributeConst.PropertiesType.ZS_HURT_ADD);
//            values.add(attr.zsHurtAdd);
//        }
//        //9
//        if (attr.fsHurtAdd != fsHurtAdd) {
//            types.add(AttributeConst.PropertiesType.FS_HURT_ADD);
//            values.add(attr.fsHurtAdd);
//        }
//        //10
//        if (attr.dsHurtAdd != dsHurtAdd) {
//            types.add(AttributeConst.PropertiesType.DS_HURT_ADD);
//            values.add(attr.dsHurtAdd);
//        }
//        //11
//        if (attr.zsHurtedRelief != zsHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.ZS_HURTED_RELIEF);
//            values.add(attr.zsHurtedRelief);
//        }
//        //12
//        if (attr.fsHurtedRelief != fsHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.FS_HURTED_RELIEF);
//            values.add(attr.fsHurtedRelief);
//        }
//        //13
//        if (attr.dsHurtedRelief != dsHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.DS_HURTED_RELIEF);
//            values.add(attr.dsHurtedRelief);
//        }
//        //14
//        if (attr.monHurtAdd != monHurtAdd) {
//            types.add(AttributeConst.PropertiesType.MON_HURT_ADD);
//            values.add(attr.monHurtAdd);
//        }
//        //15
//        if (attr.monHurtedRelief != monHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.MON_HURTED_RELIEF);
//            values.add(attr.monHurtedRelief);
//        }
//        //16
//        if (attr.monCritical != monCritical) {
//            types.add(AttributeConst.PropertiesType.MON_CRITICAL);
//            values.add(attr.monCritical);
//        }
//        //17
//        if (attr.monCritFix != monCritFix) {
//            types.add(AttributeConst.PropertiesType.MON_CRIT_FIX);
//            values.add(attr.monCritFix);
//        }
//        //18
//        if (attr.relief != relief) {
//            types.add(AttributeConst.PropertiesType.RELIEF);
//            values.add(attr.relief);
//        }
//        //19
//        if (attr.toMonsterHurtFixAdd != toMonsterHurtFixAdd) {
//            types.add(AttributeConst.PropertiesType.TO_MONSTER_HURT_FIX_ADD);
//            values.add(attr.toMonsterHurtFixAdd);
//        }
//        //20
//        if (attr.innerPowerMax != innerPowerMax) {
//            types.add(AttributeConst.PropertiesType.INNER_POWER_MAX);
//            values.add(attr.innerPowerMax);
//        }
//        //21
//        if (attr.critrate + attr.basicCritrate != critrate + basicCritrate) {
//            types.add(AttributeConst.PropertiesType.CRITRATE);
//            values.add(attr.critrate + attr.basicCritrate);
//        }
//        //22
//        if (attr.innerRestore != innerRestore) {
//            types.add(AttributeConst.PropertiesType.INNER_RESTORE);
//            values.add(attr.innerRestore);
//        }
//        //23
//        if (attr.innerRelief != innerRelief) {
//            types.add(AttributeConst.PropertiesType.INNER_RELIEF);
//            values.add(attr.innerRelief);
//        }
//        //24
//        if (attr.hurtAdd != hurtAdd) {
//            types.add(AttributeConst.PropertiesType.HURT_ADD);
//            values.add(attr.hurtAdd);
//        }
//        //25
//        if (attr.ljholyAtk != ljholyAtk) {
//            types.add(AttributeConst.PropertiesType.LJHOLY_ATK);
//            values.add(attr.ljholyAtk);
//        }
//        //26
//        if (attr.ljHurtedRelief != ljHurtedRelief) {
//            types.add(AttributeConst.PropertiesType.LJ_HURTED_RELIEF);
//            values.add(attr.ljHurtedRelief);
//        }
//        //27
//        if (attr.holyAtk != holyAtk) {
//            types.add(AttributeConst.PropertiesType.HOLY_ATK);
//            values.add(attr.holyAtk);
//        }
//        //28
//        if (attr.holyAdd != holyAdd) {
//            types.add(AttributeConst.PropertiesType.HOLY_ADD);
//            values.add(attr.holyAdd);
//        }
//        //29
//        if (attr.critFixAdd != critFixAdd) {
//            types.add(AttributeConst.PropertiesType.CRIT_FIX_ADD);
//            values.add(attr.critFixAdd);
//        }
//        //30
//        if (attr.critFixRelief != critFixRelief) {
//            types.add(AttributeConst.PropertiesType.CRIT_FIX_RELIEF);
//            values.add(attr.critFixRelief);
//        }
//        //31
//        if (attr.criticalPlus != criticalPlus) {
//            types.add(AttributeConst.PropertiesType.CRITCAL_PLUS);
//            values.add(attr.criticalPlus);
//        }
//        //32
//        if (attr.critResistPlus != critResistPlus) {
//            types.add(AttributeConst.PropertiesType.CRIT_RESIST_PLUS);
//            values.add(attr.critResistPlus);
//        }
//        //33
//        if (attr.hurtAddPlus != hurtAddPlus) {
//            types.add(AttributeConst.PropertiesType.HURT_ADD_PLUS);
//            values.add(attr.hurtAddPlus);
//        }
//        //34
//        if (attr.reliefPlus != reliefPlus) {
//            types.add(AttributeConst.PropertiesType.RELIEF_PLUS);
//            values.add(attr.reliefPlus);
//        }
//        //35
//        if (attr.deadatk != deadatk) {
//            types.add(AttributeConst.PropertiesType.DEAD_ATK);
//            values.add(attr.deadatk);
//        }
//        //36
//        if (attr.deadatkRelief != deadatkRelief) {
//            types.add(AttributeConst.PropertiesType.DEAD_ATK_RELIEF);
//            values.add(attr.deadatkRelief);
//        }
//        //37
//        if (attr.deadHurt != deadHurt) {
//            types.add(AttributeConst.PropertiesType.DEAD_HURT);
//            values.add(attr.deadHurt);
//        }
//        //38
//        if (attr.deadHurtAdd != deadHurtAdd) {
//            types.add(AttributeConst.PropertiesType.DEAD_HURT_ADD);
//            values.add(attr.deadHurtAdd);
//        }
//        //39
//        if (attr.deadHurtRelief != deadHurtRelief) {
//            types.add(AttributeConst.PropertiesType.DEAD_HURT_RELIEF);
//            values.add(attr.deadHurtRelief);
//        }
//        //40
//        if (attr.maxMagic != maxMagic) {
//            types.add(AttributeConst.PropertiesType.MAX_MAGIC);
//            values.add(attr.maxMagic);
//        }
//        //41
//        if (attr.dAtk != dAtk) {
//            types.add(AttributeConst.PropertiesType.D_ATK);
//            values.add(attr.dAtk);
//        }
//        //42
//        if (attr.dDef != dDef) {
//            types.add(AttributeConst.PropertiesType.D_DEF);
//            values.add(attr.dDef);
//        }
//        //43
//        if (attr.dRelief != dRelief) {
//            types.add(AttributeConst.PropertiesType.D_RELIEF);
//            values.add(attr.dRelief);
//        }
//        //44
//        if (attr.dHurtAdd != dHurtAdd) {
//            types.add(AttributeConst.PropertiesType.D_HURT_ADD);
//            values.add(attr.dHurtAdd);
//        }
//        //45
//        if (attr.fAtk != fAtk) {
//            types.add(AttributeConst.PropertiesType.F_ATK);
//            values.add(attr.fAtk);
//        }
//        //46
//        if (attr.fDef != fDef) {
//            types.add(AttributeConst.PropertiesType.F_DEF);
//            values.add(attr.fDef);
//        }
//        //47
//        if (attr.fRelief != fRelief) {
//            types.add(AttributeConst.PropertiesType.F_RELIEF);
//            values.add(attr.fRelief);
//        }
//        //48
//        if (attr.fHurtAdd != fHurtAdd) {
//            types.add(AttributeConst.PropertiesType.F_HURT_ADD);
//            values.add(attr.fHurtAdd);
//        }
//        //49
//        if (attr.sAtk != sAtk) {
//            types.add(AttributeConst.PropertiesType.S_ATK);
//            values.add(attr.sAtk);
//        }
//        //50
//        if (attr.sDef != sDef) {
//            types.add(AttributeConst.PropertiesType.S_DEF);
//            values.add(attr.sDef);
//        }
//        //51
//        if (attr.sRelief != sRelief) {
//            types.add(AttributeConst.PropertiesType.S_RELIEF);
//            values.add(attr.sRelief);
//        }
//        //52
//        if (attr.sHurtAdd != sHurtAdd) {
//            types.add(AttributeConst.PropertiesType.S_HURT_ADD);
//            values.add(attr.sHurtAdd);
//        }
//        //53
//        if (attr.hAtk != hAtk) {
//            types.add(AttributeConst.PropertiesType.H_ATK);
//            values.add(attr.hAtk);
//        }
//        //54
//        if (attr.hDef != hDef) {
//            types.add(AttributeConst.PropertiesType.H_DEF);
//            values.add(attr.hDef);
//        }
//        //55
//        if (attr.hRelief != hRelief) {
//            types.add(AttributeConst.PropertiesType.H_RELIEF);
//            values.add(attr.hRelief);
//        }
//        //56
//        if (attr.hHurtAdd != hHurtAdd) {
//            types.add(AttributeConst.PropertiesType.H_HURT_ADD);
//            values.add(attr.hHurtAdd);
//        }
//
//        boolean b = attr.fightPower == fightPower && (types.isEmpty() || values.isEmpty());
//        if (b) {
//            return null;
//        } else {
//            ResHeroAttributeChangeMessage msg = new ResHeroAttributeChangeMessage();
//            msg.setUid(hero.getId());
//            msg.setAttributeType(types);
//            msg.setAttributeValue(values);
//            msg.setPower(attr.fightPower);
//            return msg;
//        }
//    }
//
//    public Attribute copy(Attribute attr) {
//
//        attack = attr.attack;
//
//        phyDefence = attr.phyDefence;
//
//        magicDefence = attr.magicDefence;
//
//        maxHpNew = attr.maxHpNew;
//
//        critical = attr.critical + attr.criticalPlus;
//
//        critFix = attr.critFix;
//
//        critResist = attr.critResist + attr.critResistPlus;
//
//        fightPower = attr.fightPower;
//
//        //------------------- PVP 属性 -------------------------
//
//        zsHurtAdd = attr.zsHurtAdd;
//
//        fsHurtAdd = attr.fsHurtAdd;
//
//        dsHurtAdd = attr.dsHurtAdd;
//
//        zsHurtedRelief = attr.zsHurtedRelief;
//
//        fsHurtedRelief = attr.fsHurtedRelief;
//
//        dsHurtedRelief = attr.dsHurtedRelief;
//
//        //--------------- 只对怪物生效 --------------------
//
//        monHurtAdd = attr.monHurtAdd;
//
//        monHurtedRelief = attr.monHurtedRelief;
//
//        monCritical = attr.monCritical;
//
//        monCritFix = attr.monCritFix;
//
//        toMonsterHurtFixAdd = attr.toMonsterHurtFixAdd;
//
//        relief = attr.relief + attr.reliefPlus;
//
//        pointedToMonsterHurtAdd.putAll(attr.pointedToMonsterHurtAdd);
//
//        pointedFromMonsterHurtDef.putAll(attr.pointedFromMonsterHurtDef);
//
//        pointedToMonsterBsAdd.putAll(attr.pointedToMonsterBsAdd);
//
//        attackSpeed = attr.attackSpeed;
//
//        fightRec = attr.fightRec;
//
//        weiwangHurtAdd = attr.weiwangHurtAdd;
//
//        innerPowerMax = attr.innerPowerMax;
//
//        innerRelief = attr.innerRelief;
//
//        innerRestore = attr.innerRestore;
//
//        currentInnerPower = attr.currentInnerPower;
//
//        hurtAdd = attr.hurtAdd + attr.hurtAddPlus;
//
//        critrate = attr.critrate + attr.basicCritrate;
//
//        ljholyAtk = attr.ljholyAtk;
//
//        ljHurtedRelief = attr.ljHurtedRelief;
//
//        holyAtk = attr.holyAtk;
//
//        holyAdd = attr.holyAdd;
//
//        critFixAdd = attr.critFixAdd;
//
//        critFixRelief = attr.critFixRelief;
//
//        deadatk = attr.deadatk;
//
//        deadatkRelief = attr.deadatkRelief;
//
//        deadHurt = attr.deadHurt;
//
//        deadHurtAdd = attr.deadHurtAdd;
//
//        deadHurtRelief = attr.deadHurtRelief;
//
//        maxMagic = attr.maxMagic;
//
//        guard = attr.guard;
//
//        dAtk = attr.dAtk;
//
//        dDef = attr.dDef;
//
//        dRelief = attr.dRelief;
//
//        dHurtAdd = attr.dHurtAdd;
//
//        fAtk = attr.fAtk;
//
//        fDef = attr.fDef;
//
//        fRelief = attr.fRelief;
//
//        fHurtAdd = attr.fHurtAdd;
//
//        sAtk = attr.sAtk;
//
//        sDef = attr.sDef;
//
//        sRelief = attr.sRelief;
//
//        sHurtAdd = attr.sHurtAdd;
//
//        hAtk = attr.hAtk;
//
//        hDef = attr.hDef;
//
//        hRelief = attr.hRelief;
//
//        hHurtAdd = attr.hHurtAdd;
//
//        return this;
//
//    }
//
//    public AttributeDTO toDTO() {
//
//        AttributeDTO dto = new AttributeDTO();
//        dto.attack = this.attack;
//
//        dto.phyDefence = this.phyDefence;
//
//        dto.magicDefence = this.magicDefence;
//
//        dto.maxHpNew = this.maxHpNew;
//
//        dto.critical = this.critical + this.criticalPlus;
//
//        dto.critFix = this.critFix;
//
//        dto.critResist = this.critResist + this.critResistPlus;
//
//        dto.fightPower = this.fightPower;
//
//        //------------------- PVP 属性 -------------------------
//
//        dto.zsHurtAdd = this.zsHurtAdd;
//
//        dto.fsHurtAdd = this.fsHurtAdd;
//
//        dto.dsHurtAdd = this.dsHurtAdd;
//
//        dto.zsHurtedRelief = this.zsHurtedRelief;
//
//        dto.fsHurtedRelief = this.fsHurtedRelief;
//
//        dto.dsHurtedRelief = this.dsHurtedRelief;
//
//        //--------------- 只对怪物生效 --------------------
//
//        dto.monHurtAdd = this.monHurtAdd;
//
//        dto.monHurtedRelief = this.monHurtedRelief;
//
//        dto.monCritical = this.monCritical;
//
//        dto.monCritFix = this.monCritFix;
//
//        dto.toMonsterHurtFixAdd = this.toMonsterHurtFixAdd;
//
//        dto.relief = this.relief + this.reliefPlus;
//
//        dto.pointedToMonsterHurtAdd.putAll(this.pointedToMonsterHurtAdd);
//        dto.pointedFromMonsterHurtDef.putAll(this.pointedFromMonsterHurtDef);
//        dto.pointedToMonsterBsAdd.putAll(this.pointedToMonsterBsAdd);
//
//        dto.attackSpeed = this.attackSpeed;
//
//        dto.fightRec = this.fightRec;
//
//        dto.weiwangHurtAdd = this.weiwangHurtAdd;
//
//        dto.innerPowerMax = this.innerPowerMax;
//
//        dto.innerRelief = this.innerRelief;
//
//        dto.innerRestore = this.innerRestore;
//
//        dto.currentInnerPower = this.currentInnerPower;
//
//        dto.hurtAdd = this.hurtAdd + this.hurtAddPlus;
//
//        dto.critrate = this.critrate + this.basicCritrate;
//
//        dto.ljholyAtk = this.ljholyAtk;
//
//        dto.ljHurtedRelief = this.ljHurtedRelief;
//
//        dto.holyAtk = this.holyAtk;
//
//        dto.holyAdd = this.holyAdd;
//
//        dto.critFixAdd = this.critFixAdd;
//
//        dto.critFixRelief = this.critFixRelief;
//
//        dto.deadatk = this.deadatk;
//
//        dto.deadatkRelief = this.deadatkRelief;
//
//        dto.deadHurt = this.deadHurt;
//
//        dto.deadHurtAdd = this.deadHurtAdd;
//
//        dto.deadHurtRelief = this.deadHurtRelief;
//
//        dto.maxMagic = this.maxMagic;
//
//        dto.guard = this.guard;
//
//        //--------------- 元素属性 --------------------
//        dto.dAtk = this.dAtk;
//
//        dto.dDef = this.dDef;
//
//        dto.dRelief = this.dRelief;
//
//        dto.dHurtAdd = this.dHurtAdd;
//
//        dto.fAtk = this.fAtk;
//
//        dto.fDef = this.fDef;
//
//        dto.fRelief = this.fRelief;
//
//        dto.fHurtAdd = this.fHurtAdd;
//
//        dto.sAtk = this.sAtk;
//
//        dto.sDef = this.sDef;
//
//        dto.sRelief = this.sRelief;
//
//        dto.sHurtAdd = this.sHurtAdd;
//
//        dto.hAtk = this.hAtk;
//
//        dto.hDef = this.hDef;
//
//        dto.hRelief = this.hRelief;
//
//        dto.hHurtAdd = this.hHurtAdd;
//
//        return dto;
//    }
//
//    public void copy(AttributeDTO dto) {
//
//        this.attack = dto.attack;
//
//        this.phyDefence = dto.phyDefence;
//
//        this.magicDefence = dto.magicDefence;
//
//        this.maxHpNew = dto.maxHpNew;
//
//        this.critical = dto.critical;
//
//        this.critFix = dto.critFix;
//
//        this.critResist = dto.critResist;
//
//        this.fightPower = dto.fightPower;
//
//        //------------------- PVP 属性 -------------------------
//
//        this.zsHurtAdd = dto.zsHurtAdd;
//
//        this.fsHurtAdd = dto.fsHurtAdd;
//
//        this.dsHurtAdd = dto.dsHurtAdd;
//
//        this.zsHurtedRelief = dto.zsHurtedRelief;
//
//        this.fsHurtedRelief = dto.fsHurtedRelief;
//
//        this.dsHurtedRelief = dto.dsHurtedRelief;
//
//        //--------------- 只对怪物生效 --------------------
//
//        this.monHurtAdd = dto.monHurtAdd;
//
//        this.monHurtedRelief = dto.monHurtedRelief;
//
//        this.monCritical = dto.monCritical;
//
//        this.monCritFix = dto.monCritFix;
//
//        this.toMonsterHurtFixAdd = dto.toMonsterHurtFixAdd;
//
//        this.relief = dto.relief;
//
//        this.pointedToMonsterHurtAdd.putAll(dto.pointedToMonsterHurtAdd);
//
//        this.pointedFromMonsterHurtDef.putAll(dto.pointedFromMonsterHurtDef);
//
//        this.pointedToMonsterBsAdd.putAll(dto.pointedToMonsterBsAdd);
//
//        this.attackSpeed = dto.attackSpeed;
//
//        this.fightRec = dto.fightRec;
//
//        this.weiwangHurtAdd = dto.weiwangHurtAdd;
//
//        this.innerPowerMax = dto.innerPowerMax;
//
//        this.innerRelief = dto.innerRelief;
//
//        this.innerRestore = dto.innerRestore;
//
//        this.currentInnerPower = dto.currentInnerPower;
//
//        this.hurtAdd = dto.hurtAdd;
//
//        this.critrate = dto.critrate;
//
//        this.ljholyAtk = dto.ljholyAtk;
//
//        this.ljHurtedRelief = dto.ljHurtedRelief;
//
//        this.holyAtk = dto.holyAtk;
//
//        this.holyAdd = dto.holyAdd;
//
//        this.critFixAdd = dto.critFixAdd;
//
//        this.critFixRelief = dto.critFixRelief;
//
//        this.deadatk = dto.deadatk;
//
//        this.deadatkRelief = dto.deadatkRelief;
//
//        this.deadHurt = dto.deadHurt;
//
//        this.deadHurtAdd = dto.deadHurtAdd;
//
//        this.deadHurtRelief = dto.deadHurtRelief;
//
//        this.maxMagic = dto.maxMagic;
//
//        this.guard = dto.guard;
//
//        this.dAtk = dto.dAtk;
//
//        this.dDef = dto.dDef;
//
//        this.dRelief = dto.dRelief;
//
//        this.dHurtAdd = dto.dHurtAdd;
//
//        this.fAtk = dto.fAtk;
//
//        this.fDef = dto.fDef;
//
//        this.fRelief = dto.fRelief;
//
//        this.fHurtAdd = dto.fHurtAdd;
//
//        this.sAtk = dto.sAtk;
//
//        this.sDef = dto.sDef;
//
//        this.sRelief = dto.sRelief;
//
//        this.sHurtAdd = dto.sHurtAdd;
//
//        this.hAtk = dto.hAtk;
//
//        this.hDef = dto.hDef;
//
//        this.hRelief = dto.hRelief;
//
//        this.hHurtAdd = dto.hHurtAdd;
//    }

    public void miniFixAdd(Attribute attr) {

        attack += attr.attack;
        maxHpNew += attr.maxHpNew;
        phyDefence += attr.phyDefence;
        magicDefence += attr.magicDefence;
    }

    public void mergeFixAdd(Attribute attr) {
        attack += attr.attack;

        phyDefence += attr.phyDefence;

        magicDefence += attr.magicDefence;

        maxHpNew += attr.maxHpNew;

        critical += attr.critical;

        criticalPlus += attr.criticalPlus;

        critFix += attr.critFix;

        critResist += attr.critResist;

        critResistPlus += attr.critResistPlus;

        //------------------- PVP 属性 -------------------------

        zsHurtAdd += attr.zsHurtAdd;

        fsHurtAdd += attr.fsHurtAdd;

        dsHurtAdd += attr.dsHurtAdd;

        zsHurtedRelief += attr.zsHurtedRelief;

        fsHurtedRelief += attr.fsHurtedRelief;

        dsHurtedRelief += attr.dsHurtedRelief;

        //--------------- 只对怪物生效 --------------------

        monHurtAdd += attr.monHurtAdd;

        monHurtedRelief += attr.monHurtedRelief;

        monCritical += attr.monCritical;

        monCritFix += attr.monCritFix;

        toMonsterHurtFixAdd += attr.toMonsterHurtFixAdd;

        relief += attr.relief;

        reliefPlus += attr.reliefPlus;

        for (Map.Entry<Integer, Integer> entry : attr.pointedToMonsterHurtAdd.entrySet()) {
            int value = pointedToMonsterHurtAdd.getOrDefault(entry.getKey(), 0);
            pointedToMonsterHurtAdd.put(entry.getKey(), value + entry.getValue());
        }

        for (Map.Entry<Integer, Integer> entry : attr.pointedFromMonsterHurtDef.entrySet()) {
            int value = pointedFromMonsterHurtDef.getOrDefault(entry.getKey(), 0);
            pointedFromMonsterHurtDef.put(entry.getKey(), value + entry.getValue());
        }

        for (Map.Entry<Integer, Integer> entry : attr.pointedToMonsterBsAdd.entrySet()) {
            int value = pointedToMonsterBsAdd.getOrDefault(entry.getKey(), 0);
            pointedToMonsterBsAdd.put(entry.getKey(), value + entry.getValue());
        }

        attackSpeed += attr.attackSpeed;

        fightRec += attr.fightRec;

        weiwangHurtAdd += attr.weiwangHurtAdd;

        innerPowerMax += attr.innerPowerMax;

        innerRelief += attr.innerRelief;

        innerRestore += attr.innerRestore;

        hurtAdd += attr.hurtAdd;

        hurtAddPlus += attr.hurtAddPlus;

        critrate += attr.critrate;

        basicCritrate += attr.basicCritrate;

        ljholyAtk += attr.ljholyAtk;

        ljHurtedRelief += attr.ljHurtedRelief;

        holyAtk += attr.holyAtk;

        holyAdd += attr.holyAdd;

        critFixAdd += attr.critFixAdd;

        critFixRelief += attr.critFixRelief;

        deadatk += attr.deadatk;

        deadatkRelief += attr.deadatkRelief;

        deadHurt += attr.deadHurt;

        deadHurtAdd += attr.deadHurtAdd;

        deadHurtRelief += attr.deadHurtRelief;

        maxMagic += attr.maxMagic;

        guard += attr.guard;

        dAtk += attr.dAtk;

        dDef += attr.dDef;

        dRelief += attr.dRelief;

        dHurtAdd += attr.dHurtAdd;

        fAtk += attr.fAtk;

        fDef += attr.fDef;

        fRelief += attr.fRelief;

        fHurtAdd += attr.fHurtAdd;

        sAtk += attr.sAtk;

        sDef += attr.sDef;

        sRelief += attr.sRelief;

        sHurtAdd += attr.sHurtAdd;

        hAtk += attr.hAtk;

        hDef += attr.hDef;

        hRelief += attr.hRelief;

        hHurtAdd += attr.hHurtAdd;
    }

    public void mergePercentAdd(Attribute attr) {
        attack += attr.attack;

        maxHpNew += attr.maxHpNew;

        phyDefence += attr.phyDefence;

        magicDefence += attr.magicDefence;
    }
}
