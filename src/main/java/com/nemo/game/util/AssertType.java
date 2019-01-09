package com.nemo.game.util;

public enum AssertType {
    CLIENT("返回给客户端提示"),
    SERVER("服务器log输出"),;

    private String memo;

    AssertType(String memo) {
        this.memo = memo;
    }
}
