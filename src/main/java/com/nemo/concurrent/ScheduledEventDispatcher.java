package com.nemo.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//定时任务分发器 本身交给定时线程去执行
public class ScheduledEventDispatcher implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledEventDispatcher.class);
    private boolean running = false;
    //地图定时任务列表 交给地图的QueueDriver去执行
    private List<ScheduledEvent> events = new ArrayList<>();
    private int mapId;
    private int line;
    private QueueDriver driver;
    private Future<?> future;

    public ScheduledEventDispatcher(QueueDriver driver, int mapId, int line) {
        this.driver = driver;
        this.mapId = mapId;
        this.line = line;
    }

    //添加定时任务
    public void addTimerEvent(ScheduledEvent event) {
        if(this.running) {
            throw new RuntimeException("已经启动的派发器不允许改变派发事件");
        } else {
            this.events.add(event);
            LOGGER.debug("ScheduledEvent事件:addScheduledEvent=" + this.mapId + "=event=" + event.getClass().getName());
        }
    }

    public void removeTimerEvent(ScheduledEvent event) {
        if (this.running) {
            throw new RuntimeException("已经启动的派发器不允许改变派发事件");
        } else {
            this.events.remove(event);
            LOGGER.debug("ScheduledEvent事件:ScheduledEvent=remove");
        }
    }

    public void clearTimerEvent() {
        if (this.running) {
            throw new RuntimeException("已经启动的派发器不允许改变派发事件");
        } else {
            this.events.clear();
            LOGGER.debug("ScheduledEvent事件:ScheduledEvent=clear");
        }
    }

    public void start(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(this, 0L, 100L, TimeUnit.MILLISECONDS);
        this.running = true;
    }

    public void stop(boolean mayInterruptIfRunning) {
        this.future.cancel(mayInterruptIfRunning);
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public void run() {
        //遍历添加任务
        Iterator<ScheduledEvent> it = events.iterator();
        long curTime = System.currentTimeMillis();

        while (it.hasNext()) {
            ScheduledEvent event = it.next();
            if(event.getEnd() - curTime <= 0L) {
                if(event.getLoop() > 0) {
                    int loop = event.getLoop();
                    --loop;
                    if(loop == 0) {
                        it.remove();
                        LOGGER.info(this.mapId + "移除定时事件：" + event.getClass().getName());
                    } else {
                        event.setLoop(loop);
                        event.setEnd(curTime + event.getDelay());
                    }
                } else {
                    event.setEnd(curTime + event.getDelay()); //无限
                }
                this.driver.addCommand(event);
            }
        }
    }
}
