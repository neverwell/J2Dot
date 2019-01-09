package com.nemo.game.util;

import com.nemo.concurrent.QueueExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorUtil {

    public static final ScheduledExecutorService COMMON_LOGIC_EXECUTOR = Executors.newScheduledThreadPool(4, new ThreadFactory() {
        AtomicInteger count = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            int curCount = count.incrementAndGet();
            return new Thread(r, "公共业务线程（线程池）-" + curCount);
        }
    });

    //定时执行场景任务
    public static final ScheduledExecutorService EVENT_DISPATCHER_EXECUTOR = Executors.newScheduledThreadPool(3, new ThreadFactory() {
        AtomicInteger count = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            int curCount = count.incrementAndGet();
            return new Thread(r, "场景定时事件派发线程-" + curCount);
        }
    });

    //游戏驱动主线程池
    public static final QueueExecutor COMMON_DRIVER_EXECUTOR = new QueueExecutor("场景公共驱动线程", 8, 8);

    //游戏登录线程池
    public static final QueueExecutor LOGIN_EXECUTOR = new QueueExecutor("登录线程", 1, 5000);

    //===============对Executor的方法包装==========================================
    //严格定时执行 上一个任务开始时间 + 延迟时间 = 下一个任务开始时间
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
        return COMMON_LOGIC_EXECUTOR.scheduleAtFixedRate(command, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return COMMON_LOGIC_EXECUTOR.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    //等任务执行完再算下一个任务执行时间 上一个任务结束时间 + 延迟时间 = 下一个任务开始时间
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return COMMON_LOGIC_EXECUTOR.scheduleWithFixedDelay(command, initialDelay, period, unit);
    }

    //延时执行一次
    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return COMMON_LOGIC_EXECUTOR.schedule(command, delay, unit);
    }

    public static Future<?> submit(Runnable task) {
        return COMMON_LOGIC_EXECUTOR.submit(task);
    }
}
