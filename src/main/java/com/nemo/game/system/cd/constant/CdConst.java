package com.nemo.game.system.cd.constant;

public interface CdConst {
    interface CdType {
        //回血CD
        int DOWN_RECOVER_HP = 1;
        //伤害时候回血
        int HURT_RECOVER_HP = 2;
        //特殊属性
        int SPEC_ATT = 3;
        //技能
        int SKILL = 4;
        //公共技能
        int SKILL_COMMON = 5;
        //buff
        int BUFFER = 6;
    }

    interface CdKey {
        int COMMON_KEY = 1;
    }
}
