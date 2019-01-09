package com.nemo.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfigCacheManager {
    private final Map<Class<?>, IConfigCache> caches = new HashMap<>();
    private static final ConfigCacheManager INSTANCE = new ConfigCacheManager();

    private ConfigCacheManager(){
    }

    public static ConfigCacheManager getInstance() {
        return INSTANCE;
    }

    public void init(String path) {
        try {
            List<IConfigCache> list = ConfigDataXmlParser.parseCache(path);
            Iterator<IConfigCache> var3 = list.iterator();

            while (var3.hasNext()) {
                IConfigCache cache = var3.next();
                cache.build(); //缓存逻辑
                this.caches.put(cache.getClass(), cache);
            }
        } catch (Exception var5) {
            throw new RuntimeException("全局缓存初始化失败", var5);
        }
    }

    public void init(String path, List<Class<?>> cacheList) {
        try {
            List<IConfigCache> list = ConfigDataXmlParser.parseCache(path);
            Iterator<IConfigCache> var4 = list.iterator();

            while (var4.hasNext()) {
                IConfigCache cache = var4.next();
                if(cacheList.contains(cache.getClass())) {
                    cache.build(); //缓存逻辑
                    this.caches.put(cache.getClass(), cache);
                }
            }
        } catch (Exception var6) {
            throw new RuntimeException("全局缓存初始化失败", var6);
        }
    }

    public <T extends IConfigCache> T getCache(Class<T> clazz) {
        return (T)this.caches.get(clazz);
    }

    public void reBuild() {
        Iterator<IConfigCache> var1 = this.caches.values().iterator();

        while (var1.hasNext()) {
            IConfigCache cache = var1.next();
            cache.build();
            this.caches.put(cache.getClass(), cache);
        }
    }

    public void reBuild(List<Class<?>> cacheList) {
        Iterator<IConfigCache> var2 = this.caches.values().iterator();

        while (var2.hasNext()) {
            IConfigCache cache = var2.next();
            if(cacheList.contains(cache.getClass())) {
                cache.build();
                this.caches.put(cache.getClass(), cache);
            }
        }
    }

    public boolean reBuild(Class<?> cacheClazz) {
        Iterator<IConfigCache> var2 = this.caches.values().iterator();

        IConfigCache cache;
        do {
            if(!var2.hasNext()) {
                return false;
            }

            cache = var2.next();
        } while (!cacheClazz.equals(cache.getClass()));

        cache.build();
        this.caches.put(cache.getClass(), cache);
        return true;
    }
}
