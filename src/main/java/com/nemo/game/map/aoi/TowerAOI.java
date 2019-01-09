package com.nemo.game.map.aoi;

import com.nemo.game.map.obj.IMapObject;
import com.nemo.game.map.scene.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by h on 2018/8/13.
 */
public class TowerAOI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TowerAOI.class);

    public static final int WIDTH = 3;
    public static final int HEIGHT = 5;
    public static final int RANGE_LIMIT = 5;
    public static final int RANGE_DEFAULT = 2;

    //地图宽度
    private int width;
    //地图高度
    private int height;
    //灯塔宽度
    private int towerWidth;
    //灯塔高度
    private int towerHeight;
    //遍历最大返回限制
    private int rangeLimit;
    //x最大值
    private int maxX;
    //y最大值
    private int maxY;
    //灯塔数组
    private Tower[][] towers;
    //事件监听者
    private List<AOIEventListener> listeners = new ArrayList<>();

    public TowerAOI(int width, int height) {
        this.width = width;
        this.height = height;
        this.towerWidth = WIDTH;
        this.towerHeight = HEIGHT;
        this.rangeLimit = RANGE_LIMIT;
        init();
    }

    private void init(){
        this.maxX = this.width / this.towerWidth;
        if(this.width % this.towerWidth != 0) {
            this.maxX ++;
        }
        this.maxY = this.height / this.towerHeight;
        if(this.height % this.towerHeight != 0) {
            this.maxY ++;
        }
        towers = new Tower[maxX+1][maxY+1]; //横竖都多了一排
        for(int i = 0; i < maxX; i++) {
            for(int j = 0; j < maxY; j++) {
                towers[i][j] = new Tower();
                towers[i][j].setX(i);
                towers[i][j].setY(j);

            }
        }
    }

    //添加观察者
    public void addListener(AOIEventListener listener) {
        this.listeners.add(listener);
    }


    //获取指定类型的观察者
    public Map<Long, IMapObject> getWatchers(Point pos) {

        return Collections.emptyMap();
    }

}
