package com.nemo.game.processor;

import com.nemo.concurrent.AbstractCommand;
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

public class InstanceProcessor implements MessageProcessor{
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceProcessor.class);

    private Executor executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "多人副本线程"));

    @Override
    public void process(Message message) {
        Session session = (Session) message.getParam();
        if (session == null) {
            LOGGER.error("Session不存在，取消执行:{}", message.getClass().getName());
            return;
        }
        Role role = session.getRole();
        if (role == null) {
            String loginName = "未知用户";
            if (session.getUser() != null) {
                loginName = session.getUser().getLoginName();
            }
            LOGGER.error("用户:{} 的role不存在，取消执行:{}", loginName, message.getClass().getSimpleName());
            return;
        }
        executor.execute(message);
    }

    public void process(AbstractCommand command) {
        executor.execute(command);
    }

    @Override
    public void process(IQueueDriverCommand command, long id) {
        executor.execute(command);
    }

    @Override
    public byte id() {
        return GameConst.QueueId.FOUR_INSTANCE;
    }
}
