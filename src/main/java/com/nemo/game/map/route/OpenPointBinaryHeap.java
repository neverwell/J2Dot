package com.nemo.game.map.route;

import com.nemo.game.map.scene.Point;

/**
 * Created by h on 2018/8/13.
 */
public class OpenPointBinaryHeap {
    private int capacity;
    private  Point[] array;

    public OpenPointBinaryHeap(int capacity) {
        this.capacity = capacity;
        array = new Point[this.capacity];
    }

}
