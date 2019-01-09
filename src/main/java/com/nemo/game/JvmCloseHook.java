package com.nemo.game;

import com.nemo.game.back.GameCloseThread;

public class JvmCloseHook extends Thread{

    @Override
    public void run() {
        //1、踢掉所有玩家
        //2、保存所有玩家数据
        //3、关闭所有线程池
        //4、退出
        if(!GameContext.isServerCloseLogicExecuted()) {
            new GameCloseThread((short)0, 4, null).run();
        }
    }
}
