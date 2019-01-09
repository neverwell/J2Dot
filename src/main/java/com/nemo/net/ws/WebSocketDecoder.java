package com.nemo.net.ws;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

public class WebSocketDecoder extends ChannelInboundHandlerAdapter{
    public WebSocketDecoder(){
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof BinaryWebSocketFrame) {
            super.channelRead(ctx, ((BinaryWebSocketFrame)msg).content());
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
