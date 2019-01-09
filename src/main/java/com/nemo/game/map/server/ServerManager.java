package com.nemo.game.map.server;

import com.nemo.game.map.server.msg.ReqLoginMapServerMessage;
import com.nemo.game.map.server.msg.ReqPlayerListMessage;
import com.nemo.game.server.Session;

public class ServerManager {
    private static final ServerManager INSTANCE = new ServerManager();

    private ServerManager() {

    }

    public static ServerManager getInstance() {
        return INSTANCE;
    }

    public void login(ReqLoginMapServerMessage msg) {

    }

    public void syncPlayerList(Session session, ReqPlayerListMessage ReqPlayerListMessage){

    }
}
