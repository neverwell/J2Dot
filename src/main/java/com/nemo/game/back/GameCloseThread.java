package com.nemo.game.back;

import com.nemo.game.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//服务器关闭线程
public class GameCloseThread extends Thread{
    private static final Logger LOGGER = LoggerFactory.getLogger(GameCloseThread.class);
    private short sequence;
    //关闭来源 1命令行 2后台 3GM命令 4JVM钩子
    private int source;
    private Session session;

    public GameCloseThread(short sequence, int source, Session session) {
        super();
        this.sequence = sequence;
        this.source = source;
        this.session = session;
        this.setDaemon(false);
    }


    @Override
    public void run() {




    }
}
