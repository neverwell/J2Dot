package com.nemo.game.map.aoi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by h on 2018/8/13.
 */
public class Tower {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tower.class);


    private int x;
    private int y;


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
}
