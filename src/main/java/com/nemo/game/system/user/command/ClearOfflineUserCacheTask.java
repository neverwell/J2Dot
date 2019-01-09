package com.nemo.game.system.user.command;

import com.nemo.concurrent.AbstractCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ClearOfflineUserCacheTask extends AbstractCommand{

    private static long deleteTime = 30 * 60 * 1000;

    @Override
    public void doAction() {
        long currentTime = System.currentTimeMillis();
        try {


        } catch (Exception e) {
            log.error("清理离线玩家数据失败-外", e);
        }

    }
}
