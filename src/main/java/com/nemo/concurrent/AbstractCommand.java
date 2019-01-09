package com.nemo.concurrent;

import com.nemo.concurrent.queue.ICommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//本质就是一个Runnable任务 附加了一些属性
public abstract class AbstractCommand implements IQueueDriverCommand{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);
    private ICommandQueue<IQueueDriverCommand> commandQueue; //队列
    protected int queueId;

    public AbstractCommand(){
    }

    @Override
    public void run() {
        try {
            long time = System.currentTimeMillis();
            this.doAction(); //执行ICommand里的doAction方法
            long exeTime = System.currentTimeMillis() - time;
            if(exeTime > 2L) {

            }
        } catch (Throwable var5) {
            LOGGER.error("命令执行错误", var5);
        }
    }

    @Override
    public ICommandQueue<IQueueDriverCommand> getCommandQueue() {
        return commandQueue;
    }

    @Override
    public void setCommandQueue(ICommandQueue<IQueueDriverCommand> commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Override
    public int getQueueId() {
        return queueId;
    }

    @Override
    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    @Override
    public Object getParam() {
        return null;
    }

    @Override
    public void setParam(Object var1) {

    }
}
