package com.nemo.game.event;

import com.sun.xml.internal.bind.v2.model.core.ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//由于事件都是预先注册好的，所以这里不考虑多线程问题，不允许在游戏运行过成中动态添加观察者
public class EventUtil {

    //游戏启动就初始化的监听者列表
    private final static Map<EventType, List<IListener>> PREPARED_LISTENERS = new HashMap<>();

    //游戏运行中动态添加的监听者列表
    private static ConcurrentHashMap<EventType, ConcurrentHashMap<IDynamicListener, Integer>> dynamicListeners = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(EventUtil.class);

    //EventRegister中用来注册
    static void addListener(IListener listener, EventType type) {
        List<IListener> listenerList = PREPARED_LISTENERS.get(type);
        if(listenerList == null) {
            listenerList = new ArrayList<>();
            PREPARED_LISTENERS.put(type, listenerList);
        }
        listenerList.add(listener);
    }

    public static void removeListener(IDynamicListener listener, EventType type) {
        ConcurrentHashMap<IDynamicListener, Integer> map = dynamicListeners.get(type);
        if(map == null) {
            return;
        }
        map.remove(listener);
    }

    public static void addListener(IDynamicListener listener, EventType type) {
        ConcurrentHashMap<IDynamicListener, Integer> map = dynamicListeners.get(type);
        if(map == null) {
            map = new ConcurrentHashMap<>(10);
            ConcurrentHashMap<IDynamicListener, Integer> exist = dynamicListeners.putIfAbsent(type, map);
            if(exist != null) {
                map = exist;
            }
        }
        map.put(listener, null);
    }

    //执行一类型的监听事件
    public static void fireEvent(EventType type, Object obj) {
        List<IListener> listenerList = PREPARED_LISTENERS.get(type);
        if(listenerList != null) {
            for(IListener listener : listenerList) {
                try {
                    listener.update(type, obj);
                } catch (Throwable e) {
                    LOGGER.error("事件执行错误", e);
                }
            }
        }

        ConcurrentHashMap<IDynamicListener, Integer> map = dynamicListeners.get(type);
        if(map != null) {
            for(IDynamicListener dynamicListener : map.keySet()) {
                try {
                    dynamicListener.update(type, obj);
                } catch (Throwable e) {
                    LOGGER.error("事件执行错误", e);
                }
            }
        }
    }

    public static void fireEvent(EventType type) {
        fireEvent(type, null);
    }
}
