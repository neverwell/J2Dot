package com.nemo.common.config;

import com.alibaba.druid.util.StringUtils;
import com.nemo.commons.util.FileLoaderUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ConfigDataXmlParser {
    private static final String CONFIG = "config";
    private static final String CLAZZ = "class";
    private static final String FILE = "file";
    private static final String KEY = "key";
    private static final String CONVERT = "convert";
    private static final String CONVERTER = "converter";
    private static final String FIELD = "field";
    private static final String CACHES = "caches";
    private static final String MAP = "map";
    private static final String CONFIGCACHES = "configcaches";
    private static final String CONFIGCACHE = "configcache";
    private static final String CONFIGDATA = "configdata";

    public ConfigDataXmlParser(){
    }

    public static List<ConfigDataContainer<?>> parse(String path) throws DocumentException, FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = FileLoaderUtil.findInputStreamByFileName(path);
        //从xml文件获取数据
        Document document = saxReader.read(inputStream);
        //获取根元素 文件中是configs
        Element root = document.getRootElement();
        List<ConfigDataContainer<?>> ret = new ArrayList();
        //遍历根元素的configdata子元素
        Iterator data = root.elementIterator(CONFIGDATA);

        while (data.hasNext()) {
            Element configdata = (Element)data.next();
            //遍历config元素
            Iterator it = configdata.elementIterator(CONFIG);

            while (it.hasNext()) {
                Element config = (Element)it.next();
                //获取属性
                String className = config.attributeValue(CLAZZ);
                String file = config.attributeValue(FILE);
                String key = config.attributeValue(KEY);
                //获取属性定义的转换器
                IConverter globalConverter = parseGlobalConverter(config);
                //获取子元素定义的转换器
                Map<String, IConverter> converterMap = parseConvert(config);
                //获取子元素cahches
                List<String> cacheList = parseCaches(config);
                Class<?> clazz = Class.forName(className);
                ConfigDataContainer<?> container = new ConfigDataContainer(clazz, file, key, converterMap, cacheList, globalConverter);
                ret.add(container);
            }
        }
        return ret;
    }

    public static List<IConfigCache> parseCache(String path) throws DocumentException, FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = FileLoaderUtil.findInputStreamByFileName(path);

        Document document = saxReader.read(inputStream);
        Element root = document.getRootElement();
        List<IConfigCache> ret = new ArrayList<>();
        Iterator<Element> data = root.elementIterator(CONFIGCACHES);

        while (data.hasNext()) {
            Element configCaches = data.next();
            Iterator<Element> it = configCaches.elementIterator(CONFIGCACHE);

            while (it.hasNext()) {
                Element config = it.next();
                String className = config.attributeValue(CLAZZ);
                Class<?> clazz = Class.forName(className);
                int size = clazz.getInterfaces().length;

                for(int i = 0; i < size; i++) {
                    if(clazz.getInterfaces()[i].getName().equals("com.nemo.common.config.IConfigCache")) {
                        IConfigCache cache = (IConfigCache) clazz.newInstance();
                        ret.add(cache);
                    }
                }
            }
        }

        return ret;
    }

    //子元素转换器
    private static Map<String, IConverter> parseConvert(Element config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, IConverter> converterMap = new HashMap<>();
        Iterator convertIt = config.elementIterator(CONVERT);

        while (convertIt.hasNext()) {
            Element convert = (Element) convertIt.next();
            String field = convert.attributeValue(FIELD);
            String converterClassName = convert.attributeValue(CONVERTER);
            Class<?> converterClass = Class.forName(converterClassName);
            IConverter converter = (IConverter) converterClass.newInstance();
            converterMap.put(field, converter);
        }

        return converterMap;
    }

    private static List<String> parseCaches(Element config) {
        List<String> ret = new ArrayList<>();
        Element caches = config.element(CACHES);
        if(caches == null) {
            return ret;
        } else {
            Iterator<Element> mapIt = caches.elementIterator(MAP);
            while (mapIt.hasNext()) {
                Element map = mapIt.next();
                String key = map.attributeValue(KEY);
                ret.add(key);
            }
            return ret;
        }
    }

    //所有属性都用到的转换器
    private static IConverter parseGlobalConverter(Element config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String globalConverterClasssName = config.attributeValue(CONVERTER);
        IConverter globalConverter = null;
        if(!StringUtils.isEmpty(globalConverterClasssName)) {
            Class<?> globalConverterClasss = Class.forName(globalConverterClasssName);
            globalConverter = (IConverter)globalConverterClasss.newInstance();
        }
        return globalConverter;
    }

    public static Class<?> getClazz(String path, String fileName) throws DocumentException, FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = FileLoaderUtil.findInputStreamByFileName(path);
        Document document = saxReader.read(inputStream);
        Element root = document.getRootElement();
        Class<?> ret = null;
        Iterator<Element> data = root.elementIterator(CONFIGDATA);

        while (true) {
            while (data.hasNext()) {
                Element configdata = data.next();
                Iterator<Element> it = configdata.elementIterator(CONFIG);

                while (it.hasNext()) {
                    Element config = it.next();
                    String className = config.attributeValue(CLAZZ);
                    String file = config.attributeValue(FILE);
                    if(fileName.equals(file)) {
                        Class<?> clazz = Class.forName(className);
                        ret = clazz;
                        break;
                    }
                }
            }

            return ret;
        }
    }
}
