package com.nemo.game.notice;

import com.nemo.game.GameContext;
import com.nemo.game.server.MessageProcessor;

public class LocalNoticeSender implements NoticeSender{

    @Override
    public void sendNotice(byte processId, ProcessNotice notice, long id) {
        MessageProcessor processor = GameContext.getGameServer().getRouter().getProcessor(processId);

        processor.process(notice, id);
    }
}
