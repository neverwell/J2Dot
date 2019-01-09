package com.nemo.game.map.remote;

import com.nemo.concurrent.AbstractCommand;
import com.nemo.game.GameContext;
import com.nemo.game.constant.GameConst;
import com.nemo.game.notice.ThreadModelProcessNotice;
import com.nemo.game.server.MessageProcessor;
import com.nemo.game.server.MessageRouter;

public class HostHeartCheckCommand extends AbstractCommand {

    @Override
    public void doAction() {
        MessageRouter router = RemoteHostManager.getInstance().getRouter();
        MessageProcessor processor = router.getProcessor(GameConst.QueueId.SEVEN_SERVER);

        processor.process(new ThreadModelProcessNotice() {
            @Override
            public void doAction() {
                AliveCheckManager.getInsatnce().doHeart();
            }
        }, 0L);
    }
}
