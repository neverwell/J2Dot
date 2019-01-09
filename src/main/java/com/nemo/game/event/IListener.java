package com.nemo.game.event;

//事件监听器
public interface IListener {
    void update(EventType type, Object param);
}
