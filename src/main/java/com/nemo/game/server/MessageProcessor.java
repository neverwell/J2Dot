package com.nemo.game.server;

import com.nemo.concurrent.IQueueDriverCommand;
import com.nemo.game.notice.ProcessNotice;
import com.nemo.net.Message;

//消息处理器 将消息交给指定线程去处理
public interface MessageProcessor {

    void process(Message message);

    void process(IQueueDriverCommand command, long id);

    default void process(ProcessNotice notice, long id) {
        process((IQueueDriverCommand)notice, id);
    }

    byte id();
}
