package com.nemo.game.processor;

import com.nemo.concurrent.IQueueDriverCommand;
import com.nemo.game.constant.GameConst;
import com.nemo.game.server.MessageProcessor;
import com.nemo.net.Message;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginProcessor implements MessageProcessor{

    private Executor executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "登录线程"));

    @Override
    public void process(Message message) {
        this.executor.execute(message);
    }

    @Override
    public void process(IQueueDriverCommand command, long id) {
        this.executor.execute(command);
    }

    @Override
    public byte id() {
        return GameConst.QueueId.ONE_LOGIN_LOGOUT;
    }
}
