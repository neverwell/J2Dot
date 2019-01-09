package com.nemo.concurrent;

import lombok.Data;

//定时任务
@Data
public abstract class ScheduledEvent extends AbstractCommand{
    private long end; //执行的时间戳 大于就执行
    private long remain;
    private int loop; //循环次数 初始设置<=0的话就是无限
    private long delay; //间隔的执行时间

    protected ScheduledEvent(long end) {
        this.end = end;
        this.loop = 1;
    }

    protected ScheduledEvent(int loop, long delay) {
        this.loop = loop;
        this.delay = delay;
        this.end = System.currentTimeMillis() + delay;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
