package com.nemo.concurrent.queue;

public interface ICommandQueue<V> {
    //移除并返回队列头部的元素
    V poll();
    //将元素塞入队列尾部 成功返回true 队列已满返回false
    boolean offer(V var1);

    void setRunning(boolean var1);

    boolean isRunning();

    void setName(String var1);

    String getName();

    void clear();

    int size();
}
