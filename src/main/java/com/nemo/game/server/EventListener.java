package com.nemo.game.server;

import com.nemo.game.GameContext;
import com.nemo.game.constant.GameConst;
import com.nemo.game.system.attr.AttributeUtil;
import com.nemo.game.system.user.command.LogoutCommand;
import com.nemo.net.NetworkEventlistener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Netty事件监听器
public class EventListener implements NetworkEventlistener{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onConnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Session session = AttributeUtil.get(channel, SessionKey.SESSION);
        if(session == null) { //第一次新建立的连接肯定为null
            session = new Session();
            session.setChannel(channel);
            AttributeUtil.set(channel, SessionKey.SESSION, session); //存到channel的属性中
            LOGGER.debug("接收到新的连接：" + channel.toString());
        } else {
            LOGGER.error("新建立连接时候已存在Session" + channel.toString());
        }
    }

    @Override
    public void onDisconnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Session session = AttributeUtil.get(channel, SessionKey.SESSION);
        closeSession(session); //断开连接时的业务逻辑
    }

    public static void closeSession(Session session) {
        if(session == null || session.getUser() == null) {
            LOGGER.error("玩家断开连接[没有找到用户信息]");
            return;
        }
        MessageProcessor processor = GameContext.getGameServer().getRouter().getProcessor(GameConst.QueueId.ONE_LOGIN_LOGOUT);
        processor.process(new LogoutCommand(session), 0);
    }

    @Override
    public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("发生错误：" + ctx.channel(), cause);
    }

    @Override
    public void idle(ChannelHandlerContext ctx, IdleState state) {
        Channel channel = ctx.channel();
        LOGGER.error("连接闲置，主动断开连接,channel:【{}】,state:【{}】", channel, state);
        ctx.close();
        Session session = AttributeUtil.get(channel, SessionKey.SESSION);
        if(session == null) {
            return;
        }
        String name = "";
        if(session.getRole() == null) {
            name = session.getRole().getBasic().getName();
        }
        LOGGER.error("玩家【{}】-> 【{}】长时间未操作", session.getUser(), name);
    }
}
