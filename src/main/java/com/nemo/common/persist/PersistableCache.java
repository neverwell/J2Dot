package com.nemo.common.persist;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.nemo.common.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

//某一类型数据的缓存
public class PersistableCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistableCache.class);
    private final Cache<Long, Persistable> dataMap;

    public PersistableCache(JdbcTemplate template, int size) {
        this.dataMap = CacheBuilder.newBuilder().concurrencyLevel(50).maximumSize((long)size).removalListener(
                (RemovalListener<Long, Persistable>) notification -> {
                    if(notification.getValue().isDirty()) {
                        long key = (notification.getKey()).longValue();
                        PersistableCache.LOGGER.error("脏数据从缓存中移除了:{}  reason {}", key, notification.getCause().name());
                    }
                }
        ).build();
    }

    public int size(){
        return (int)this.dataMap.size();
    }

    public Persistable get(long id) {
        return this.dataMap.getIfPresent(id);
    }

    public void put(Persistable obj) {
        this.dataMap.put(obj.getId(), obj);
    }

    public void remove(long id) {
        this.dataMap.invalidate(id);
    }

    public Map<Long, Persistable> allDatas() {
        return this.dataMap.asMap();
    }
}
