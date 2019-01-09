package com.nemo.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageExecutor extends ChannelInboundHandlerAdapter{
    private static Logger LOGGER = LoggerFactory.getLogger(MessageExecutor.class);
    private NetworkConsumer consumer;
    protected NetworkEventlistener listener;
    private boolean idleCheck = false;

    public MessageExecutor(NetworkConsumer consumer, NetworkEventlistener listener, boolean idleCheck) {
        this.consumer = consumer;
        this.listener = listener;
        this.idleCheck = idleCheck;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Message) {
            this.consumer.consume(ctx.channel(), (Message) msg);
        } else {
            ReferenceCountUtil.release(msg);
            LOGGER.error("不识别的msg类型：{}", msg.getClass().getName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //建立连接时 创建session
        this.listener.onConnected(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.listener.onDisconnected(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(this.idleCheck && evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            IdleState state = idleStateEvent.state();
            this.listener.idle(ctx, state);
        }
    }

    public void setConsumer(NetworkConsumer consumer) {
        this.consumer = consumer;
    }

    public NetworkConsumer getConsumer() {
        return consumer;
    }

    public void setListener(NetworkEventlistener listener) {
        this.listener = listener;
    }

    public NetworkEventlistener getListener() {
        return listener;
    }

    public void setIdleCheck(boolean idleCheck) {
        this.idleCheck = idleCheck;
    }

    public boolean isIdleCheck() {
        return idleCheck;
    }
}
