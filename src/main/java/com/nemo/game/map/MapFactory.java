package com.nemo.game.map;

import com.nemo.commons.util.CSVData;
import com.nemo.commons.util.CSVUtil;
import com.nemo.commons.util.Cast;
import com.nemo.game.map.play.constant.PlayConst;
import com.nemo.game.GameContext;
import com.nemo.game.config.model.MapConfig;
import com.nemo.game.map.play.constant.PlayConst.MapType;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.map.scene.Topography;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MapFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapFactory.class);

    public static GameMap createMap(MapConfig mapConfig, int line){
        //配置地形的文件路径
        String binaryFile = GameContext.getOption().getConfigDataPath() + "map/" + mapConfig.getId() + "/mapdata.list";

        GameMap map = null;
        try {
            Topography topography = new Topography(binaryFile, mapConfig.getWidth(), mapConfig.getHeight());
            MapType mapType = PlayConst.MapType.parse(mapConfig.getMapType());
            switch (mapType) {
                case SHENYU_BOSS:
                case SHENLONGDIANTANG_BOSS:
//                    map = new ShengyuBossMap(topography);
                    break;
                case SECRET_BOSS:
                case OUT_BOSS:
//                    map = new WorldBossMap(topography);
                    break;
                default:
                    map = new GameMap(topography);
                    break;
            }
            //赋予地图属性值
            map.setId(mapConfig.getId());
            map.setCfgId(mapConfig.getId());
            map.setTopography(topography);
            map.setLine(line);
            map.setName(mapConfig.getName());
            map.setCanCross(mapConfig.getCancross() == 1);
            map.init();

            //创建怪物
            initMonster(map, GameContext.getOption().getConfigDataPath() + "map/" + map.getCfgId() + "/monsters.csv");

        } catch (Exception ex) {
            LOGGER.error("地图{}：{}创建失败", mapConfig.getId(), mapConfig.getName());
        }
        return map;
    }

    public static void initMonster(GameMap map, String fileurl) {
        CSVData data = CSVUtil.read(fileurl, 0);
        for(Map<String, String> mobj : data.tableRows) {
            int mid = Cast.toInteger(mobj.get("mid"));
            int x = Cast.toInteger(mobj.get("born-x"));
            int y = Cast.toInteger(mobj.get("born-y"));







        }






    }






}
