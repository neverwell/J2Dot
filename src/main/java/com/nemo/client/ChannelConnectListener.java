package com.nemo.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ChannelConnectListener implements ChannelFutureListener{
    private Client client;
    private int index;
    public static final Logger LOGGER = LoggerFactory.getLogger(ChannelFutureListener.class);

    public ChannelConnectListener(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        ClientBuilder builder = this.client.getBuilder();
        if(future.isSuccess()) {
            this.client.registerChannel(this.index, future.channel());
            if(this.client.getBuilder().getClientListener() != null) {
                //连接完成回调
                this.client.getBuilder().getClientListener().afterConnected(future.channel(), this.index);
            }

            LOGGER.info("成功连接到服务器,index->{}, host->{}, port->{}", this.index, builder.getHost(), builder.getPort());
        } else {
            LOGGER.error("连接服务器失败,index->{}, host->{}, port->{}", this.index, builder.getHost(), builder.getPort());
            if(this.client.isStopped()) {
                LOGGER.error("客户端已经关闭不重连,index->{}, host->{}, port->{}", this.index, builder.getHost(), builder.getPort());
            } else if(!this.client.needReconnect) {
                LOGGER.error("重连功能关闭不重连,index->{}, host->{}, port->{}", this.index, builder.getHost(), builder.getPort());
            } else {
                int reconnectDelay = this.client.getReconnectDelay(this.index);
                this.client.countReconnectDelay(this.index);
                LOGGER.error("{}秒后进行重连,index->{}, host->{}, port->{}", reconnectDelay, this.index, builder.getHost(), builder.getPort());
                Client.executor.schedule(() -> {
                    client.createChannel(index);
                }, (long)reconnectDelay, TimeUnit.SECONDS);
            }
        }
    }
}
