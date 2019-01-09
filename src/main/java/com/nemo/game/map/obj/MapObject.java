package com.nemo.game.map.obj;

import com.nemo.game.map.aoi.TowerAOI;
import com.nemo.game.map.constant.MapConst.Dir;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.map.scene.Point;
import com.nemo.game.util.GeomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//抽象地图对象
public abstract class MapObject implements IMapObject{

    private static final Logger LOGGER = LoggerFactory.getLogger(MapObject.class);

    //唯一ID
    private long id;
    //是否可视
    private boolean visible = true;
    //配置ID
    private int configId;
    //地图ID
    private int mapId;
    //地图分线ID
    private int line;
    //所在坐标点
    protected Point point;
    //名字
    protected String name;
    //方向
    protected int dir;

    //地图视野单位
    private int viewRange = TowerAOI.RANGE_DEFAULT;

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getConfigId() {
        return configId;
    }

    @Override
    public void setConfigId(int configId) {
        this.configId = configId;
    }

    @Override
    public int getMapId() {
        return mapId;
    }

    @Override
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public Point getPoint() {
        return point;
    }

    @Override
    public void setPoint(Point point) {
        if(point != null && this.point != null) {
            Dir dir = GeomUtil.getDir(this.point, point);
            if(dir != Dir.NONE) {
                setDir(dir.getIndex());
            }
        }
        this.point = point;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public boolean penetrate(IMapObject obj, boolean cross) {
        return false;
    }

    @Override
    public boolean overlying(IMapObject obj, boolean cross) {
        return false;
    }

    @Override
    public boolean isEnemy(IMapObject obj, boolean ignoreTargetOnly) {
        return false;
    }

    @Override
    public boolean isFriend(IMapObject obj, boolean ignoreTargetOnly) {
        return false;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    @Override
    public long getRid() {
        return 0;
    }

    @Override
    public String getShowName() {
        return name;
    }

    @Override
    public int getViewRange() {
        return viewRange;
    }

    public void setViewRange(int viewRange) {
        this.viewRange = viewRange;
    }

    @Override
    public int getHostId() {
        return 0;
    }
}
