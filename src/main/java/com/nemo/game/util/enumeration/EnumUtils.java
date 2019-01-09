package com.nemo.game.util.enumeration;

public class EnumUtils {

    //通过index获取枚举
    public static <T extends IndexEnum> T getByIndex(int index, T[] values) {
        for(T e : values) {
            if(index == e.getIndex()) {
                return e;
            }
        }
        throw new IllegalArgumentException(String.format("类型 %s 中找不到序号为 %d 的元素", values[0].getClass().getName(), index));
    }

}
