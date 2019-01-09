package com.nemo.game.map.obj;

import com.nemo.game.util.enumeration.EnumUtils;
import com.nemo.game.util.enumeration.IndexEnum;

public enum FixHurtType implements IndexEnum {
    FIX_0(0, "正常伤害及显示"),
    FIX_1(1, "伤害及显示均为1"),
    FIX_2(2, "伤害为1显示正常伤害"),;

    private int type;

    private String memo;

    FixHurtType(int type, String memo) {
        this.type = type;
        this.memo = memo;
    }

    public static FixHurtType valueOf(int type) {
        return EnumUtils.getByIndex(type, FixHurtType.values());
    }

    @Override
    public int getIndex() {
        return type;
    }
}
