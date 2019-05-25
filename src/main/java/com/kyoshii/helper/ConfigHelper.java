package com.kyoshii.helper;

import com.kyoshii.util.PropsUtil;

import java.util.Properties;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-25
 * @Description:
 */
public class ConfigHelper {

    private static final Properties APP_CONFIG = PropsUtil.loadProps("web-framework.properties");

    public static String getAppBasePackage(){
        return PropsUtil.getString(APP_CONFIG,"web_framework.app.base_package");
    }

    public static String getAppJspPath(){
        return PropsUtil.getString(APP_CONFIG,"web_framework.app.jsp_path","/WEB-INF/view/");
    }

    public static String getAppAssetPath(){
        return PropsUtil.getString(APP_CONFIG,"web_framework.app.asset_path","/asset/");
    }
}
