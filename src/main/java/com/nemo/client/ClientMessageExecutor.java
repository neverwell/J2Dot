package com.nemo.client;

import com.nemo.net.Message;
import com.nemo.net.MessageExecutor;
import com.nemo.net.NetworkConsumer;
import com.nemo.net.NetworkEventlistener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class ClientMessageExecutor extends MessageExecutor{
    public static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageExecutor.class);
    //与Client中的futureMap相同
    protected Map<Short, ClientFuture<Message>> futureMap;

    public ClientMessageExecutor(NetworkConsumer consumer, NetworkEventlistener listener, Map<Short, ClientFuture<Message>> futureMap, boolean idleCheck) {
        super(consumer, listener, idleCheck);
        this.futureMap = futureMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message m = (Message)msg;
        ClientFuture<Message> f = this.futureMap.get(m.getSequence());
        if(f != null) { //是同步的消息
            if(!f.isCancelled()) {
                f.result(m);
            }
        } else { //不是就交给MessageRouter分发
            super.channelRead(ctx, msg);
        }
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(this.isIdleCheck() && evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            IdleState state = idleStateEvent.state();
            if(state == IdleState.READER_IDLE) {
                this.listener.idle(ctx, state);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
