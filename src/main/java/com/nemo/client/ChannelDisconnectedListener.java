package com.nemo.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ChannelDisconnectedListener implements ChannelFutureListener{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelFutureListener.class);
    private Client client;
    private int index;

    public ChannelDisconnectedListener(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(this.client.isStopped()) {
            LOGGER.info("连接断开，客户端已关闭不进行重连，index->{},host->{},port->{}", this.client.getBuilder().getHost(), this.client.getBuilder().getPort());
        } else if(!this.client.needReconnect) {
            LOGGER.info("连接断开，重连功能关闭不重连，index->{},host->{},port->{}", this.client.getBuilder().getHost(), this.client.getBuilder().getPort());
        } else {
            int reconnectDelay = this.client.getReconnectDelay(this.index);
            this.client.countReconnectDelay(this.index);
            LOGGER.info("连接断开，{}秒后开始重连，index->{},host->{},port->{}", reconnectDelay, this.client.getBuilder().getHost(), this.client.getBuilder().getPort());
            Client.executor.schedule(() -> {
                client.createChannel(index);
            }, (long)reconnectDelay, TimeUnit.SECONDS);
        }
    }
}
