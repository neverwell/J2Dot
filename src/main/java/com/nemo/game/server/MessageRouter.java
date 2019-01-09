package com.nemo.game.server;

import com.nemo.game.constant.GameConst;
import com.nemo.game.processor.*;
import com.nemo.game.system.attr.AttributeUtil;
import com.nemo.game.system.user.msg.ReqHeartMessage;
import com.nemo.net.Message;
import com.nemo.net.NetworkConsumer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

//MessageExecutor将Message传给该类去处理
//不同线程之间通信寻找线程
public class MessageRouter implements NetworkConsumer{
    private static Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    private Map<Integer, MessageProcessor> processors = new HashMap<>();

    public MessageRouter() {
        register();
    }

    public void register() {
        //登录和下线
        this.registerProcessor(GameConst.QueueId.ONE_LOGIN_LOGOUT, new LoginProcessor());
        //玩家队列
        this.registerProcessor(GameConst.QueueId.TWO_PLAYER, new PlayerProcessor());
        //帮会队列
        this.registerProcessor(GameConst.QueueId.THREE_UNION, new UnionProcessor());
        //多人副本队列
        this.registerProcessor(GameConst.QueueId.FOUR_INSTANCE, new InstanceProcessor());
        //地图
        this.registerProcessor(GameConst.QueueId.FIVE_MAP, new MapProcessor());
        //通用
        this.registerProcessor(GameConst.QueueId.SIX_COMMON, new CommonProcessor());


    }

    public void registerProcessor(int queueId, MessageProcessor consumer) {
        processors.put(queueId, consumer);
    }

    @Override
    public void consume(Channel channel, Message msg) {
        //将消息分发到指定的队列中，该队列可能在同一个线程，也可能不同一个线程
        int queueId = msg.getQueueId();

        MessageProcessor processor = processors.get(queueId);
        if(processor == null) {
            LOGGER.error("找不到可用的消息处理器[{}]", queueId);
            return;
        }

        Session session = AttributeUtil.get(channel, SessionKey.SESSION);
        if(session == null) {
            return;
        }

        msg.setParam(session); //消息添加session信息

        String loginName = "";
        if (session.getUser() != null) {
            loginName = session.getUser().getLoginName();
        }
        if (msg.getClass() != ReqHeartMessage.class) {
            LOGGER.info("收到消息:{} {}", msg.getClass().getSimpleName(), loginName);
        }

        processor.process(msg);
    }

    public MessageProcessor getProcessor(int queueId) {
        return processors.get(queueId);
    }
}
