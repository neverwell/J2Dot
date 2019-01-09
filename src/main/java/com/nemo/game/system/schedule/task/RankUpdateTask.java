package com.nemo.game.system.schedule.task;

import com.nemo.game.system.rank.RankManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RankUpdateTask implements Runnable{

    @Override
    public void run() {
        log.info("开始执行玩家排行榜更新事件");
        try {
            RankManager.getInstance().updateRankData();
        } catch (Exception e) {
            log.error("{}", e);
        }
    }
}
