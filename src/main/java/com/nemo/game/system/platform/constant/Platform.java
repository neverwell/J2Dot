package com.nemo.game.system.platform.constant;

import lombok.extern.slf4j.Slf4j;

public interface Platform {

    @Slf4j
    enum PlatformId {
        DOUYU(1, "斗鱼"),
        TENCENT(2, "腾讯"),
        ;
        private int platformId;

        private String platformName;

        PlatformId(int platformId, String platformName) {
            this.platformId = platformId;
            this.platformName = platformName;
        }

        //转换
        public static PlatformId parse(int platformId) {
            PlatformId[] values = PlatformId.values();
            for(PlatformId value : values) {
                if(value.getPlatformId() == platformId) {
                    return value;
                }
            }
            return null;
        }

        //是否包含某个平台
        public static boolean hasPlatform(int platformId) {
            PlatformId[] values = PlatformId.values();
            for(PlatformId value : values) {
                if(value.getPlatformId() == platformId) {
                    return true;
                }
            }
            return false;
        }

        public int getPlatformId() {
            return platformId;
        }

        public String getPlatformName() {
            return platformName;
        }
    }
}
