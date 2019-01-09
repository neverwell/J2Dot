package com.nemo.game.map.play.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by h on 2018/8/12.
 */
public interface PlayConst {

    enum MapType {
        NORMAL(0),
        OUT_BOSS(1),
        SECRET_BOSS(2),
        RECYCLE_BOSS(3),
        SHENYU_BOSS(4),
        SHENLONGDIANTANG_BOSS(5);

        int type;

        MapType(int type) {
            this.type = type;
        }

        private static Map<Integer, MapType> map = new HashMap<>();

        static {
            for(MapType v : values()) {
                map.put(v.type, v);
            }
        }

        public int type(){
            return this.type;
        }

        public static boolean contains(int type) {
            return map.containsKey(type);
        }

        public static MapType parse(int type) {
            return map.get(type);
        }
    }

}
