package com.kyoshii.helper;

import com.kyoshii.annotation.Controller;
import com.kyoshii.annotation.Service;
import com.kyoshii.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 类操作助手类
 */
public class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * @return 应用指定包名的所有类
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * @return 应用包下的所有Service类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classes = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)){
                classes.add(cls);
            }
        }
        return classes;
    }

    /**
     * @return 应用包下的所有Controller类
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classes = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)){
                classes.add(cls);
            }
        }
        return classes;
    }

    /**
     * @return 应用包下的所有Bean类
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classes = new HashSet<>();
        classes.addAll(getServiceClassSet());
        classes.addAll(getControllerClassSet());
        return classes;
    }
}
