package com.nemo.game.map.remote;

import com.nemo.concurrent.AbstractCommand;
import com.nemo.game.GameContext;

public class FetchRemoteInfoCommand extends AbstractCommand{

    String url;

    int retryCount = 0;

    public FetchRemoteInfoCommand(String url) {
        this.url = url;
    }

    @Override
    public void doAction() {
        if(GameContext.getOption().getRemoteInfoType() == 1) {
            //连接远程服务器
            RemoteHostManager.getInstance().init(GameContext.getOption().getRemoteHost());
            //初始化一起跨服的区服列表

        } else {


        }
    }
}
