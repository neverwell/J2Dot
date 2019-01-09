package com.nemo.game.map.obj;

import com.nemo.game.map.fsm.FSMMachine;

public class PetActor extends MonsterActor{

    private int skillId;

    private int skillLevel;

    private int exp;

    private FSMMachine<PetActor> machine;

    public FSMMachine<? extends PetActor> getMachine() {
        return machine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setMachine(FSMMachine<? extends MonsterActor> machine) {
        this.machine =  (FSMMachine<PetActor>) machine;
    }

    @Override
    public int getType() {
        return MapObjectType.Pet;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean isEnemy(IMapObject obj, boolean ignoreTargetOnly) {
        if(this == obj) {
            return false;
        } else {
            //将自己的攻击目标复制给主人，然后主人那边逻辑才能通过
            master.setSkillTargetType(this.getSkillTargetType());
            master.setSkillTargetId(this.getSkillTargetId());
            return master.isEnemy(obj, ignoreTargetOnly);
        }
    }

    @Override
    public boolean isFriend(IMapObject obj, boolean ignoreTargetOnly) {
        if(this == obj){
            return true;
        } else {
            //将自己的攻击目标复制给主人，然后主人那边逻辑才能通过
            master.setSkillTargetType(this.getSkillTargetType());
            master.setSkillTargetId(this.getSkillTargetId());
            return master.isFriend(obj, ignoreTargetOnly);
        }
    }


    public Performer getMaster() {
        if(this.master.getType() == MapObjectType.Player && this.master.isSlave()) {
            PlayerActor slave = (PlayerActor) this.master;
            return slave.getMaster();
        }
        return master;
    }

    @Override
    public long getRid() {
        if(this.master.getType() == MapObjectType.Player ) {
            PlayerActor playerActor = (PlayerActor) this.master;
            return playerActor.getRid();
        }
        return 0;
    }
}
