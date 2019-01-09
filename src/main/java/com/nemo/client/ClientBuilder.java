package com.nemo.client;

import com.nemo.net.MessagePool;
import com.nemo.net.NetworkConsumer;
import com.nemo.net.NetworkEventlistener;
import io.netty.channel.ChannelHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientBuilder {
    private int nioEventLoopCount;
    private int port;
    private MessagePool msgPool;
    private NetworkConsumer consumer;
    private List<ChannelHandler> extraHandlers = new ArrayList<>();
    private String host;
    private boolean pooled;
    private int poolMaxCount;
    private int heartTime;
    private int maxIdleTime;
    private ClientHeart.PingMessageFactory pingMessageFactory;
    private ClientListener clientListener;
    private NetworkEventlistener eventlistener;
    private boolean needReconnect = true;

    public Client createClient() {
        if(this.pooled) {
            if(this.poolMaxCount <= 0) {
                this.poolMaxCount = 1;
            }
            return new PooledClient(this);
        } else {
            return new Client(this);
        }
    }



}
