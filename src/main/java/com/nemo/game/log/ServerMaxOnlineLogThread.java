package com.nemo.game.log;

import com.nemo.game.GameContext;
import com.nemo.game.log.entity.MaxOnlineLog;
import com.nemo.game.server.SessionManager;
import com.nemo.game.system.platform.constant.Platform;
import com.nemo.game.system.platform.constant.QuDao;
import com.nemo.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//最大在线人数日志
public class ServerMaxOnlineLogThread implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMaxOnlineLogThread.class);

    @Override
    public void run() {
        try {
            if(GameContext.getOption().getPlatformId() == Platform.PlatformId.TENCENT.getPlatformId()) {
                MaxOnlineLog log1 = new MaxOnlineLog();
                log1.setNum(SessionManager.getInstance().getSessionCountByQudao(QuDao.ONE.getQuDaoIndex()));
                log1.setPid(GameContext.getOption().getPlatformId());
                log1.setSid(GameContext.getServerId());
                log1.setQudao(QuDao.ONE.getQuDaoIndex());
                LogService.submit(log1);
                MaxOnlineLog log2 = new MaxOnlineLog();
                log2.setNum(SessionManager.getInstance().getSessionCountByQudao(QuDao.TWO.getQuDaoIndex()));
                log2.setPid(GameContext.getOption().getPlatformId());
                log2.setSid(GameContext.getServerId());
                log2.setQudao(QuDao.TWO.getQuDaoIndex());
                LogService.submit(log2);
            } else {
                MaxOnlineLog log = new MaxOnlineLog();
                log.setNum(SessionManager.getInstance().getSessionCount());
                log.setPid(GameContext.getOption().getPlatformId());
                log.setSid(GameContext.getServerId());
                LogService.submit(log);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
