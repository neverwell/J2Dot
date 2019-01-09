package com.nemo.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;

//在netty的各个事件方法中去调用触发
public interface NetworkEventlistener {
    void onConnected(ChannelHandlerContext ctx);

    void onDisconnected(ChannelHandlerContext ctx);

    void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause);

    void idle(ChannelHandlerContext ctx, IdleState state);
}
