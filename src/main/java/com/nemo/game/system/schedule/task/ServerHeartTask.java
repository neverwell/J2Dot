package com.nemo.game.system.schedule.task;

import com.nemo.game.event.EventType;
import com.nemo.game.event.EventUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class ServerHeartTask implements Runnable{

    private static final int MID_NIGHT_EVENT_DELAY = 5;
    private static Logger LOGGER = LoggerFactory.getLogger(ServerHeartTask.class);

    private int lastMinute;
    private int lastDay;

    public ServerHeartTask() {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        this.lastMinute = minute;
        this.lastDay = day;
//        LOGGER.error("lastMinute：" + this.lastMinute + " lastDay：" + this.lastDay);
    }

    @Override
    public void run() {
        try {
            Calendar calendar = Calendar.getInstance();

            int day = calendar.get(Calendar.DAY_OF_YEAR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            //每分钟一次
            if(minute != lastMinute) {
                lastMinute = minute;
                EventUtil.fireEvent(EventType.SERVER_MINUTE_HEART);
            }
            //每天一次（零点过后至少5秒才执行）
            if(day != lastDay && second > MID_NIGHT_EVENT_DELAY) {
                lastDay = day;
                EventUtil.fireEvent(EventType.SERVER_MIDNIGHT);
            }
            //每秒一次
            EventUtil.fireEvent(EventType.SERVER_SECOND_HEART);
        } catch (Throwable e) {
            LOGGER.error("服务器心跳事件发生错误", e);
        }
    }
}
