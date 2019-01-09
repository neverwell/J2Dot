package com.nemo.game.util;

public enum UpdateAction {

    ANYWAY(1, ""),
    PLAYER_MIDNIGHT(133, "玩家零点事件"),
    ;

    final private int code;

    final private String comment;

    UpdateAction(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public int getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
