package com.nemo.game;

import com.nemo.game.back.BackServer;
import com.nemo.game.server.ServerOption;
import com.nemo.game.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by h on 2018/8/5.
 */
public class GameContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameContext.class);

    private static int serverId;
    //1游戏服 2跨服 用于创建地图和地图的业务逻辑区分
    private static int serverType;

    private static boolean ready;
    //开服日期
    private static LocalDateTime openTime;
    //开服当天0点时间戳
    private static long openDayZeroTime;
    //合服日期
    private static LocalDateTime combineTime;
    //合服当天0点时间戳
    private static long combineDayZeroTime;
    //是否已经合服
    private static boolean combined = false;
    //是否开启全服双倍经验
    private static int expDouble = 1;

    private static ServerOption option;

    private static GameServer gameServer;

    private static BackServer backServer;

    private static boolean isDebug;
    //服务器关闭逻辑是否已经执行
    private static boolean serverCloseLogicExecuted;
    //游戏服务器关闭
    private static boolean closed;
    //是否防沉迷
    private static boolean fcm = false;

    public static void init(ServerOption option) {
        GameContext.option = option;
        serverId = option.getServerId();
        serverType = option.getServerType();
        openTime = option.getOpenTime();
        isDebug = option.isDebug();

        //开服日期时间戳
        long openTimeMills = openTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
        openDayZeroTime = TimeUtil.dayZeroMillsFromTime(openTimeMills);

        LOGGER.info("开服时间：{}", openTime);
        LOGGER.info("开服当天0点时间戳：{}", openDayZeroTime);
        LOGGER.info("开服距离开服当天0点时间：{}", (openTimeMills - openDayZeroTime));

        combineTime = option.getCombineTime();
        if(combineTime != null) {
            long combineTimeMills = combineTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
            combineDayZeroTime = TimeUtil.dayZeroMillsFromTime(combineTimeMills);
            if(combineTimeMills <= openTimeMills) {
                throw new RuntimeException("开服与合服时间配置错误");
            }
            combined = true;

            LOGGER.info("合服时间：{}", combineTime);
            LOGGER.info("合服当天0点时间戳：{}", combineDayZeroTime);
            LOGGER.info("合服距离合服当天0点：{}", (combineTimeMills - combineDayZeroTime));
        }
    }

    public static GameServer createGameServer() {
        try {
            gameServer = new GameServer(option);
            //初始化世界地图

            ready = true;
            return gameServer;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static BackServer createBackServer() {
        try {
            backServer = new BackServer(option);
            ready = true;
            return backServer;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isStarted() {
        return gameServer != null && gameServer.isOpen();
    }

    public static int getExpDouble() {
        return expDouble;
    }

    public static void setExpDouble(int expDouble) {
        GameContext.expDouble = expDouble;
    }

    public static LocalDateTime getOpenTime() {
        return openTime;
    }

    public static void setOpenTime(LocalDateTime openTime) {
        GameContext.openTime = openTime;
    }

    public static long getOpenDayZeroTime() {
        return openDayZeroTime;
    }

    public static void setOpenDayZeroTime(long openDayZeroTime) {
        GameContext.openDayZeroTime = openDayZeroTime;
    }

    public static LocalDateTime getCombineTime() {
        return combineTime;
    }

    public static void setCombineTime(LocalDateTime combineTime) {
        GameContext.combineTime = combineTime;
    }

    public static long getCombineDayZeroTime() {
        return combineDayZeroTime;
    }

    public static void setCombineDayZeroTime(long combineDayZeroTime) {
        GameContext.combineDayZeroTime = combineDayZeroTime;
    }

    public static boolean isCombined() {
        return combined;
    }

    public static void setCombined(boolean combined) {
        GameContext.combined = combined;
    }

    public static ServerOption getOption() {
        return option;
    }

    public static GameServer getGameServer() {
        return gameServer;
    }

    public static BackServer getBackServer() {
        return backServer;
    }

    public static int getServerId() {
        return serverId;
    }

    public static int getServerType() {
        return serverType;
    }

    public static boolean isServerCloseLogicExecuted() {
        return serverCloseLogicExecuted;
    }

    public static void setServerCloseLogicExecuted(boolean serverCloseLogicExecuted) {
        GameContext.serverCloseLogicExecuted = serverCloseLogicExecuted;
    }

    public static boolean isClosed() {
        return closed;
    }

    public static void setClosed(boolean closed) {
        GameContext.closed = closed;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static boolean isFcm() {
        return fcm;
    }

    public static void setFcm(boolean fcm) {
        GameContext.fcm = fcm;
    }

    public static boolean isReady() {
        return ready;
    }

    public static void setReady(boolean ready) {
        GameContext.ready = ready;
    }
}
