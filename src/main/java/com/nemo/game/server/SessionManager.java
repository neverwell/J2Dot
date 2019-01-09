package com.nemo.game.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();

    private Map<Long, Session> userSessionMap = new ConcurrentHashMap<>();

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public Session getSession(long uid) {
        return userSessionMap.get(uid);
    }

    public void register(long uid, Session session) {
        userSessionMap.put(uid, session);
    }

    public void unregister(long uid) {
        userSessionMap.remove(uid);
    }

    public Session[] sessionArray() {
        //toArray传不传都一样 底层concurrentHashMap会检测
        return userSessionMap.values().toArray(new Session[0]);
    }

    public int getSessionCount() {
        int count = 0;
        for(Long uid : userSessionMap.keySet()) {
            if(uid == null) {
                continue;
            }
            Session session = userSessionMap.get(uid);
            if(session == null) {
                continue;
            }
            if(session.getUser() == null || session.getRole() == null) {
                continue;
            }
            count++;
        }
        return count;
    }

    public int getSessionCountByQudao(int qudao) {
        int count = 0;
        for(Long uid : userSessionMap.keySet()) {
            if(uid == null) {
                continue;
            }
            Session session = userSessionMap.get(uid);
            if(session == null) {
                continue;
            }
            if(session.getUser() == null || session.getRole() == null) {
                continue;
            }
            if(session.getUser().getQudao() != qudao) {
                continue;
            }
            count++;
        }
        return count;
    }
}
