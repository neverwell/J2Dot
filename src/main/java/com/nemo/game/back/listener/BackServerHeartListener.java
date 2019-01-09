package com.nemo.game.back.listener;

import com.nemo.game.back.entity.Announce;
import com.nemo.game.event.EventType;
import com.nemo.game.event.IListener;

import java.util.ArrayList;
import java.util.List;

public class BackServerHeartListener implements IListener{
    public static List<Announce> announces = new ArrayList<>();

    @Override
    public void update(EventType type, Object param) {

    }
}
