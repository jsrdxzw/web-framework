package com.kyoshii.helper;

import com.kyoshii.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: Bean助手类
 */
public class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        // 将bean的实例放入map中
        for (Class<?> beanClass : beanClassSet) {
            Object bean = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, bean);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取具体的Bean实例
     *
     * @param cls Bean类
     * @param <T> Bean实例类型
     * @return Bean实例
     */
    public static <T> T getBean(Class<T> cls) {
        if (BEAN_MAP.containsKey(cls)) {
            return (T) BEAN_MAP.get(cls);
        } else {
            throw new RuntimeException("can not get bean by class:" + cls);
        }
    }

    /**
     * 这里的设置，可以为AOP设置代理类而使用
     * @param cls
     * @param object
     */
    public static void setBean(Class<?> cls,Object object){
        BEAN_MAP.put(cls,object);
    }


}
