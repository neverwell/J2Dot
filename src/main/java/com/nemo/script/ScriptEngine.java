package com.nemo.script;

import com.nemo.script.annotation.Exclude;
import com.nemo.script.annotation.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

public class ScriptEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngine.class);
    private static Map<Class<?>, IScript> script_1_to_1 = new HashMap<>();
    private static Map<Class<?>, List<IScript>> script_1_to_n = new HashMap<>();
    private static boolean dev = false;
    private static String scriptJarFile;

    static {
        scriptJarFile = System.getProperty("game.script.file");
        String devBoolean = System.getProperty("game.script.dev", "false");
        dev = Boolean.parseBoolean(devBoolean);
        LOGGER.info("当前脚本模式：{},script path:{}", dev, scriptJarFile);
    }

    public static void setDev(boolean dev) {
        ScriptEngine.dev = dev;
    }

    public static void setScriptJarFile(String scriptJarFile) {
        ScriptEngine.scriptJarFile = scriptJarFile;
    }

    public static boolean load(String packageName) {
        URL url = null;
        JarFile jarFile = null;
        if(scriptJarFile != null) {
            File file = new File(scriptJarFile);
            if(file.exists()) {
                try {
                    url = file.toURI().toURL();
                    jarFile = new JarFile(new File(scriptJarFile));
                } catch (IOException e) {
                    LOGGER.error("脚本文件URL读取失败，file:" + file.getAbsolutePath(), e);
                }
            }
        }

        if(!dev && (url == null || jarFile == null)) {
            throw new ScriptException("非调试模式下必须通过-Dgame.script.file参数设置脚本jar包所在路径,设置调试模式请使用-Dgame.script.dev=true");
        } else {
            ScriptClassLoader classLoader = ScriptClassLoader.newInstance(url, packageName, dev);

            //找到指定包下所有带@Script注解的class文件
            Set<Class<?>> classList;
            if(dev) {
                classList = ClassUtil.findClassWithAnnotation(classLoader, packageName, Script.class);
            } else {
                classList = ClassUtil.findClassWithAnnotation(classLoader, packageName, jarFile, Script.class);
            }

            return checkAndLoad(classList, true);
        }
    }

    public static boolean load(String packageName, String bootstrapImpl) {
        URL url = null;
        if (scriptJarFile != null) {
            File file = new File(scriptJarFile);
            if (file.exists()) {
                try {
                    url = file.toURI().toURL();
                } catch (MalformedURLException var12) {
                    LOGGER.error("脚本文件URL读取失败,file:" + file.getAbsolutePath(), var12);
                }
            }
        }

        if (!dev && url == null) {
            throw new ScriptException("非调试模式下必须通过-Dscript.file参数设置脚本jar包所在路径");
        } else {
            ScriptClassLoader classLoader = ScriptClassLoader.newInstance(url, packageName, dev);

            Class clazz;
            try {
                clazz = classLoader.loadClass(bootstrapImpl);
            } catch (ClassNotFoundException var11) {
                LOGGER.error("找不到指定的脚本启动实现类：" + bootstrapImpl, var11);
                return false;
            }

            BootstrapScript bootstrapScript;
            try {
                bootstrapScript = (BootstrapScript)clazz.newInstance();
            } catch (Exception var10) {
                LOGGER.error("实例化脚本启动实现类发生错误：" + bootstrapImpl, var10);
                return false;
            }

            List<Class<? extends IScript>> scriptList = bootstrapScript.registerScript();
            List<Class<?>> uniqueScriptList = new ArrayList();
            Iterator<Class<? extends IScript>> iterator = scriptList.iterator();

            while(iterator.hasNext()) {
                Class<? extends IScript> script = iterator.next();
                if (uniqueScriptList.contains(script)) {
                    LOGGER.warn("脚本[{}]重复注册", script.getName());
                } else {
                    uniqueScriptList.add(script);
                }
            }

            return checkAndLoad(uniqueScriptList, false);
        }
    }

    //annotation 表示是否检测要排除标注了Exclude注解的类
    private static boolean checkAndLoad(Collection<Class<?>> classList, boolean annotation) {
        Map<Class<?>, IScript> script_1_to_1 = new HashMap<>();
        Map<Class<?>, List<IScript>> script_1_to_n = new HashMap<>();
        Map<IScript, Integer> orderMap = new HashMap<>();

        Iterator<Class<?>> iterator = classList.iterator();
        while (iterator.hasNext()) {
            Class scriptImpl = iterator.next();
            //必须实现IScript
            if(!IScript.class.isAssignableFrom(scriptImpl)) {
                continue;
            }
            if(annotation && scriptImpl.getAnnotation(Exclude.class) != null) {
                LOGGER.error("注册脚本[{}]不是IScript的子类", scriptImpl.getName());
                continue;
            }
            //该类实现的接口（排除直接实现IScript的）
            List<Class<?>> scriptIntfList = fetchInterface(scriptImpl);
            if (scriptIntfList.isEmpty()) {
                LOGGER.error("注册脚本[{}]没有实现任何脚本事件接口", scriptImpl.getName());
                continue;
            }
            try {
                IScript script = (IScript)scriptImpl.newInstance();
                //orderMap有什么用？？？
                if (annotation) {
                    Script scriptAnnotation = (Script)scriptImpl.getAnnotation(Script.class);
                    int order = scriptAnnotation.order();
                    orderMap.put(script, order);
                }

                Iterator<Class<?>> interfaceIterator = scriptIntfList.iterator();
                while(interfaceIterator.hasNext()) {
                    Class<?> interfaceClazz = interfaceIterator.next();
                    if (script_1_to_n.containsKey(interfaceClazz)) {
                        List<IScript> list = script_1_to_n.get(interfaceClazz);
                        list.add(script);
                        script_1_to_n.put(interfaceClazz, list);
                    } else if (script_1_to_1.containsKey(interfaceClazz)) {
                        IScript exist = script_1_to_1.remove(interfaceClazz);
                        List<IScript> list = new ArrayList<>();
                        list.add(exist);
                        list.add(script);
                        script_1_to_n.put(interfaceClazz, list);
                    } else {
                        script_1_to_1.put(interfaceClazz, script);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("检查接口注册失败,script:" + scriptImpl.getName(), e);
                return false;
            }
        }

        //排个序
        if (annotation) {
            script_1_to_n.forEach((k, v) -> {
                orderMap.getClass();
                v.sort(Comparator.comparingInt(orderMap::get));
            });
        }

        ScriptEngine.script_1_to_1 = script_1_to_1;
        ScriptEngine.script_1_to_n = script_1_to_n;
        return true;
    }

    private static List<Class<?>> fetchInterface(Class<?> scriptImpl) {
        Class<?>[] interfaces = scriptImpl.getInterfaces();
        List<Class<?>> ret = new ArrayList<>();

        //该类必须最少实现一个继承IScript的业务接口
        for(int i = 0; i < interfaces.length; ++i) {
            Class<?> clazz = interfaces[i];
            if (!clazz.equals(IScript.class) && IScript.class.isAssignableFrom(clazz)) {
                ret.add(clazz);
            }
        }

        return ret;
    }

    public static <T extends IScript> T get1t1(Class<T> clazz) {
        T ret = (T)script_1_to_1.get(clazz);
        if (ret != null) {
            return ret;
        } else {
            List<IScript> list = script_1_to_n.get(clazz);
            if (list != null && !list.isEmpty()) {
                LOGGER.warn("1对N类型的脚本被当做1对1类型来使用，脚本接口：{}", clazz.getName());
                return (T)list.get(0);
            } else {
                return null;
            }
        }
    }

    public static <T extends IScript> List<T> get1tn(Class<T> clazz) {
        List<IScript> ret = script_1_to_n.get(clazz);
        if (ret != null) {
            return (List<T>)ret;
        } else {
            IScript script = script_1_to_1.get(clazz);
            if (script != null) {
                LOGGER.warn("1对1类型的脚本被当做1对N类型来使用，脚本接口：{}", clazz.getName());
                ret = new ArrayList<>(1);
                ret.add(script);
                return (List<T>) ret;
            } else {
                return Collections.emptyList();
            }
        }
    }

//        while(true) {
//            Class scriptImpl;
//            Exclude excludeAnnotation;
//            label51: //跳到这里会去执行while(excludeAnnotation != null) 如果是继续do 就是跳过带Exclude注解的类
//            do {
//                while(iterator.hasNext()) {
//                    scriptImpl = iterator.next();
//                    //必须实现IScript
//                    if (IScript.class.isAssignableFrom(scriptImpl)) {
//                        if (!annotation) {
//                            break label51;
//                        }
//
//                        excludeAnnotation = (Exclude)scriptImpl.getAnnotation(Exclude.class);
//                        continue label51;
//                    }
//
//                    LOGGER.error("注册脚本[{}]不是IScript的子类", scriptImpl.getName());
//                }
//
//                if (annotation) {
//                    script_1_to_n.forEach((k, v) -> {
//                        orderMap.getClass();
//                        v.sort(Comparator.comparingInt(orderMap::get));
//                    });
//                }
//
//                ScriptEngine.script_1_to_1 = script_1_to_1;
//                ScriptEngine.script_1_to_n = script_1_to_n;
//                return true;
//            } while(excludeAnnotation != null);
//
//            //获取修饰符是否带abstract 1024
//            int modifiers = scriptImpl.getModifiers();
//            if (Modifier.isAbstract(modifiers) || Modifier.isAbstract(modifiers)) {
//                throw new ScriptException("脚本[{" + scriptImpl.getName() + "}]是一个抽象类或者接口");
//            }
//
//            //该类实现的接口（排除直接实现IScript的）
//            List<Class<?>> scriptIntfList = fetchInterface(scriptImpl);
//            if (scriptIntfList.isEmpty()) {
//                LOGGER.error("注册脚本[{}]没有实现任何脚本事件接口", scriptImpl.getName());
//            } else {
//                try {
//                    IScript script = (IScript)scriptImpl.newInstance();
//                    //orderMap有什么用？？？
//                    if (annotation) {
//                        Script scriptAnnotation = (Script)scriptImpl.getAnnotation(Script.class);
//                        int order = scriptAnnotation.order();
//                        orderMap.put(script, order);
//                    }
//
//                    Iterator<Class<?>> iter = scriptIntfList.iterator();
//                    while(iter.hasNext()) {
//                        Class<?> intf = iter.next();
//                        if (script_1_to_n.containsKey(intf)) {
//                            List<IScript> list = script_1_to_n.get(intf);
//                            list.add(script);
//                            script_1_to_n.put(intf, list);
//                        } else if (script_1_to_1.containsKey(intf)) {
//                            IScript exist = script_1_to_1.remove(intf);
//                            List<IScript> list = new ArrayList<>();
//                            list.add(exist);
//                            list.add(script);
//                            script_1_to_n.put(intf, list);
//                        } else {
//                            script_1_to_1.put(intf, script);
//                        }
//                    }
//                } catch (Exception e) {
//                    LOGGER.error("检查接口注册失败,script:" + scriptImpl.getName(), e);
//                    return false;
//                }
//            }
//        }
}
