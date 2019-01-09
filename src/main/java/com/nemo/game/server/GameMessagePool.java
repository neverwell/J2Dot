package com.nemo.game.server;

import com.nemo.game.constant.GameConst;
import com.nemo.game.map.msg.ReqLoginMapMessage;
import com.nemo.game.system.user.msg.ReqHeartMessage;
import com.nemo.net.Message;
import com.nemo.net.MessagePool;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GameMessagePool implements MessagePool{
    private Map<Integer, Class<? extends Message>> pool = new HashMap<>();
    private Map<Integer, Byte> queueIdMap = new HashMap<>();

    //注册消息 如果新加模块，则添加一个新方法进来
    public GameMessagePool() {
        registerUser();
        registerMap();
    }

    //注册用户相关请求
    private void registerUser() {
        register(new ReqHeartMessage(), GameConst.QueueId.ONE_LOGIN_LOGOUT);
    }

    public void registerMap() {
        register(new ReqLoginMapMessage(), GameConst.QueueId.FIVE_MAP);
    }



    @Override
    public Message get(int messageId) {
        Class<? extends Message> clazz = pool.get(messageId);
        if(clazz != null) {
            int queueId = queueIdMap.get(messageId);
            try {
                Message msg = clazz.getDeclaredConstructor().newInstance();
                msg.setQueueId(queueId); //设置第几条线程去执行
                return msg;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private void register(int messageId, Class<? extends Message> clazz) {
        pool.put(messageId, clazz);
        //默认走玩家主逻辑队列
        queueIdMap.put(messageId, GameConst.QueueId.TWO_PLAYER);
    }

    protected void register(int messageId, Class<? extends Message> clazz, byte queueId) {
        pool.put(messageId, clazz);
        queueIdMap.put(messageId, queueId);
    }

    protected void register(AbstractMessage message) {
        pool.put(message.getId(), message.getClass());
        //默认走玩家主逻辑队列
        queueIdMap.put(message.getId(), GameConst.QueueId.TWO_PLAYER);
    }

    protected void register(AbstractMessage message, byte queueId) {
        pool.put(message.getId(), message.getClass());
        queueIdMap.put(message.getId(), queueId);
    }
}
