package com.nemo.concurrent;

import com.nemo.concurrent.queue.ICommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//自定义线程池
public class QueueExecutor extends ThreadPoolExecutor{
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueExecutor.class);

    private String name;
    private int corePoolSize;
    private int maxPoolSize;

    public QueueExecutor(final String name, int corePoolSize, int maxPoolSize) {
        super(corePoolSize, maxPoolSize, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                int curCount = this.count.incrementAndGet();
                LOGGER.info("创建线程:" + name + "-" + curCount);
                return new Thread(r, name + "-" + curCount);
            }
        });
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    protected void afterExecute(Runnable task, Throwable throwable) {
        super.afterExecute(task, throwable);
        IQueueDriverCommand work = (IQueueDriverCommand)task;
        ICommandQueue<IQueueDriverCommand> queue = work.getCommandQueue();
        synchronized (queue) {
            IQueueDriverCommand nextCommand = queue.poll();
            if(nextCommand == null) { //如果暂时没有下一个任务了 停止
                queue.setRunning(false);
            } else {
                this.execute(nextCommand);
            }
        }
    }
}
