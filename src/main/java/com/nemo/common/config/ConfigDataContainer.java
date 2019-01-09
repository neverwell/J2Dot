package com.nemo.common.config;

import com.nemo.commons.util.CSVData;
import com.nemo.commons.util.CSVUtil;
import com.nemo.commons.util.Cast;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

//每一类数据容器
public class ConfigDataContainer<T extends IConfigData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDataContainer.class);
    public static final String DEFAULT_CACHE = "default";
    private Map<String, Map<Object, T>> mapCaches;
    private List<T> list;
    public static Set<String> errKey = new HashSet<>();
    private Class<T> clazz;
    private String fileName;
    private String key; //查找的主键
    private List<String> extraKeyList;
    private Map<String, IConverter> converterMap;
    private IConverter globalConverter;

    public ConfigDataContainer(Class<T> clazz, String fileName, String key, Map<String, IConverter> converterMap, List<String> extraKeyList, IConverter globalConverter) {
        this.clazz = clazz;
        this.fileName = fileName;
        this.key = key;
        this.extraKeyList = extraKeyList;
        this.converterMap = converterMap;
        this.globalConverter = globalConverter;
    }

    public ConfigDataContainer(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getByIdAndCacheName(String cacheName, Object key) {
        Map<Object, T> cache = this.mapCaches.get(cacheName);
        return cache == null ? null : cache.get(key);
    }

    public boolean containsKey(String cacheName, Object key) {
        Map<Object, T> cache = this.mapCaches.get(cacheName);
        return cache == null ? null : cache.containsKey(key);
    }

    public List<T> getList() {
        return this.list;
    }

    public Map<Object, T> getMap(String cacheName) {
        Map<Object, T> ret = this.mapCaches.get(cacheName);
        return ret == null ? Collections.emptyMap() : ret;
    }

    public void reload() {
    }

    public void load(String filePath) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String file = filePath + this.fileName + ".csv";
        CSVData data = CSVUtil.read(file, 3);
        List<T> list = new ArrayList<>();
        //遍历每一行的数据
        Iterator<Map<String, String>> var5 = data.tableRows.iterator();

        Map defaultCache;
        while (var5.hasNext()) {
            defaultCache = var5.next();
            //将每一行的数据拼装成一个对象
            T config = (T)this.converterObject(defaultCache);
            list.add(config);
        }

        //主键索引map 默认
        Map<String, Map<Object, T>> mapCaches = new HashMap<>();
        defaultCache = this.buildCache(list, this.key);
        mapCaches.put(DEFAULT_CACHE, defaultCache);

        //caches子元素配置的索引map
        Iterator<String> var11 = this.extraKeyList.iterator();
        while (var11.hasNext()) {
            String key = var11.next();
            Map<Object, T> cache = this.buildCache(list, key);
            mapCaches.put(key, cache);
        }

        this.mapCaches = mapCaches;
        this.list = list;
        this.afterLoad();
        errKey.clear();
    }

    //根据给定key创建map索引
    public Map<Object, T> buildCache(List<T> list, String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] keys = key.split("#");
        PropertyDesc[] descs = new PropertyDesc[keys.length];

        for(int i = 0; i < keys.length; i++) {
            String keyField = keys[i];
            PropertyDesc desc = ReflectUtil.getPropertyDesc(this.clazz, keyField);
            if(desc == null || desc.getReadMethod() == null) {
                LOGGER.error("配置表：" + this.fileName + "中的" + keyField + " 字段在" + this.clazz.getName() + "没有找到setter和getter方法");
                return Collections.emptyMap();
            }
            descs[i] = desc;
        }

        Map<Object, T> cache = new HashMap<>();
        Iterator<T> var10 = list.iterator();

        while (var10.hasNext()) {
            T t = var10.next();
            Object value = this.buildKey(t, descs);
            cache.put(value, t);
        }

        return cache;
    }

    //反射获得对应索引值
    public Object buildKey(T object, PropertyDesc[] descs) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(descs.length == 1) {
            return descs[0].getReadMethod().invoke(object);
        } else {
            StringBuilder key = new StringBuilder();

            for(int i = 0; i < descs.length; i++) {
                PropertyDesc desc = descs[i];
                Object v = desc.getReadMethod().invoke(object);
                key.append(v.toString());
                key.append("#");
            }
            if(key.length() > 0) {
                key.deleteCharAt(key.length() - 1);
            }

            return key.toString();
        }
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    private Object convertValue(PropertyDesc desc, String oldValue) {
        Object value = oldValue;
        if(this.converterMap.containsKey(desc.getName())) {
            IConverter converter = this.converterMap.get(desc.getName());
            value = converter.convert(oldValue);
        } else if(desc.getPropertyType() != Integer.TYPE && desc.getPropertyType() != Integer.class) { //不是int和Integer class对象
            if(desc.getPropertyType() != Long.TYPE && desc.getPropertyType() != Long.class) {
                if (desc.getPropertyType() != Float.TYPE && desc.getPropertyType() != Float.class) {
                    if (desc.getPropertyType() != Double.TYPE && desc.getPropertyType() != Double.class) {
                        //#分割的可以转换为int[]和String[]
                        if(desc.getPropertyType() == int[].class) {
                            if(StringUtils.isEmpty(oldValue)) {
                                value = new int[0];
                            } else {
                                value = Cast.stringToInts(oldValue, "#");
                            }
                        } else if(desc.getPropertyType() == String[].class) {
                            if(StringUtils.isEmpty(oldValue)) {
                                value = new String[0];
                            } else {
                                value = oldValue.split("#");
                            }
                        }
                    } else {
                        value = Cast.toDouble(oldValue);
                    }
                } else {
                    if(StringUtils.isEmpty(oldValue)) {
                        oldValue = "0.0";
                    }
                    value = Float.parseFloat(oldValue);
                }
            } else {
                value = Cast.toLong(oldValue);
            }
        } else {
            value = Cast.toInteger(oldValue);
        }

        return value;
    }

    private T converterObject(Map<String, String> data) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(this.globalConverter != null) {
            return (T)this.globalConverter.convert(data); //每一个字段都用公共转换器转换
        } else {
            T config = this.clazz.newInstance();
            boolean isNull = true;
            Iterator<String> var4 = data.values().iterator();

            String key;
            while (var4.hasNext()) {
                key = var4.next();
                if(key != null && !key.equals("")) {
                    isNull = false;
                    break;
                }
            }
            if (isNull) {
                LOGGER.error("配置表：" + this.fileName + "存在空数据，用EM检查一下，防止getList时候出BUG！");
            }

            var4 = data.keySet().iterator();
            while (var4.hasNext()) {
                key = var4.next(); //每一个字段名
                String value = data.get(key); //每一个字段值
                PropertyDesc desc = ReflectUtil.getPropertyDesc(this.clazz, key); //获取类对应属性的定义信息
                if(desc == null) {
                    if(!errKey.contains(key)) {
                        LOGGER.error("配置表：" + this.fileName + "中的" + key + "字段在" + this.clazz.getName() + "没有找到setter和getter方法");
                        errKey.add(key);
                    }
                } else {
                    try {
                        Object newValue = this.convertValue(desc, value); //将文件的字段值转换为类里指定类型的属性值
                        Method method = desc.getWriteMethod();
                        method.invoke(config, newValue);
                    } catch (Exception e) {
                        LOGGER.error("", e);
                    }
                }
            }
            return config;
        }
    }

    private void afterLoad() {
        Iterator<T> var1 = this.list.iterator();

        while (var1.hasNext()) {
            IConfigData configData = var1.next();
            configData.afterLoad();
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
