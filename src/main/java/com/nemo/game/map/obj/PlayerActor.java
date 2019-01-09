package com.nemo.game.map.obj;

import com.nemo.game.map.MapManager;
import com.nemo.game.map.fight.constant.FightConst;
import com.nemo.game.map.fsm.FSMMachine;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.system.shobak.ShobakManager;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PlayerActor extends Performer {

    //对应的服务器id
    private int hostId;
    //只在上线/切换地图的时候才会变化（实时坐标点取point的x）
    private int x;
    //只在上线/切换地图的时候才会变化（实时坐标点取point的y）
    private int y;
    //刚刚登录
    private boolean afterLogin;
    //是否已经下线
    private boolean offline;
    //队伍id
    private int teamId;
    //是否原地复活
    private int reliveType;
    //回城复活时间
    private int reliveTime;
    //战斗模型
    private int fightModel;

    //===============拷贝Role的数据，需要即时通知==================

    //英雄激活的序号
    private int number;
    //vip等级
    private int vipLevel;
    //roleId
    private long rid;
    //客户端显示的名字
    private String showName;
    //装备
    public Map<Integer, Integer> equipMap = new HashMap<>();
    //翅膀
    private int wing;
    //神斧Id
    private int godAxId;
    //威望
    private long weiWang;
    //帮会id
    private long unionId;
    //帮会名称
    private String unionName;
    //称号
    private int title;
    //军衔
    private int junxian;
    //神装套装id
    private int szSuit;
    //时装
    private Map<Integer, Integer> fashion = new HashMap<>();
    //阵容
    private int group;
    //传说武器等级
    private int legendWeaponLv;
    //传说神甲等级
    private int legendClothLv;


    /**======================历史地图信息（世界地图）用于回城和登录=================================*/
    //上个x
    private int lastX;
    //上个y
    private int lastY;
    //上个地图id
    private int lastMapId;
    //上个line
    private int lastLine;

    /**============================连击技能===========================================*/
    //连击当前技能顺序
    private int lianjiSkillIndex = -1;
    //下一个技能使用的剩余时间，超过该时间就不让用
    private long nextSkillRemainTime = 0;
    //上次回复内力时间
    private long lastRecoverInnerPowerTime = 0;

    private List<PlayerActor> slavePlayers = new ArrayList<>();

    private FSMMachine<PlayerActor> machine;

    @Override
    public FSMMachine<PlayerActor> getMachine() {
        return machine;
    }

    public void setMachine(FSMMachine<PlayerActor> machine) {
        this.machine = machine;
    }

    public PlayerActor() {
    }


    @Override
    public int getType() {
        return MapObjectType.Player;
    }

    public int getLastMapId() {
        return this.lastMapId;
    }

    public void setLastMapId(int lastMapId) {
        this.lastMapId = lastMapId;
    }

    public int getLastLine() {
        return this.lastLine;
    }

    public void setLastLine(int lastLine) {
        this.lastLine = lastLine;
    }

    public int getLastX() {
        return this.lastX;
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    public int getLastY() {
        return this.lastY;
    }

    public void setLastY(int lastY) {
        this.lastY = lastY;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIp() {
        return this.getFinalAttribute().getCurrentInnerPower();
    }

    public void updateInnerPower() {
//        if (this.isDead()) {
//            return;
//        }
//
//        long nowOfMills = TimeUtil.getNowOfMills();
//        if (nowOfMills - this.lastRecoverInnerPowerTime > RoleManager.INNER_POWER_FIX_RECOVER_TIME * 1000) {
//            int innerPower = this.getFinalAttribute().getCurrentInnerPower();
//            int innerPowerMax = this.getFinalAttribute().getInnerPowerMax();
//            if (innerPower < innerPowerMax) {
//                int addCount = innerPower + this.getFinalAttribute().getInnerRestore();
//                this.getFinalAttribute().setCurrentInnerPower(addCount > innerPowerMax ? innerPowerMax : addCount);
//                this.lastRecoverInnerPowerTime = nowOfMills;
//                // 通知内力变化
//                ResInnerChangeMessage message = new ResInnerChangeMessage();
//                message.setLid(this.getId());
//                message.setInner(this.getFinalAttribute().getCurrentInnerPower());
//                MessageUtil.sendRoundMessage(message, this);
//            }
//        }
    }

    @Override
    public boolean penetrate(IMapObject obj, boolean cross) {
        if (obj.getType() == MapObjectType.Monster) {
            return true;
        } else if (obj.getType() == MapObjectType.Player) {
            //同一个主角的英雄永远不能叠加
            PlayerActor actor = (PlayerActor) obj;
            if (this.isSlave() && actor.isSlave()) {
                if (this.getRid() == actor.getRid()) {
                    return false;
                }
            }
            if (cross) {
                return true;
            } else {
                if (!actor.isSlave()) {
                    return this.getRid() == actor.getRid();
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean overlying(IMapObject obj, boolean cross) {
        return penetrate(obj, cross);
    }


    /**
     * 是否是敌人
     *
     * @param obj obj
     * @return boolean
     */
    @Override
    public boolean isEnemy(IMapObject obj, boolean ignoreTargetOnly) {
        if (this == obj) {
            return false;
        }

        PlayerActor self = this;

        if (this.isSlave()) {
            // 奴隶用主人来判断
            self = (PlayerActor) this.master;
        }

        Performer target = (Performer) obj;
        if (target.isSlave()) {
            //奴隶用主人来判断
            target = target.getMaster();
        }


        if (target == self) {
            return false;
        }

        switch (target.getType()) {
            case MapObjectType.Player: {
                GameMap objMap = MapManager.getInstance().getMap(obj);
                if (objMap.isSafe()) {
                    return false;
                }

                if (obj.getPoint().isSafe()) {
                    return false;
                }

                PlayerActor targetPlayer = (PlayerActor) target;
                switch (this.fightModel) {
                    case FightConst.FightModel.UNION: {
                        if (self.getUnionId() > 0 && self.getUnionId() == targetPlayer.getUnionId()) {
                            return false;
                        } else {
                            return true;
                        }

                    }
                    case FightConst.FightModel.TARGET_ONLY: {
                        if (ignoreTargetOnly) {
                            //不判断善恶模式，这里涉及到AI寻找目标的问题，所以不能判断
                            return true;
                        } else {
                            long skillTargetId = this.getSkillTargetId();
                            int skillTargetType = this.getSkillTargetType();
                            if (skillTargetType == MapObjectType.Monster) {
                                return false;
                            } else if (skillTargetType == MapObjectType.Player) {

                                GameMap map = MapManager.getInstance().getMap(this);
                                PlayerActor skillTargetPlayerActor = (PlayerActor) map.getObject(skillTargetId);

                                if (skillTargetPlayerActor != null &&
                                        skillTargetPlayerActor.getRid() == targetPlayer.getRid()) {
                                    //整体是同一个玩家
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }


                    }
                    case FightConst.FightModel.ALL: {
                        if (self.getGroup() > 0) {
                            return targetPlayer.getGroup() != self.getGroup();
                        } else {
                            return true;
                        }
                    }
                    default:
                        return true;
                }
            }

            case MapObjectType.Monster: {
                MonsterActor monsterActor = (MonsterActor) target;
                switch (this.fightModel) {
                    case FightConst.FightModel.UNION:
                        if (monsterActor.getConfigId() == ShobakManager.STATUS_ID && self.getUnionId() > 0 && self.getUnionId() == ShobakManager.defenderUnionId) {
                            return false;
                        }
                        return true;
                    case FightConst.FightModel.TARGET_ONLY:
                        if (ignoreTargetOnly) {
                            //不判断善恶模式，这里涉及到AI寻找目标的问题，所以不能判断
                            return true;
                        } else {
                            //类型一样就可以攻击
                            return this.getSkillTargetType() == MapObjectType.Monster;
                        }

                    default:
                        //其他攻击模式都可以打怪
                        return true;
                }
            }
            default:
                //其他所有都不是敌人
                return false;
        }
    }

    @Override
    public boolean isFriend(IMapObject obj, boolean ignoreTargetOnly) {

        if (this == obj) {
            return true;
        }

        PlayerActor self = this;

        if (this.isSlave()) {
            // 奴隶用主人来判断
            self = (PlayerActor) this.master;
        }

        Performer target = (Performer) obj;
        if (target.isSlave()) {
            //奴隶用主人来判断
            target = target.getMaster();
        }


        if (target == self) {
            return true;
        }

        switch (target.getType()) {
            case MapObjectType.Player: {
                PlayerActor targetPlayer = (PlayerActor) target;
                switch (this.fightModel) {
                    case FightConst.FightModel.UNION: {
                        if (self.getUnionId() > 0 && self.getUnionId() == targetPlayer.getUnionId()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    case FightConst.FightModel.TARGET_ONLY:
                    case FightConst.FightModel.ALL: {
                        return false;
                    }
                }
            }
            default:
                //其他所有都不是朋友
                return false;
        }
    }


    public boolean isAfterLogin() {
        return afterLogin;
    }

    public void setAfterLogin(boolean afterLogin) {
        this.afterLogin = afterLogin;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }


    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        if (this.teamId != teamId) { // 队伍变化 场景消息
			/*ResTeamChangeMessage msg = new ResTeamChangeMessage();
			msg.setTeamId(teamId);
			msg.setLid(this.getId());
			MessageUtil.sendRoundMessage(msg, this);*/
        }
        this.teamId = teamId;
    }

    public int getVipLevel() {
        return this.vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }


    public int getReliveTime() {
        return reliveTime;
    }

    public void setReliveTime(int reliveTime) {
        this.reliveTime = reliveTime;
    }

    @Override
    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public List<PlayerActor> getSlavePlayers() {
        return slavePlayers;
    }

    public void setSlavePlayers(List<PlayerActor> slavePlayers) {
        this.slavePlayers = slavePlayers;
    }


    public Map<Integer, Integer> getEquipMap() {
        return equipMap;
    }

    public void setEquipMap(Map<Integer, Integer> equipMap) {
        this.equipMap = equipMap;
    }

    public int getWing() {
        return wing;
    }

    public void setWing(int wing) {
        this.wing = wing;
    }

    @Override
    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }
}
