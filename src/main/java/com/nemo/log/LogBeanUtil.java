package com.nemo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class LogBeanUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(LogBeanUtil.class);

    public static <T> Set<Class<T>> getSubClasses(String packageName, Class<T> parentClass) {
        Set<Class<T>> ret = new LinkedHashSet<>();
        boolean recursive = true;
        String packageDirName = packageName.replace('.', '/'); //包名转成文件路径 com.nemo.game.log -> com/nemo/game/log

        try {
            //class文件绝对目录
            Enumeration<URL> dirs = LogBeanUtil.class.getClassLoader().getResources(packageDirName);

            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol(); // file:/F:/.../com/nemo/game/log
                if("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8"); // "/F:/.../com/nemo/game/log"
                    ret.addAll(findSubClazzFromFile(packageName, filePath, recursive, parentClass)); //递归搜索查找
                } else if("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
                    ret.addAll(findSubClassFromJar(jar, packageName, packageDirName, parentClass, recursive));
                }
            }
        } catch (Throwable var9) {
//            LOGGER.error("读取日志Class文件出错", var9);
        }

        return ret;
    }

    public static <T> Set<Class<T>> findSubClassFromJar(JarFile jar, String packageName, String packageDirName, Class<T> parentClass, boolean recursive) {
        Set<Class<T>> ret = new LinkedHashSet<>();
        Enumeration<JarEntry> entries = jar.entries();

        while (true) {
            JarEntry entry;
            String name;
            String packageNameNew;
            int idx;
            do {
                do {
                    if(!entries.hasMoreElements()) {
                        return ret;
                    }

                    entry = entries.nextElement();
                    name = entry.getName();
                    if(name.charAt(0) == '/') {
                        name = name.substring(1);
                    }

                    packageNameNew = packageName;
                } while (!name.startsWith(packageDirName));

                idx = name.lastIndexOf(47);
                if(idx != -1) {
                    packageNameNew = name.substring(0, idx).replace('/', '.');
                }
            } while (idx == -1 && !recursive);

            if(name.endsWith(".class") && !entry.isDirectory()) {
                String className = name.substring(packageNameNew.length() + 1, name.length() - 6);
                if(className.endsWith("Log")) {
                    try {
                        Class<T> loadClass = (Class<T>) Class.forName(packageNameNew + "." + className, false, LogBeanUtil.class.getClassLoader());
                        if(parentClass.isAssignableFrom(loadClass) && !parentClass.equals(loadClass)) {
                            ret.add(loadClass);
                        }
                    } catch (Throwable t) {
                        LOGGER.error("读取Jar中的Class文件出错", t);
                    }
                }
            }
        }
    }

    public static <T> Set<Class<T>> findSubClazzFromFile(String packageName, String packagePath, final boolean recursive, Class<T> parentClass) {
        File dir = new File(packagePath);
        if(dir.exists() && dir.isDirectory()) {
            Set<Class<T>> ret = new LinkedHashSet<>();
            File[] dirfiles = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    //找出packagePath路径下所有文件夹和class文件
                    return recursive && file.isDirectory() || file.getName().endsWith(".class");
                }
            });

            for(int i = 0; i < dirfiles.length; ++i) {
                File file = dirfiles[i];
                if(file.isDirectory()) {
                    //子文件夹路径下继续找
                    ret.addAll(findSubClazzFromFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, parentClass));
                } else {
                    //class文件名去掉".class" 6个字符  XXXLog.class -> XXXLog
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    if(className.endsWith("Log")) {  //类必须Log命名结尾
                        try {
                            Class<T> clazz = (Class<T>)Class.forName(packageName + "." + className, false, LogBeanUtil.class.getClassLoader());
                            //isAssignableFrom判断该类是否是父类或父接口或是同一类
                            if(parentClass.isAssignableFrom(clazz) && !parentClass.equals(clazz)) {
                                ret.add(clazz);
                            }
                        } catch (Throwable t) {
                            LOGGER.error("读取文件夹中的Class文件出错", t);
                        }
                    }
                }
            }

            return ret;
        } else {
            return Collections.emptySet();
        }
    }
}
