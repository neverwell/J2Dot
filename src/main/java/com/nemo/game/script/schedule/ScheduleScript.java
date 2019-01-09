package com.nemo.game.script.schedule;

import com.nemo.game.log.LogServerMidNightThread;
import com.nemo.game.log.ServerMaxOnlineLogThread;
import com.nemo.game.map.remote.AliveCheckCommand;
import com.nemo.game.map.remote.HostHeartCheckCommand;
import com.nemo.game.map.remote.PlayerSyncCommand;
import com.nemo.game.system.activity.DynamicActivityCfgReloadTask;
import com.nemo.game.system.schedule.script.IScheduleScript;
import com.nemo.game.system.schedule.task.RankUpdateTask;
import com.nemo.game.system.schedule.task.RoleHeartDispatchTask;
import com.nemo.game.system.schedule.task.ServerHeartTask;
import com.nemo.game.system.user.command.ClearOfflineUserCacheTask;
import com.nemo.game.util.ExecutorUtil;
import com.nemo.game.util.TimeUtil;
import com.nemo.script.annotation.Script;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Slf4j
@Script
public class ScheduleScript implements IScheduleScript{

    @Override
    public void start() {
        Calendar c = Calendar.getInstance();
        int second = c.get(Calendar.SECOND);
        int millisecond = c.get(Calendar.MILLISECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        long dayDelay = TimeUtil.ONE_DAY_IN_MILLISECONDS - (hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000 + millisecond);
        log.info("距离零点剩余：【{}】MS", dayDelay);
        long hourDelay = TimeUtil.ONE_HOUR_IN_MILLISECONDS - (minute * 60 * 1000 + second * 1000 + millisecond);
        log.info("距离整点剩余：【{}】MS", hourDelay);
        long minuteDelay = TimeUtil.ONE_MINUTE_IN_MILLISECONDS - (second * 1000 + millisecond);
        log.info("距离整分剩余：【{}】MS", minuteDelay);
        long secondDelay = 1000 - millisecond;
        log.info("距离整秒剩余：【{}】MS", secondDelay);

        //服务器心跳/秒（包含了零点事件 秒 分钟事件）
        ExecutorUtil.scheduleAtFixedRate(new ServerHeartTask(), secondDelay, 1000);
        //排行榜更新/10分钟一次
        ExecutorUtil.scheduleAtFixedRate(new RankUpdateTask(), minuteDelay, 10 * 60 * 1000);
        //最高在线人数日志/5分钟一次
        ExecutorUtil.scheduleAtFixedRate(new ServerMaxOnlineLogThread(), minuteDelay, 5 * 60 * 1000);
        //动态活动表刷新/5分钟一次
        ExecutorUtil.scheduleAtFixedRate(new DynamicActivityCfgReloadTask(), 5000, 5 * 60 * 1000);
        //玩家心跳派发事件 （执行完了延迟1秒再执行）
        ExecutorUtil.EVENT_DISPATCHER_EXECUTOR.scheduleWithFixedDelay(new RoleHeartDispatchTask(), 500, 1000, TimeUnit.MILLISECONDS);
        //跨服主机心跳/2秒一次
        ExecutorUtil.scheduleAtFixedRate(new HostHeartCheckCommand(), 1000, 2 * 1000, TimeUnit.MILLISECONDS);
        //跨服主机活性检查/30秒一次
        ExecutorUtil.scheduleAtFixedRate(new AliveCheckCommand(), 5000, 30 * 1000, TimeUnit.MILLISECONDS);
        //同步当前玩家列表到跨服服务器/30秒一次
        ExecutorUtil.scheduleAtFixedRate(new PlayerSyncCommand(), 10000, 30 * 1000, TimeUnit.MILLISECONDS);
        //全服零点日志记录（日志记录不要用零点事件）
        ExecutorUtil.scheduleAtFixedRate(new LogServerMidNightThread(), dayDelay, TimeUtil.ONE_DAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        //清理玩家数据/10分钟一次
        ExecutorUtil.scheduleAtFixedRate(new ClearOfflineUserCacheTask(), 60 * 1000, 10 * 60 * 1000, TimeUnit.MILLISECONDS);
    }
}
