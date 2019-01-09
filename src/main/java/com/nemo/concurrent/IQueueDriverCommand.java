package com.nemo.concurrent;

import com.nemo.concurrent.queue.ICommandQueue;

public interface IQueueDriverCommand extends ICommand{
    int getQueueId();

    void setQueueId(int var1);

    ICommandQueue<IQueueDriverCommand> getCommandQueue();

    void setCommandQueue(ICommandQueue<IQueueDriverCommand> var1);

    Object getParam();

    void setParam(Object var1); //一般设置上session
}
