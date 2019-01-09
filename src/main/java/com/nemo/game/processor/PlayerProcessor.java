package com.nemo.game.processor;

import com.nemo.concurrent.IQueueDriverCommand;
import com.nemo.game.constant.GameConst;
import com.nemo.game.entity.Role;
import com.nemo.game.server.MessageProcessor;
import com.nemo.game.server.Session;
import com.nemo.net.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayerProcessor implements MessageProcessor{
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerProcessor.class);

    private static final int QUEUE_COUNT = 32;
    private Executor[] executors = new Executor[QUEUE_COUNT];

    public PlayerProcessor() {
        for(int i = 0; i < QUEUE_COUNT; i++) {
            final int index = i + 1;
            executors[i] = Executors.newSingleThreadExecutor(r -> {
                LOGGER.error("创建玩家逻辑线程-{}", index);
                return new Thread(r, "玩家逻辑线程-" + index);
            });
        }
    }

    @Override
    public void process(Message message) {
        Session session = (Session)message.getParam(); //NetworkConsumer分发前会设置
        if(session == null) {
            LOGGER.error("Session不存在，取消执行:{}", message.getClass().getName());
            return;
        }
        Role role = session.getRole();
        if(role == null) {
            String loginName = "未知用户";
            if (session.getUser() != null) {
                loginName = session.getUser().getLoginName();
            }
            LOGGER.error("用户:{} 的role不存在，取消执行:{}", loginName, message.getClass().getSimpleName());
            return;
        }

        int index = (int) (role.getId() % QUEUE_COUNT);
        executors[index].execute(message);  //保证同一个用户的消息都在一个线程上执行 避免并发问题
    }

    @Override
    public void process(IQueueDriverCommand command, long key) {
        //这里的key就是玩家role的id 一般在notice里调用
        int index = (int)(key % QUEUE_COUNT);
        executors[index].execute(command);
    }

    @Override
    public byte id() {
        return GameConst.QueueId.TWO_PLAYER;
    }
}
