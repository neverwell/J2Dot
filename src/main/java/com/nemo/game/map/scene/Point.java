package com.nemo.game.map.scene;

import com.nemo.game.util.GeomUtil;

/**
 * Created by h on 2018/8/7.
 */
public class Point {
    //坐标ID
    private int id;
    private int x;
    private int y;
    //是否阻挡
    private boolean block;
    //是否安全区
    private boolean safe;
    //是否可挖掘
    private boolean digAble;
    //周围能走的格子
    private Point[] nears;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = GeomUtil.getPointId(x, y);
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean isBlock() {
        return block;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setDigAble(boolean digAble) {
        this.digAble = digAble;
    }

    public boolean isDigAble() {
        return digAble;
    }

    public void setNears(Point[] nears) {
        this.nears = nears;
    }

    public Point[] getNears() {
        return nears;
    }
}
