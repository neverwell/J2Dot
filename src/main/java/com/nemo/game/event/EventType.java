package com.nemo.game.event;

//事件类型
public enum EventType {
    PLAYER_LOGIN, // 玩家上线
    PLAYER_LOGOUT, // 玩家下线
    PLAYER_LEVEL_UP, // 玩家升级
    PLAYER_VIP_LEVEL_UP, // 玩家VIP等级变化

    PLAYER_MIDNIGHT, // 玩家凌晨事件
    PLAYER_SECOND_HEART, // 玩家心跳(一秒一次)
    PLAYER_MINUTE_HEART, // 玩家心跳(一分钟一次)
    PLAYER_HOUR_HEART,//玩家心跳（一小时一次）
    PLAYER_DIE, // 玩家死亡

    SERVER_MIDNIGHT, // 服务器凌晨事件
    SERVER_START_UP, // 服务器启动
    SERVER_SHUT_DOWN, // 服务器关闭
    SERVER_MINUTE_HEART, // 服务器心跳(一分钟一次)
    SERVER_SECOND_HEART, // 服务器心跳(一秒钟一次)

//    SERVER_HOUR5, // 5点事件
//    SERVER_HOUR20, // 20点事件 预留
//    SERVER_HOUR21, //21点事件  预留

    //释放技能前
    BEFORE_SKILL,
    //被攻击
    AFTER_HURT,
    //释放技能后
    AFTER_SKILL,
    //怪物死亡
    MONSTER_DIE,
    //进入地图
    ENTER_MAP,
    //退出地图
    QUIT_MAP,
    //格子站立事件
    STAND,
}
