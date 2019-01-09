package com.nemo.net;

import com.nemo.net.ws.WebSocketDecoder;
import com.nemo.net.ws.WebSocketEncoder;
import com.nemo.net.ws.WebSocketFireWall;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//通过builder构建启动netty服务
public class NetworkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);
    private int bossLoopGroupCount;
    private int workerLoopGroupCount;
    private int port;
    private ServerBootstrap bootstrap;
    private int state;
    NioEventLoopGroup bossGroup;
    NioEventLoopGroup workerGroup;
    private static final byte STATE_STOP = 0;
    private static final byte STATE_START = 1;

    //构建ServerBootstrap
    NetworkService(final NetworkServiceBuilder builder) {
        this.bossLoopGroupCount = builder.getBossLoopGroupCount();
        this.workerLoopGroupCount = builder.getWorkerLoopGroupCount();
        this.port = builder.getPort();

        this.bossGroup = new NioEventLoopGroup(this.bossLoopGroupCount);
        this.workerGroup = new NioEventLoopGroup(this.workerLoopGroupCount);

        final SslContext sslCtx;
        if(builder.isSsl()) {
            try{
                sslCtx = SslContextBuilder.forServer(new File(builder.getSslKeyCertChainFile()), new File(builder.getSslKeyFile())).build();
            } catch (SSLException var4) {
                throw new RuntimeException("sslCtx create failed.", var4);
            }
        } else {
            sslCtx = null;
        }

        this.bootstrap = new ServerBootstrap();
        this.bootstrap.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, Integer.valueOf(1024))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_RCVBUF, builder.getSoRecBuf() > 0 ? builder.getSoRecBuf() : 131072)
                .childOption(ChannelOption.SO_SNDBUF, builder.getSoSendBuf() > 0 ? builder.getSoSendBuf() : 131072)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pip = ch.pipeline();
                        if(sslCtx != null) {
                            pip.addLast("sslHandler", sslCtx.newHandler(ch.alloc()));
                        }

                        if(builder.isWebSocket()) {
                            pip.addLast(new ChannelHandler[]{new HttpServerCodec()});
                            pip.addLast(new ChannelHandler[]{new HttpObjectAggregator(65536)});
                            pip.addLast(new ChannelHandler[]{new WebSocketServerProtocolHandler("/", true)});
//                            pip.addLast(new ChannelHandler[]{new WebSocketFireWall("/")});
                            pip.addLast(new ChannelHandler[]{new WebSocketDecoder()});
                            pip.addLast(new ChannelHandler[]{new WebSocketEncoder()});
                        }

                        if (builder.getIdleMaxTime() > 0) {
                            pip.addLast("Idle", new IdleStateHandler(builder.getIdleMaxTime(), builder.getIdleMaxTime(), 0));
                        }

                        //入站数据解码解析成特定的子类Message
                        pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMsgPool()));
                        pip.addLast("NettyMessageEncoder", new MessageEncoder());

                        Iterator iterator = builder.getChannelHandlerList().iterator();
                        while(iterator.hasNext()) {
                            ChannelHandler handler = (ChannelHandler)iterator.next();
                            pip.addLast(new ChannelHandler[]{handler});
                        }

                        //连接建立断开事件处理和特定Message处理
                        MessageExecutor executor = new MessageExecutor(builder.getConsumer(),
                                builder.getNetworkEventlistener(), builder.getIdleMaxTime() > 0);
                        pip.addLast("NettyMessageExecutor", executor);
                    }
                });
    }

    //通过ServerBootstrap引导启动netty服务
    public void start() {
        try {
            ChannelFuture f = this.bootstrap.bind(this.port);
            f.sync();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }

        this.state = STATE_START;
        LOGGER.info("server on port: {} is start", this.port);
    }

    public void stop() {
        this.state = STATE_STOP;
        Future<?> bf = this.bossGroup.shutdownGracefully();
        Future<?> wf = this.workerGroup.shutdownGracefully();

        try {
            bf.get(5000L, TimeUnit.MILLISECONDS);
            wf.get(5000L, TimeUnit.MILLISECONDS);
        } catch (Exception var4) {
            LOGGER.info("Netty服务器关闭失败", var4);
        }
        LOGGER.info("Netty Server on port:{} is closed", this.port);
    }

    public boolean isRunning() {
        return state == STATE_START;
    }
}
