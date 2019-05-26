package com.kyoshii.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-25
 * @Description: 专门读取properties文件的工具类
 */
public final class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * @param fileName props文件的名字
     * @return Properties类
     */
    public static Properties loadProps(String fileName) {
        Properties properties = null;
        try (InputStream is = PropsUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            LOGGER.error("load property file failure", e);
        }
        return properties;
    }

    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.containsKey(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }

    public static int getInt(Properties properties, String key) {
        return getInt(properties, key, 0);
    }

    public static int getInt(Properties properties, String key, int defaultValue) {
        int value = defaultValue;
        if (properties.containsKey(key)) {
            value = Integer.parseInt(properties.getProperty(key));
        }
        return value;
    }

    public static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties, key, false);
    }

    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (properties.containsKey(key)) {
            value = Boolean.parseBoolean((properties.getProperty(key)));
        }
        return value;
    }
}
