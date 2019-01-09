package com.nemo.game.system.attr.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public interface AttrinbuteConst {
    Logger LOGGER = LoggerFactory.getLogger(AttrinbuteConst.class);

    class PropertiesType {
        private static PropertiesType instance;

        private PropertiesType(){}

        public static PropertiesType getInstance() {
            if(instance == null) {
                instance = new PropertiesType();
            }
            return instance;
        }

        /**
         * 生命值
         */
        byte hp = 1;

        /**
         * 魔法值
         */
        byte mp = 2;

        /**
         * 物攻下限
         */
        byte phyatk = 3;

        /**
         * 物攻上限
         */
        byte phyatkmax = 4;

        /**
         * 魔攻下限
         */
        byte magicatk = 5;

        /**
         * 魔攻上限
         */
        byte magicatkmax = 6;

        /**
         * 道攻下限
         */
        byte taoatk = 7;

        /**
         * 道攻上限
         */
        byte taoatkmax = 8;

        /**
         * 物防下限
         */
        byte def = 9;

        /**
         * 物防上限
         */
        byte defmax = 10;

        /**
         * 魔防下限
         */
        byte mdef = 11;

        /**
         * 魔防上限
         */
        byte mdefmax = 12;

        /**
         * 暴击概率
         */
        byte critical = 13;

        /**
         * 韧性（抗暴击概率）
         */
        byte tenacity = 14;

        /**
         * 暴击伤害
         */
        byte critFix = 15;

        /**
         * 暴伤减免
         */
        byte cridreduce = 16;

        /**
         * 物理免伤
         */
        byte injury = 17;

        /**
         * 魔法免伤
         */
        byte minjury = 18;

        /**
         * 攻速
         */
        byte speed = 19;

        /**
         * 准确
         */
        byte dodge = 20;

        /**
         * 闪避
         */
        byte mis = 21;

        /**
         * 幸运
         */
        byte luck = 22;

        /**
         * 神圣伤害
         */
        byte holyatk = 23;

        /**
         * 3职业神佑
         */
        byte bless = 24;

        /**
         * 噬血概率
         */
        byte vampirerate = 25;

        /**
         * 噬血
         */
        byte vampire = 26;

        /**
         * 增加伤害
         */
        byte damageatt = 27;

        /**
         * 减少伤害
         */
        byte damagedef = 28;

        /**
         * 对敌对阵营伤害增加
         */
        byte campatt = 29;

        /**
         * 对敌对阵营伤害降低
         */
        byte campdef = 30;

        /**
         * 对怪物伤害增加
         */
        byte monHurtAdd = 31;

        /**
         * 受怪物伤害降低
         */
        byte monHurtedRelief = 32;

        /**
         * 对战士伤害增加
         */
        byte zsHurtAdd = 33;

        /**
         * 受战士伤害降低
         */
        byte zsHurtedRelief = 34;

        /**
         * 对法师伤害增加
         */
        byte fsHurtAdd = 35;

        /**
         * 受法师伤害降低
         */
        byte fsHurtedRelief = 36;

        /**
         * 对道士伤害增加
         */
        byte dsHurtAdd = 37;

        /**
         * 受道士伤害降低
         */
        byte dsHurtedRelief = 38;

        /**
         * 服务器用，战斗力
         */
        byte fightPower = 39;

        public byte getHp() {
            return hp;
        }

        public void setHp(byte hp) {
            this.hp = hp;
        }

        public byte getMp() {
            return mp;
        }

        public void setMp(byte mp) {
            this.mp = mp;
        }

        public static void setInstance(PropertiesType instance) {
            PropertiesType.instance = instance;
        }

        public byte getPhyatk() {
            return phyatk;
        }

        public void setPhyatk(byte phyatk) {
            this.phyatk = phyatk;
        }

        public byte getPhyatkmax() {
            return phyatkmax;
        }

        public void setPhyatkmax(byte phyatkmax) {
            this.phyatkmax = phyatkmax;
        }

        public byte getMagicatk() {
            return magicatk;
        }

        public void setMagicatk(byte magicatk) {
            this.magicatk = magicatk;
        }

        public byte getMagicatkmax() {
            return magicatkmax;
        }

        public void setMagicatkmax(byte magicatkmax) {
            this.magicatkmax = magicatkmax;
        }

        public byte getTaoatk() {
            return taoatk;
        }

        public void setTaoatk(byte taoatk) {
            this.taoatk = taoatk;
        }

        public byte getTaoatkmax() {
            return taoatkmax;
        }

        public void setTaoatkmax(byte taoatkmax) {
            this.taoatkmax = taoatkmax;
        }

        public byte getDef() {
            return def;
        }

        public void setDef(byte def) {
            this.def = def;
        }

        public byte getDefmax() {
            return defmax;
        }

        public void setDefmax(byte defmax) {
            this.defmax = defmax;
        }

        public byte getMdef() {
            return mdef;
        }

        public void setMdef(byte mdef) {
            this.mdef = mdef;
        }

        public byte getMdefmax() {
            return mdefmax;
        }

        public void setMdefmax(byte mdefmax) {
            this.mdefmax = mdefmax;
        }

        public byte getCritical() {
            return critical;
        }

        public void setCritical(byte critical) {
            this.critical = critical;
        }

        public byte getTenacity() {
            return tenacity;
        }

        public void setTenacity(byte tenacity) {
            this.tenacity = tenacity;
        }

        public byte getCritFix() {
            return critFix;
        }

        public void setCritFix(byte critFix) {
            this.critFix = critFix;
        }

        public byte getCridreduce() {
            return cridreduce;
        }

        public void setCridreduce(byte cridreduce) {
            this.cridreduce = cridreduce;
        }

        public byte getInjury() {
            return injury;
        }

        public void setInjury(byte injury) {
            this.injury = injury;
        }

        public byte getMinjury() {
            return minjury;
        }

        public void setMinjury(byte minjury) {
            this.minjury = minjury;
        }

        public byte getSpeed() {
            return speed;
        }

        public void setSpeed(byte speed) {
            this.speed = speed;
        }

        public byte getDodge() {
            return dodge;
        }

        public void setDodge(byte dodge) {
            this.dodge = dodge;
        }

        public byte getMis() {
            return mis;
        }

        public void setMis(byte mis) {
            this.mis = mis;
        }

        public byte getLuck() {
            return luck;
        }

        public void setLuck(byte luck) {
            this.luck = luck;
        }

        public byte getHolyatk() {
            return holyatk;
        }

        public void setHolyatk(byte holyatk) {
            this.holyatk = holyatk;
        }

        public byte getBless() {
            return bless;
        }

        public void setBless(byte bless) {
            this.bless = bless;
        }

        public byte getVampirerate() {
            return vampirerate;
        }

        public void setVampirerate(byte vampirerate) {
            this.vampirerate = vampirerate;
        }

        public byte getVampire() {
            return vampire;
        }

        public void setVampire(byte vampire) {
            this.vampire = vampire;
        }

        public byte getDamageatt() {
            return damageatt;
        }

        public void setDamageatt(byte damageatt) {
            this.damageatt = damageatt;
        }

        public byte getDamagedef() {
            return damagedef;
        }

        public void setDamagedef(byte damagedef) {
            this.damagedef = damagedef;
        }

        public byte getCampatt() {
            return campatt;
        }

        public void setCampatt(byte campatt) {
            this.campatt = campatt;
        }

        public byte getCampdef() {
            return campdef;
        }

        public void setCampdef(byte campdef) {
            this.campdef = campdef;
        }

        public byte getMonHurtAdd() {
            return monHurtAdd;
        }

        public void setMonHurtAdd(byte monHurtAdd) {
            this.monHurtAdd = monHurtAdd;
        }

        public byte getMonHurtedRelief() {
            return monHurtedRelief;
        }

        public void setMonHurtedRelief(byte monHurtedRelief) {
            this.monHurtedRelief = monHurtedRelief;
        }

        public byte getZsHurtAdd() {
            return zsHurtAdd;
        }

        public void setZsHurtAdd(byte zsHurtAdd) {
            this.zsHurtAdd = zsHurtAdd;
        }

        public byte getZsHurtedRelief() {
            return zsHurtedRelief;
        }

        public void setZsHurtedRelief(byte zsHurtedRelief) {
            this.zsHurtedRelief = zsHurtedRelief;
        }

        public byte getFsHurtAdd() {
            return fsHurtAdd;
        }

        public void setFsHurtAdd(byte fsHurtAdd) {
            this.fsHurtAdd = fsHurtAdd;
        }

        public byte getFsHurtedRelief() {
            return fsHurtedRelief;
        }

        public void setFsHurtedRelief(byte fsHurtedRelief) {
            this.fsHurtedRelief = fsHurtedRelief;
        }

        public byte getDsHurtAdd() {
            return dsHurtAdd;
        }

        public void setDsHurtAdd(byte dsHurtAdd) {
            this.dsHurtAdd = dsHurtAdd;
        }

        public byte getDsHurtedRelief() {
            return dsHurtedRelief;
        }

        public void setDsHurtedRelief(byte dsHurtedRelief) {
            this.dsHurtedRelief = dsHurtedRelief;
        }

        public byte getFightPower() {
            return fightPower;
        }

        public void setFightPower(byte fightPower) {
            this.fightPower = fightPower;
        }

        //获取各个属性值的定义值
        public static byte getPropertyType(String name) throws Exception {
            try {
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = PropertiesType.class.getMethod("get" + name);
                byte result = (byte)method.invoke(PropertiesType.getInstance());
                return result;
            } catch (NoSuchMethodException e) {
            } catch (Exception e) {
                LOGGER.error("属性名{}没有找到对应的定义类型，原因{}！", name, e.getMessage());
                throw e;
            }
            return (byte)0;
        }
    }

    interface AttributeType {
        enum Hero implements AttributeType {
            BASIC, // 英雄基础属性
            EQUIP,// 英雄装备属性
            SPIRIT,// 英雄灵兽属性
            //            VEIN,// 英雄经脉属性
            REIN,// 英雄转生属性
            WING,// 英雄光翼属性
            STRENGTHEN, // 强化
            SKILL, //技能
            BUFF, //buff
            GEM, //宝石
            CAMP_EQUIP, //传世装备
            REALM, //境界
            WING_EQUIP, //光翼装备
            PERSIST_BUFF, //持续性BUFF(如属性药水)
            STAR,//英雄装备星级
            DIANJIN,//点金
            XINFA,//心法
            OFFICIAL,//官职
            UNION_XIULIAN,//行会修炼
            GM,//GM属性临时强化
            HUMANSHOW,//装扮
            OFFICIAL_EQUIP,//仙官装备
            MIJI,//秘笈
        }

        enum Role implements AttributeType {
            WUXING, // 五行
            RUNES, // 符文
            VITALITY, //噬魂
            UNION_TECH,//行会科技
            ARTIFACT, //神器
            SHIELD, //护盾
            BUFF //buff
        }

        enum Monster implements AttributeType {
            Basic, // 怪物基础属性
            Buffer,
            PropertiesAttribute//浮动属性
        }
    }
}
