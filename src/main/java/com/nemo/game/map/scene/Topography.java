package com.nemo.game.map.scene;

import com.nemo.game.map.constant.MapConst.Dir;
import com.nemo.game.util.GeomUtil;
import com.nemo.game.util.MapUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by h on 2018/8/5.
 */
public class Topography {
    //格子宽度
    public static final int GRID_WIDTH = 68;
    //格子高度
    public static final int GRID_HEIGHT = 35;
    //地图总长度
    private int pixelWidth;
    //地图总高度
    private int pixelHeight;
    //格子横向数量
    private int width;
    //格子纵向数量
    private int height;
    //所有的点（二维数组）
    private Point[][] pointArray;
    //所有的点
    private Map<Integer, Point> pointMap = new HashMap<>();
    //出生点
    private List<Point> bornPointList = new ArrayList<>();
    //摆摊点
    private Map<Integer, Point> stallagePoint = new HashMap<>();
    //可行走点
    private List<Point> walkList = new ArrayList<>();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Topography(String binaryFile, int pixelWidth, int pixelHeight){
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        byte[] bytes = MapUtil.readMapBytes(binaryFile);
        if (bytes == null) {
            throw new RuntimeException("地图地形信息初始化失败,请检查地形文件[" + binaryFile + "]");
        }
        
        initGrid(); //初始化各个格子（点）
        readGridAttribute(bytes); //为各个格子赋属性
        createEightTree(); //创建每个格子的八叉树
    }

    private void initGrid(){
        this.width = pixelWidth / GRID_WIDTH;
        this.height = pixelHeight / GRID_HEIGHT;
        this.width = pixelWidth % GRID_WIDTH == 0 ? this.width : this.width + 1;
        this.height = pixelHeight % GRID_HEIGHT == 0 ? this.height : this.height + 1;
        this.pointArray = new Point[this.width][this.height];

        for(int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                Point point = new Point(x, y);
                this.pointArray[x][y] = point;
                pointMap.put(point.getId(), point);
            }
        }
    }

    private void readGridAttribute(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        //4个bit代表一个格子[阻挡、遮挡(客户端)、安全区、矿点]
        byte b = buffer.get();
        int bitIndex = 8;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int block;
                int safe;
                int digAble;
                if(bitIndex == 8) {
                    block = b >> 7 & 0b00000001;
                    safe = b >> 5 & 0b00000001;
                    digAble = b >> 4 & 0b00000001;
                    bitIndex = 4;
                } else {
                    block = b >> 3 & 0b00000001;
                    safe = b >> 1 & 0b00000001;
                    digAble = b & 0b00000001;
                    if(!(x == width -1 && y == height - 1)) { //赋值完最后一个格子
                        b = buffer.get();
                    }
                    bitIndex = 8;
                }
                Point grid = this.pointArray[x][y];
                grid.setBlock(block == 1);
                grid.setSafe(safe == 1);
                grid.setDigAble(digAble == 1);
            }
        }

        //读取出生点 就是人物进入地图时默认出现的位置
        int bornLength = buffer.getShort();
        for(int i = 0; i < bornLength; i++) {
            int x = buffer.getShort();
            int y = buffer.getShort();
            Point grid = this.pointArray[x][y];
            bornPointList.add(grid);
        }

        //摆摊点
        int stallageLength = buffer.getShort();
        for(int i = 0; i < stallageLength; i++) {
            int x = buffer.getShort();
            int y = buffer.getShort();
            Point grid = this.pointArray[x][y];
            stallagePoint.put(grid.getId(), grid);
        }

        //所有可行走的点
        for(Point[] outer : pointArray) {
            for(Point p : outer) {
                if(!p.isBlock()) {
                    walkList.add(p);
                }
            }
        }
    }

    private void createEightTree(){
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Point point = pointArray[x][y];
                Point[] nears = new Point[8];
                //遍历该格子周围八个格子，进行能否走动的判断
                for(int index = 0; index < 16; index += 2) {
                    int tx = x + GeomUtil.EIGHT_DIR_OFFSET[index];
                    int ty = y + GeomUtil.EIGHT_DIR_OFFSET[index+1];
                    if (tx < 0 || ty < 0 || tx > width || ty > height) {
                        continue;
                    }
                    Point tp = pointArray[tx][ty];
                    Dir dir = GeomUtil.getDir(x, y, tx, ty);
                    nears[dir.getIndex()] = tp;
                }
                point.setNears(nears);
            }
        }
    }




}
