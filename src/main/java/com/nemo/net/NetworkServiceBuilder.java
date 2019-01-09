package com.nemo.net;

import io.netty.channel.ChannelHandler;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//收集创建NetworkService所需的参数
@Data
public class NetworkServiceBuilder {
    private int bossLoopGroupCount;
    private int workerLoopGroupCount;
    private int port;
    //消息池
    private MessagePool msgPool;
    //消息分发处理
    private NetworkConsumer consumer;
    //消息事件监听
    private NetworkEventlistener networkEventlistener;

    private boolean webSocket;
    private boolean ssl;
    private String sslKeyCertChainFile;
    private String sslKeyFile;
    private int idleMaxTime;
    private int soRecBuf;
    private int soSendBuf;
    private List<ChannelHandler> channelHandlerList = new ArrayList<>();

    public NetworkServiceBuilder() {
    }

    //创建一个NetWorkService
    public NetworkService createService() {
        return new NetworkService(this);
    }

    public void addChannelHandler(ChannelHandler handler) {
        if(handler == null) {
            throw new NullPointerException("指定的handler为空");
        } else {
            this.channelHandlerList.add(handler);
        }
    }
}
