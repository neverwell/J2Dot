package com.nemo.concurrent;

import com.nemo.concurrent.queue.ICommandQueue;
import com.nemo.concurrent.queue.UnlockedCommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//持有一个队列ICommandQueue 一个线程池QueueExecutor
public class QueueDriver {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueDriver.class);
    private int maxQueueSize;
    private String name;
    private long queueId;
    private ICommandQueue<IQueueDriverCommand> queue;
    private QueueExecutor executor;

    public QueueDriver(QueueExecutor executor, String name, long id, int maxQueueSize) {
        this.executor = executor;
        this.name = name;
        this.maxQueueSize = maxQueueSize;
        this.queueId = id;
        this.queue = new UnlockedCommandQueue<>();
        this.queue.setName(name);
    }

    //任务添加到队列中
    public boolean addCommand(IQueueDriverCommand command) {
        if(command.getQueueId() > 0 && (long)command.getQueueId() != this.queueId) {
            return false;
        } else {
            if(this.queue.size() > 200) {
            }

            synchronized (this.queue) {
                if(this.maxQueueSize > 0 && this.queue.size() > this.maxQueueSize) {
                    this.queue.clear();
                }

                boolean result = this.queue.offer(command);
                if(result) {
                    command.setCommandQueue(this.queue);
                    //一般队列中没有任务时会停止(在QueueExecutor的afterExecute中)
                    if(!this.queue.isRunning()) {
                        this.queue.setRunning(true);
                        this.executor.execute(this.queue.poll());
                    }
                } else {
                    LOGGER.error("队列添加任务失败");
                }
                return result;
            }
        }
    }

    public int getQueueSize() {
        return this.queue.size();
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
