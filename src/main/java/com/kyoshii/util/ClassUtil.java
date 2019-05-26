package com.kyoshii.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-25
 * @Description: 类加载器加载配置文件定义的包路径下的所有类
 */
public class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";


    public static ClassLoader getClassLoader() {
        return ClassUtil.class.getClassLoader();
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        try {
            return Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class error", e);
        }
        return null;
    }


    public static Class<?> loadClass(String className) {
        return loadClass(className, false);
    }

    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        URL url = getClassLoader().getResource(packageName.replace(".", "/"));
        if (url != null) {
            String protocol = url.getProtocol();
            if (FILE_PROTOCOL.equals(protocol)) {
                String packagePath = url.getPath().replaceAll("%20", "");
                addClass(classSet, packagePath, packageName);
            } else if (JAR_PROTOCOL.equals(protocol)) {
                try {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> entries = jarFile.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry jarEntry = entries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("not supported type for load class file");
            }
        }
        return classSet;
    }


    /**
     * 获取一个包下的所有文件
     *
     * @param classSet
     * @param packagePath target包的全路径名
     * @param packageName 类的全名
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        try {
            packagePath = URLDecoder.decode(packagePath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("decode packagePath", e);
        }
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        if (files != null) {
            for (File file : files) {
                // target 里面的文件名字 有.class
                String fileOrDirectoryName = file.getName();
                if (file.isFile()) {
                    String className = "";
                    if (packageName != null) {
                        // 在target中获得具体的类名
                        className = fileOrDirectoryName.substring(0, fileOrDirectoryName.lastIndexOf("."));
                    }
                    className = packageName + "." + className;
                    doAddClass(classSet, className);
                } else {
                    // model..
                    String subPackagePath = fileOrDirectoryName;
                    // packagePath=/Users/.../web-framework/target/classes/com/kyoshii
                    if (!packagePath.isEmpty()) {
                        subPackagePath = packagePath + "/" + subPackagePath;
                    }
                    String subPackageName = fileOrDirectoryName;
                    if (packageName != null && !packageName.isEmpty()) {
                        subPackageName = packageName + "." + subPackageName;
                    }
                    addClass(classSet, subPackagePath, subPackageName);
                }
            }
        }

    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> aClass = loadClass(className);
        classSet.add(aClass);
    }

}
