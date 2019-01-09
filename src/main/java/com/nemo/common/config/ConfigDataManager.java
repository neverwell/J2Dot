package com.nemo.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfigDataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDataManager.class);
    private static final ConfigDataManager INSTANCE = new ConfigDataManager();
    private final Map<Class<?>, ConfigDataContainer<?>> configContainters = new HashMap<>();

    public static ConfigDataManager getInstance() {
        return INSTANCE;
    }

    private ConfigDataManager(){
    }

    public <T extends IConfigData> T getByIdAndCacheName(Class<T> clazz, String cacheName, Object id) {
        ConfigDataContainer<T> container = (ConfigDataContainer) this.configContainters.get(clazz);
        return container == null ? null : container.getByIdAndCacheName(cacheName, id);
    }

    public <T extends IConfigData> T getById(Class<T> clazz, Object id) {
        return this.getByIdAndCacheName(clazz, "default", id);
    }

    public <T extends IConfigData> boolean containsKey(Class<T> clazz, String cacheName, Object id) {
        ConfigDataContainer<T> container = (ConfigDataContainer) this.configContainters.get(clazz);
        return container == null ? false : container.containsKey(cacheName, id);
    }

    public <T extends IConfigData> boolean containsKey(Class<T> clazz, Object id) {
        return this.containsKey(clazz, "default", id);
    }

    public <T extends IConfigData> List<T> getList(Class<T> clazz) {
        ConfigDataContainer<T> container = (ConfigDataContainer) this.configContainters.get(clazz);
        return container == null ? null : container.getList();
    }

    public <T extends IConfigData> void reload(Class<T> clazz) {
    }

    public void init(String path) {
        String xmlPath = "data_config.xml";

        try {
            List<ConfigDataContainer<?>> configDatas = ConfigDataXmlParser.parse(xmlPath);
            LOGGER.info("配置条数：" + configDatas.size());
            Iterator<ConfigDataContainer<?>> var4 = configDatas.iterator();

            while (var4.hasNext()) {
                ConfigDataContainer<?> container = var4.next();
                container.load(path);
                this.configContainters.put(container.getClazz(), container);
            }
        } catch (Exception var6) {
            LOGGER.error("加载配置文件失败...", var6);
            throw new RuntimeException(var6);
        }

        ConfigCacheManager.getInstance().init(xmlPath);
    }

    public void init(String path, List<Class<?>> configList, List<Class<?>> cacheList) {
        String xmlPath = "data_config.xml";

        try {
            List<ConfigDataContainer<?>> configDatas = ConfigDataXmlParser.parse(xmlPath);
            Iterator var6 = configDatas.iterator();

            while(var6.hasNext()) {
                ConfigDataContainer<?> container = (ConfigDataContainer)var6.next();
                if (configList.contains(container.getClazz())) {
                    container.load(path);
                    this.configContainters.put(container.getClazz(), container);
                }
            }
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }

        ConfigCacheManager.getInstance().init(xmlPath, cacheList);
    }

    public void reload(String path) {
        Iterator<ConfigDataContainer<?>> var2 = this.configContainters.values().iterator();

        while (var2.hasNext()) {
            ConfigDataContainer container = var2.next();

            try {
                container.load(path);
                this.configContainters.put(container.getClazz(), container);
            } catch (Exception var5) {
                LOGGER.error("重新加载" + container.getFileName() + "配置表出错，class:" + container.getClazz());
                LOGGER.error("", var5);
            }
        }
        ConfigCacheManager.getInstance().reBuild();
    }

    public void reload(String path, String configName, List<Class<?>> cacheList) {
        Iterator<ConfigDataContainer<?>> var4 = this.configContainters.values().iterator();

        while (var4.hasNext()) {
            ConfigDataContainer container = var4.next();

            try {
                if (configName.equals(container.getFileName())) {
                    container.load(path);
                    this.configContainters.put(container.getClazz(), container);
                }
            } catch (Exception var7) {
                LOGGER.error("重新加载" + container.getFileName() + "配置表出错，class:" + container.getClazz());
                LOGGER.error("", var7);
            }
        }

        if(cacheList != null) {
            ConfigCacheManager.getInstance().reBuild();
        }
    }

    public boolean reload(String path, Class<?> cacheClazz) {
        return ConfigCacheManager.getInstance().reBuild(cacheClazz);
    }

    public boolean reload(String path, String configName) {
        Iterator<ConfigDataContainer<?>> var3 = this.configContainters.values().iterator();

        while (var3.hasNext()) {
            ConfigDataContainer container = var3.next();

            try {
                if(configName.equals(container.getFileName())) {
                    container.load(path);
                    this.configContainters.put(container.getClazz(), container);
                    return true;
                }
            } catch (Exception var6) {
                LOGGER.error("重新加载" + container.getFileName() + "配置表出错，class:" + container.getClazz());
                LOGGER.error("", var6);
                return false;
            }
        }
        return false;
    }
}
