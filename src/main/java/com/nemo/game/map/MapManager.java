package com.nemo.game.map;

import com.nemo.game.map.obj.IMapObject;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//地图管理类
public class MapManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);

    private static final MapManager INSTANCE = new MapManager();

    public static MapManager getInstance() {
        return INSTANCE;
    }

    private Map<Long, GameMap> maps = new ConcurrentHashMap<>();

    public Map<Long, GameMap> getMaps() {
        return maps;
    }

    //初始化地图
    public void initMaps() {

    }

    public GameMap removeMap(int mapId, int line) {
        return maps.remove(MapUtil.getMapKey(mapId, line));
    }

    public GameMap getMap(int mapId, int line) {
        return maps.get(MapUtil.getMapKey(mapId, line));
    }

    public GameMap getMap(IMapObject obj) {
        return getMap(obj.getMapId(), obj.getLine());
    }
}
