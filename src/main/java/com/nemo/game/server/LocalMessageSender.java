package com.nemo.game.server;

import com.nemo.game.entity.Union;
import com.nemo.game.map.MapManager;
import com.nemo.game.map.obj.IMapObject;
import com.nemo.game.map.obj.PlayerActor;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.processor.MessageSender;
import com.nemo.game.system.union.entity.MemberInfo;
import com.nemo.net.Message;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LocalMessageSender implements MessageSender{

    @Override
    public void sendMsg(long id, Message msg) {
        sendMsg(msg, id);
    }

    @Override
    public void sendMsg(Message msg, long id) {
        Session session = SessionManager.getInstance().getSession(id);
        if(session == null) {
            return;
        }
        session.sendMessage(msg);
    }

    @Override
    public void sendMsgToRids(Message msg, long... rids) {
        for(long rid : rids) {
            sendMsg(msg, rid);
        }
    }

    @Override
    public void sendMsgToRids(Message msg, Collection<Long> rids) {
        for(Long rid : rids) {
            if(rid != null) {
                sendMsg(msg, rid);
            }
        }
    }

    @Override
    public void sendMsgToRids(Message msg, Collection<Long> rids, long exceptRoleId) {
        for(Long rid : rids) {
            if(rid.equals(exceptRoleId)) {
                sendMsg(msg, rid);
            }
        }
    }

    @Override
    public void sendToWorld(Message msg) {
        Session[] sessions = SessionManager.getInstance().sessionArray();
        for(Session session : sessions) {
            if(session == null) {
                continue;
            }
            if(session.getRole() == null) {
                continue;
            }
            sendMsg(msg, session.getRole().getId());
        }
    }

    @Override
    public void sendToWorld(Message msg, int excludeHostId) {
        sendToWorld(msg);
    }

    @Override
    public void sendToUnion(Union union, Message msg) {
        if(union == null) {
            return;
        }
        Map<Long, MemberInfo> unionMembers = union.getMemberInfos();
        sendMsgToRids(msg, unionMembers.keySet());
    }

    @Override
    public void sendRoundMessage(Message msg, IMapObject obj) {
        GameMap map = MapManager.getInstance().getMap(obj.getMapId(), obj.getLine());
        if(map == null) {
            return;
        }
        Map<Long, IMapObject> watchers = map.getAoi().getWatchers(obj.getPoint());

        Set<Long> ids = new HashSet<>();
        for(IMapObject watcher : watchers.values()) {
            ids.add(watcher.getRid());
        }
        sendMsgToRids(msg, ids);
    }

    @Override
    public void sendRoundMessage(Message msg, IMapObject obj, long excludeId) {
        GameMap map = MapManager.getInstance().getMap(obj.getMapId(), obj.getLine());
        if (map == null) {
            return;
        }
        Map<Long, IMapObject> watchers = map.getAoi().getWatchers(obj.getPoint());
        Set<Long> ids = new HashSet<>();
        for (IMapObject watcher : watchers.values()) {
            ids.add(watcher.getRid());
        }
        ids.remove(excludeId);

        sendMsgToRids(msg, watchers.keySet());
    }

    @Override
    public void sendMapMessage(Message msg, GameMap map) {
        Set<Long> ids = new HashSet<>();
        Map<Long, PlayerActor> players = map.getPlayerMap();
        for(PlayerActor actor : players.values()) {
            ids.add(actor.getRid());
        }
        sendMsgToRids(msg, ids);
    }

    @Override
    public void sendToHost(Message msg) {
        //本地服就是发给自己服的玩家 和sendToWorld一个意思
        sendToWorld(msg);
    }
}
