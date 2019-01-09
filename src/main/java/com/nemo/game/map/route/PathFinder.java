package com.nemo.game.map.route;

import com.nemo.game.map.scene.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by h on 2018/8/13.
 */
//路径寻找器
public class PathFinder {
    private OpenPointBinaryHeap openList;
    private Set<Point> clostSet;
    private Set<Point> openSet;

    public PathFinder(int width, int height) {
        openList = new OpenPointBinaryHeap(width*height);
        clostSet = new HashSet<>();
        openSet = new HashSet<>();
    }

}
