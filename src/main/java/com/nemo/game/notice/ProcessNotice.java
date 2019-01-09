package com.nemo.game.notice;

import com.nemo.concurrent.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//队列通知消息， 主要是用来在一个进程的不同线程中进行数据的通信
public abstract class ProcessNotice extends AbstractCommand{

    protected static final Logger LOGGER = LoggerFactory.getLogger(ProcessNotice.class);

    protected transient List<Integer> hosts = new ArrayList<>();

    public abstract int id();

    public abstract byte[] encode();

    public abstract void decode(byte[] bytes);

    public void addHost(int hostId) {
        hosts.add(hostId);
    }

    public List<Integer> getHosts() {
        return hosts;
    }

    public void setHosts(List<Integer> hosts) {
        this.hosts = hosts;
    }

    public void send(byte processId, long queueId) {
        NoticeUtil.sendNotice(processId, this, queueId);
    }
}
