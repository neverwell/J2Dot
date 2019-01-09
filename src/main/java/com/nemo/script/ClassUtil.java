package com.nemo.script;

import oracle.jrockit.jfr.jdkevents.ThrowableTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
    private static final String PROTOCOL_FILE = "file";
    private static final String PROTOCOL_JAR = "jar";

    public static Set<Class<?>> findClassWithAnnotation(ClassLoader classLoader, String packageName, Class<? extends Annotation> annotationClass) {
        if(classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
        }

        Set<Class<?>> allClazz = new LinkedHashSet<>();
        boolean recursive = true;
        String packageDir = packageName.replace('.', '/');

        try {
            Enumeration<URL> dirs = classLoader.getResources(packageDir);

            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if(PROTOCOL_FILE.equals(protocol)) {
                    //文件夹下找
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    allClazz.addAll(findClassFromDir(classLoader, packageName, filePath, recursive));
                } else if(PROTOCOL_JAR.equals(protocol)) {
                    //jar包里找
                    JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
                    allClazz.addAll(findClassFromJar(classLoader, jar, packageDir));
                }
            }
        } catch (Throwable t) {
            LOGGER.error("读取Class文件出错", t);
        }

        Set<Class<?>> ret = new LinkedHashSet<>();
        Iterator<Class<?>> iterator = allClazz.iterator();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            if(clazz.getAnnotation(annotationClass) != null) {
                LOGGER.info("发现脚本类：" + clazz.getName());
                ret.add(clazz);
            }
        }
        return ret;
    }

    public static Set<Class<?>> findClassWithAnnotation(ClassLoader classLoader, String packageName, JarFile file, Class<? extends Annotation> annotationClass) {
        if(classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
        }

        String packageDir = packageName.replace(".", "/");
        Set<Class<?>> allClazz = findClassFromJar(classLoader, file, packageDir);
        Set<Class<?>> ret = new LinkedHashSet<>();
        Iterator<Class<?>> iterator = allClazz.iterator();

        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            if(clazz.getAnnotation(annotationClass) != null) {
                LOGGER.info("发现脚本类：" + clazz.getName());
                ret.add(clazz);
            }
        }
        return ret;
    }

    private static Set<Class<?>> findClassFromJar(ClassLoader classLoader, JarFile jar, String packageDir) {
        Set<Class<?>> ret = new LinkedHashSet<>();
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(!entry.isDirectory()) {
                String name = entry.getName();
                if(name.startsWith(packageDir) && name.endsWith(".class")) {
                    name = name.replaceAll("/", ".");
                    name = name.substring(0, name.length() - 6);

                    try {
                        Class<?> clazz = Class.forName(name, false, classLoader);
                        ret.add(clazz);
                    } catch (Throwable t) {
                        LOGGER.error("读取Jar中的Class文件出错：" + name, t);
                    }
                }
            }
        }
        return ret;
    }

    private static Set<Class<?>> findClassFromDir(ClassLoader classLoader, String packageName, String filePath, boolean recursive) {
        File dir = new File(filePath);
        if(dir.exists() && dir.isDirectory()) {
            Set<Class<?>> ret = new LinkedHashSet<>();
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return recursive && pathname.isDirectory() || pathname.getName().endsWith(".class");
                }
            });

            for(int i = 0; i < files.length; ++i) {
                File file = files[i];
                if(file.isDirectory()) {
                    //文件夹递归查找
                    ret.addAll(findClassFromDir(classLoader, packageName + "." + file.getName(), file.getAbsolutePath(), recursive));
                } else {
                    //去掉".class"后缀
                    String className = file.getName().substring(0, file.getName().length() - 6);

                    try {
                        Class<?> clazz = Class.forName(packageName + "." + className, false, classLoader);
                        ret.add(clazz);
                    } catch (Throwable t) {
                        LOGGER.error("读取文件夹中Class文件出错", t);
                    }
                }
            }

            return ret;
        } else {
            return Collections.emptySet();
        }
    }
}
