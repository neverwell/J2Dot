package com.nemo.client;

import com.nemo.game.map.remote.TransformMessageGroup;
import com.nemo.net.Message;
import com.nemo.net.MessageDecoder;
import com.nemo.net.MessageEncoder;
import com.nemo.net.MessageGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Client {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public final AttributeKey<Integer> ATTR_INDEX = AttributeKey.valueOf("INDEX");
    //用来心跳检测和断线重连的线程 唯一
    public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    //用来同步消息收发
    protected Map<Short, ClientFuture<Message>> futureMap = new ConcurrentHashMap<>();

    //同步消息添加的sequence
    private short sequence = 0;
    private final Object seq_lock = new Object();

    protected ClientBuilder builder;
    protected Channel channel;
    protected Bootstrap bootstrap;
    protected EventLoopGroup group;

    protected boolean stopped = false;
    protected boolean connected = false;
    protected boolean needReconnect = true;

    private int reconnectDelay = 2;

    public Client(){
    }

    public Client(final ClientBuilder builder) {
        this.builder = builder;
        if(builder.getNioEventLoopCount() > 0) {
            this.group = new NioEventLoopGroup(builder.getNioEventLoopCount());
        } else {
            this.group = new NioEventLoopGroup();
        }

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.group).channel(NioSocketChannel.class);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, true);
        this.needReconnect = builder.isNeedReconnect();
        final boolean idleCheck = builder.getMaxIdleTime() > 0;
        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pip = ch.pipeline();
                if(idleCheck) {
                    pip.addLast("Idle", new IdleStateHandler(builder.getMaxIdleTime(), 0, 0));
                }

                pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMsgPool()));
                pip.addLast("NettyMessageEncoder", new MessageEncoder());
                pip.addLast("NettyMessageExecutor", new ClientMessageExecutor(builder.getConsumer(), builder.getEventlistener(), Client.this.futureMap, idleCheck));
            }
        });
        //心跳检测机制
        if(builder.getHeartTime() > 0) {
            if(executor == null) {
                executor = this.createExecutor();
            }
            executor.scheduleAtFixedRate(new ClientHeart(builder.getPingMessageFactory(), this), 5L, (long)builder.getHeartTime(), TimeUnit.SECONDS);
        }
        //如果需要重连 要初始化线程池 断开后就用此线程池去执行延迟重连
        if(this.needReconnect && executor == null) {
            executor = this.createExecutor();
        }
    }

    public boolean sendMsg(List<Message> list) {
        try {
            Channel channel = this.getChannel(Thread.currentThread().getId());
            if(channel != null) {
                MessageGroup group = new MessageGroup();
                Iterator<Message> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Message message = iterator.next();
                    group.addMessage(message);
                }

                channel.writeAndFlush(group);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("消息发送失败.", e);
        }
        return false;
    }

    public boolean sendMsg(Message message) {
        Channel channel = this.getChannel(Thread.currentThread().getId());
        if(channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
            return true;
        } else {
            return false;
        }
    }

    public Message sendSyncMsg(Message message) {
        return this.sendSyncMsg(message, 200000L);
    }

    public Message sendSyncMsg(Message message, long timeout) {
        message.setSequence(this.getValidateId());
        boolean flag = false;

        Message msg;
        label70: {
            try {
                flag = true;
                Channel channel = this.getChannel(Thread.currentThread().getId());
                channel.writeAndFlush(message);
                ClientFuture<Message> f = ClientFuture.create();
                this.futureMap.put(message.getSequence(), f);
                msg = f.get(timeout, TimeUnit.MILLISECONDS);
                flag = false;
                break label70;
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            } finally {
                if(flag) {
                    ClientFuture future = this.futureMap.remove(message.getSequence());
                    if (future != null) {
                        future.cancel(false);
                    }
                }
            }

            ClientFuture<Message> future = this.futureMap.remove(message.getSequence());
            if(future != null) {
                future.cancel(false);
            }
            return null;
        }

        ClientFuture<Message> future = this.futureMap.remove(message.getSequence());
        if(future != null) {
            future.cancel(false);
        }
        return msg;
    }

    public void connect(boolean sync) throws Exception {
        ChannelFuture future = this.createChannel(0);
        if(sync) {
            future.sync();
        }
        this.connected = true;
    }

    public Channel getChannel(long id) {
        if(this.channel != null && this.channel.isActive()) {
            return this.channel;
        } else {
            LOGGER.error("暂时不能连接上服务器....");
            return null;
        }
    }

    public void stop() throws IOException {
        this.stopped = true;

        ChannelFuture cf = this.channel.close();
        try {
            cf.get(5000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.error("Channel关闭失败", e);
        }

        Future gf = this.group.shutdownGracefully();
        try {
            gf.get(5000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.info("EventLoopGroup关闭失败", e);
        }

        LOGGER.info("Netty Client on port:{} is closed", this.builder.getPort());
    }

    public ChannelFuture createChannel(int index) {
        ChannelFuture f = this.bootstrap.connect(this.builder.getHost(), this.builder.getPort());
        f.addListener(new ChannelConnectListener(this, index));
        return f;
    }

    public void registerChannel(int index, Channel channel) {
        this.channel = channel;
        //添加断开连接事件监听器
        channel.closeFuture().addListener(new ChannelDisconnectedListener(this, index));
    }

    public void unregisterChannel(int index) {
        this.channel = null;
    }

    public void ping(Message msg) {
        this.sendMsg(msg);
    }

    public void stopQuickly() throws IOException {
        this.stopped = true;
        this.channel.close();
        this.group.shutdownGracefully();
    }

    public short getValidateId() {
        synchronized (this.seq_lock) {
            ++this.sequence;
            return this.sequence;
        }
    }

    public ClientBuilder getBuilder() {
        return builder;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public ScheduledExecutorService createExecutor() {
        return Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Client心跳和断线重连线程"));
    }

    public int getReconnectDelay(int index) {
        return this.reconnectDelay;
    }

    public void resetReconnectDelay(int index) {
        this.reconnectDelay = 1;
    }

    public void countReconnectDelay(int index) {
        this.reconnectDelay <<= 1; //每次重连延迟时间翻倍
        if(this.reconnectDelay > 60) {
            this.reconnectDelay = 20;
        }
    }

    public int getIndex(Channel channel) {
        Integer index = channel.attr(this.ATTR_INDEX).get();
        return index == null ? 0 : index.intValue();
    }

    public boolean isEmpty() {
        return this.channel == null;
    }

    public Channel[] getChannelArray() {
        return new Channel[]{this.channel};
    }

    public Channel getChannelByIndex(int index) {
        return this.channel;
    }
}
