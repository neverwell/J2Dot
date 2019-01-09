package com.nemo.game.map.remote;

import com.nemo.client.Client;
import com.nemo.client.ClientBuilder;
import com.nemo.client.ClientHeart;
import com.nemo.client.ClientListener;
import com.nemo.game.GameContext;
import com.nemo.game.constant.GameConst;
import com.nemo.game.map.remote.msg.ReqLoginMapServerMessage;
import com.nemo.game.map.remote.msg.ReqPingMessage;
import com.nemo.game.map.remote.msg.ResLoginMapServerMessage;
import com.nemo.game.map.remote.msg.ResPongMessage;
import com.nemo.game.map.remote.msg.bean.HostInfo;
import com.nemo.game.map.remote.tranfrom.TransformToHostMessage;
import com.nemo.game.map.remote.tranfrom.TransformToMapMessage;
import com.nemo.game.map.remote.tranfrom.TransformToNoticeMessage;
import com.nemo.game.map.remote.tranfrom.TransformToPlayerMessage;
import com.nemo.game.map.remote.tranfrom.TransformToWorldMessage;
import com.nemo.game.notice.ThreadModelProcessNotice;
import com.nemo.game.processor.RPCProcessor;
import com.nemo.game.processor.ServerProcessor;
import com.nemo.game.server.GameMessagePool;
import com.nemo.game.server.MessageProcessor;
import com.nemo.game.server.MessageRouter;
import com.nemo.game.server.Session;
import com.nemo.game.server.SessionKey;
import com.nemo.game.system.attr.AttributeUtil;
import com.nemo.game.system.attr.entity.Attribute;
import com.nemo.net.Message;
import com.nemo.net.NetworkEventlistener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

@Setter
@Getter
@Slf4j
public class RemoteHost {

    static final ReqPingMessage PING = new ReqPingMessage();
    static final ResPongMessage PONG = new ResPongMessage();

    //远程主机id
    private int id;
    //ip
    private String host;
    //端口
    private int port;
    //远程主机客户端
    private Client client;
    //是否已登录
    private boolean login;
    //是否server端的client 不是真的netty客户端 仅保存对应host的一组channel
    private boolean serverSide;
    //开服时间
    private long openTime;
    //合服时间
    private long combineTime;

    public RemoteHost() {

    }

    public RemoteHost(boolean serverSide) {
        this.serverSide = serverSide;
        if(serverSide) {
            client = new ServerSideClient();
        }
    }

    //获取开服天数，开服首日算作第一天
    public int getOpenServerDay() {
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.openTime), ZoneId.systemDefault());
        return (int) (LocalDateTime.now().getLong(ChronoField.EPOCH_DAY) - ldt.getLong(ChronoField.EPOCH_DAY) + 1);
    }

    //获取合服天数，合服首日算第一天
    public int getCombineServerDay() {
        if (this.combineTime == 0L) {
            return 0;
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.combineTime), ZoneId.systemDefault());
        return (int) (LocalDateTime.now().getLong(ChronoField.EPOCH_DAY) - ldt.getLong(ChronoField.EPOCH_DAY) + 1);
    }

    public void connect() {
        try {
            //不使用client底层心跳检测机制 使用外部逻辑检查 实现更完善判断
            ClientBuilder builder = new ClientBuilder();

            //只关本机的服务器
            builder.setHost(host);
            builder.setPort(port);
            builder.setEventlistener(new RemoteEventListener());
            builder.setConsumer(RemoteHostManager.getInstance().getRouter());
            builder.setMsgPool(new RemoteMessagePool());
            builder.setClientListener(new RemoteClientListener());
            builder.setNeedReconnect(true);
            builder.setPooled(false);
//        builder.setPingMessageFactory(new RemotePingFactory());
//        builder.setHeartTime(2);
//        builder.setMaxIdleTime(10);
            client = builder.createClient(); //构建netty客户端
            client.connect(true);
        } catch (Exception e) {
            log.error("连接失败：" + this.host + ":" + this.port, e);
        }
    }

    public void registerChannel(Channel channel) {
        this.client.registerChannel(0, channel);
    }

    public void unregisterChannel(int index) {
        this.client.unregisterChannel(index);
    }

    static class RemoteConsumer extends MessageRouter {

        @Override
        public void register() {
            this.registerProcessor(GameConst.QueueId.SEVEN_SERVER, new ServerProcessor());
            this.registerProcessor(GameConst.QueueId.EIGHT_RPC, new RPCProcessor());
        }
    }

    static class RemoteMessagePool extends GameMessagePool {
        RemoteMessagePool() {
            register(new TransformToMapMessage(), GameConst.QueueId.EIGHT_RPC);
            register(new TransformToPlayerMessage(), GameConst.QueueId.EIGHT_RPC);
            register(new TransformToNoticeMessage(), GameConst.QueueId.EIGHT_RPC);
            register(new TransformToWorldMessage(), GameConst.QueueId.EIGHT_RPC);
            register(new TransformToHostMessage(), GameConst.QueueId.EIGHT_RPC);

            register(new ResPongMessage(), GameConst.QueueId.SEVEN_SERVER);
            register(new ResLoginMapServerMessage(), GameConst.QueueId.SEVEN_SERVER);
        }
    }

    static class RemoteEventListener implements NetworkEventlistener {

        @Override
        public void onConnected(ChannelHandlerContext ctx) {
            Channel channel = ctx.channel();
            Session session = AttributeUtil.get(channel, SessionKey.SESSION);
            if(session == null) {
                session = new Session();
                session.setChannel(channel);
                AttributeUtil.set(channel, SessionKey.SESSION, session);
                log.error("接收到新的连接：" + channel.toString());
            } else {
                log.error("新连接建立时已存在Session，注意排查原因" + channel.toString());
            }
        }

        @Override
        public void onDisconnected(ChannelHandlerContext ctx) {
            Channel channel = ctx.channel();
            MessageRouter router = RemoteHostManager.getInstance().getRouter();
            MessageProcessor processor = router.getProcessor(GameConst.QueueId.SEVEN_SERVER);
            processor.process(new ThreadModelProcessNotice() {
                @Override
                public void doAction() {
                    RemoteHostManager.getInstance().unregister(channel);
                }
            }, 0L);
        }

        @Override
        public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {
            log.error("网络发生异常:" + ctx, cause);
        }

        @Override
        public void idle(ChannelHandlerContext ctx, IdleState state) {

        }
    }

    static class RemotePingFactory implements ClientHeart.PingMessageFactory {
        @Override
        public Message getPingMessage() {
            return PING;
        }
    }

    //客户端连接完成后监听器执行的回调
    static class RemoteClientListener implements ClientListener {

        @Override
        public void afterConnected(Channel channel, int index) {
            //客户端连接完成后向远程发登录请求
            ReqLoginMapServerMessage req = new ReqLoginMapServerMessage();
            HostInfo hostInfo = new HostInfo();
            hostInfo.setHostId(GameContext.getOption().getServerId());
            hostInfo.setHostPort(GameContext.getOption().getGameServerPort());
            hostInfo.setHostIp(GameContext.getOption().getHost());
            hostInfo.setIndex(index);
            hostInfo.setOpenTime(GameContext.getOpenDayZeroTime());
            hostInfo.setCombineTime(GameContext.getCombineDayZeroTime());
            req.setHost(hostInfo);
            channel.writeAndFlush(req);
            log.error("连接成功请求登录,id:{},host:{},port:{},index:{}",hostInfo.getHostId(), hostInfo.getHostIp(), hostInfo.getHostPort(), index);
        }
    }
}
