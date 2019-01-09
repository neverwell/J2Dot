package com.nemo.game.util;

import com.nemo.game.map.constant.MapConst.Dir;
import com.nemo.game.map.scene.Point;

/**
 * Created by h on 2018/8/7.
 */

public class GeomUtil {

    public static int getPointId(int x, int y) {
        return (x << 16) | y;
    }

    //一个格子周围八方向的偏移量
    public static final int[] EIGHT_DIR_OFFSET = new int[]{-1, -1, 0, -1, 1, -1, 1, 0, 1, 1, 0, 1, -1, 1, -1, 0 };

    public static Dir getDir(Point fromPoint, Point toPoint) {
        return getDir(fromPoint.getX(), fromPoint.getY(), toPoint.getX(), toPoint.getY());
    }

    //返回两个坐标之间的方向量 t相对于f
    public static Dir getDir(int fx, int fy, int tx, int ty) {
        int colDiff = tx - fx;
        int rowDiff = ty - fy;
        if (colDiff > 0) {
            if (rowDiff > 0) { //第四象限
                if (rowDiff == colDiff) {
                    return Dir.RIGHT_BOTTOM;
                } else if (rowDiff > colDiff) {
                    return Dir.BOTTOM;
                } else {
                    return Dir.RIGHT;
                }
            } else if (rowDiff < 0) {
                if (rowDiff + colDiff == 0) {
                    return Dir.RIGHT_TOP;
                } else if (rowDiff + colDiff > 0) {
                    return Dir.RIGHT;
                } else {
                    return Dir.TOP;
                }
            } else {
                return Dir.RIGHT;
            }
        } else if (colDiff == 0) {
            if (ty > fy) {
                return Dir.BOTTOM;
            } else if (ty == fy) {
                // 两个点在同一个位置上
                return Dir.NONE;
            } else {
                return Dir.TOP;
            }
        } else {
            if (rowDiff > 0) {
                if (rowDiff + colDiff == 0) {
                    return Dir.LEFT_BOTTOM;
                } else if (rowDiff + colDiff > 0) {
                    return Dir.BOTTOM;
                } else {
                    return Dir.LEFT;
                }
            } else if (rowDiff < 0) {
                if (rowDiff == colDiff) {
                    return Dir.LEFT_TOP;
                } else if (rowDiff > colDiff) {
                    return Dir.LEFT;
                } else {
                    return Dir.TOP;
                }
            } else {
                return Dir.LEFT;
            }
        }
    }


}
