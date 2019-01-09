package com.nemo.game.constant;

public interface GameConst {

    interface QueueId {
        //登录和下线队列
        byte ONE_LOGIN_LOGOUT = 1;
        //玩家队列
        byte TWO_PLAYER = 2;
        //帮会模块
        byte THREE_UNION = 3;
        //副本模块 主要针对多人副本（战报）
        byte FOUR_INSTANCE = 4;
        //场景
        byte FIVE_MAP = 5;
        //通用处理器，主要针对内部使用，不对外
        byte SIX_COMMON = 6;
        //服务器之间的通信，比如说服务器登录，服务器信息同步
        byte SEVEN_SERVER = 7;
        //服务器之间远程调用，比如说notice，比如说玩家message转发
        byte EIGHT_RPC = 8;

    }
}
