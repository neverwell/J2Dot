package com.nemo.game.processor;

import com.nemo.game.entity.Union;
import com.nemo.game.map.obj.IMapObject;
import com.nemo.game.map.scene.GameMap;
import com.nemo.net.Message;

import java.util.Collection;

public interface MessageSender {

    void sendMsg(long id, Message msg);

    void sendMsg(Message msg, long id);

    void sendMsgToRids(Message msg, long... rids);

    void sendMsgToRids(Message msg, Collection<Long> rids);

    void sendMsgToRids(Message msg, Collection<Long> rids, long exceptRoleId);

    void sendToWorld(Message msg) ;

    void sendToWorld(Message msg, int excludeHostId);

    void sendToHost(Message msg);

    void sendToUnion(Union union, Message msg) ;

    void sendRoundMessage(Message msg, IMapObject obj);

    void sendRoundMessage(Message msg, IMapObject obj, long excludeId);

    void sendMapMessage(Message msg, GameMap map);
}
