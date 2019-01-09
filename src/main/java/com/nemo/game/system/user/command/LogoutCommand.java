package com.nemo.game.system.user.command;

import com.nemo.concurrent.AbstractCommand;
import com.nemo.game.server.Session;
import com.nemo.game.server.SessionKey;
import com.nemo.game.system.attr.AttributeUtil;
import com.nemo.game.system.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Netty断开连接事件里处理离线逻辑
public class LogoutCommand extends AbstractCommand{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutCommand.class);

    private Session session;

    public LogoutCommand(Session session) {
        this.session = session;
    }

    @Override
    public void doAction() {
        Boolean logoutHandled = AttributeUtil.get(session.getChannel(), SessionKey.LOGOUT_HANDLED);
        if(Boolean.TRUE.equals(logoutHandled)) {
            LOGGER.error("忘了断开连接时候玩家已经处理过下限事件[顶号] -> {}", session.getUser().toString());
            return;
        }
        AttributeUtil.set(session.getChannel(), SessionKey.LOGOUT_HANDLED, true);
        //登出业务
        UserManager.getInstance().logout(session);
        LOGGER.error("网络连接断开，处理玩家下线逻辑->{}", session.getUser().toString());
    }
}
