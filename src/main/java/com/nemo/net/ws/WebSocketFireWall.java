package com.nemo.net.ws;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketFireWall extends ChannelInboundHandlerAdapter{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketFireWall.class);
    private String websocketPath;

    public WebSocketFireWall(String websocketPath) {
        this.websocketPath = websocketPath;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest req = (FullHttpRequest)msg;
        if(this.isNotWebSocketPath(req)) {
            ctx.close();
            LOGGER.error("非法的http请求：" + req.uri() + "," + ctx);
        }
    }

    private boolean isNotWebSocketPath(FullHttpRequest req) {
        return !req.uri().equals(this.websocketPath);
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if(!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof HandshakeComplete) {
            HandshakeComplete event = (HandshakeComplete)evt;
            ctx.pipeline().remove(this);
            LOGGER.error("websocket握手完毕，移除http防火墙：" + ctx);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
